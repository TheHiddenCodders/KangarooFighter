package Kangaroo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Packets.FriendsDataPacket;
import Packets.LadderDataPacket;
import Packets.LoginPacket;
import Packets.Packets;
import Server.Server;
import Utils.FileUtils;
import Utils.ServerUtils;

public class Main 
{
	public static BufferedInputStream inputReader;
	public static String msg = "";
	
	static ArrayList<Game> games;
	static ArrayList<Player> players;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Main thread : Creation of server data");
		Server server = new Server();
		server.open();
		
		games = new ArrayList<Game>();
		players = new ArrayList<Player>();
		
		ArrayList<Packets> readPackets;
		
		// Read Packet received by the server
		while(true)
		{
			readPackets = server.readBuffer.readPackets();
			
			for (Packets packet : readPackets)
			
			/**
			 * Receive Login packet 
			 */
			if (packet.getClass().isAssignableFrom(LoginPacket.class))
			{			
				LoginPacket receivedPacket = (LoginPacket) packet;
				attemptToLogin(receivedPacket);
				
				// Send to the client (who sent the packet) the updated packet
				server.sendBuffer.sendPacket(packet);
				
				// If server accepted the request, send client data
				if (receivedPacket.accepted)
				{				
					// Send to the client his data
					server.sendBuffer.sendPacket((Packets) getPlayerFromIP(receivedPacket.ip).getPacket());
					
					// Send to the client the last news
					server.sendBuffer.sendPacket((Packets) ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));		
					server.sendBuffer.sendPacket((Packets) ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));		

					// Send to the client his friends
					server.sendBuffer.sendPacket((Packets) new FriendsDataPacket());
					
					// Send to the client the ladder and his position
					server.sendBuffer.sendPacket((Packets) new LadderDataPacket());
					
					// Send to his connected friends he is connected
					// TODO : send packets to his friends
				}
			}
			
			// TODO : manage the creation of games when receiving MatchMakingPacket
			/*{
				Thread t = new Thread(games.get(index));
				t.start();
			}*/
		}
	}
	
	/**
	 * Check if the client pseudo exists, if it exists, it check the password match with the pseudo
	 * @param packet the login packet
	 * @return true if it's exists and pwd match, false if isn't
	 */
	public static void attemptToLogin(LoginPacket packet)
	{
		// If already log, unlog him
		for (int i = 0; i < players.size(); i++)
		{
			if (players.get(i).getName().equals(packet.pseudo))
			{
				packet.accepted = false;
				return;
			}
		}
		
		// Check fields
		for (File file : ServerUtils.getPlayersFiles())
		{
			// If pseudo exists
			if (file.getName().equals(packet.pseudo))
			{
				packet.pseudoExists = true;
				if (FileUtils.readString(new File(file.getAbsolutePath().concat("/pwd"))).get(0).equals(packet.pwd))
				{
					packet.pwdMatch = true;
					packet.accepted = true;				
					getPlayerFromIP(packet.ip).setName(packet.pseudo);
					break;
				}
				else
				{
					packet.pwdMatch = false;
				}
				break;
			}
			else
			{
				packet.pseudoExists = false;
			}
		}		
	}
	
	/**
	 * Get a kangaroo from the ip of a client
	 * @param ip
	 * @return the associated kangaroo or null if no kangaroo exist with this ip
	 */
	public static Player getPlayerFromIP(String ip)
	{
		for (Player player : players)
			if (player.getIp().equals(ip))
				return player;
		
		return null;
	}

}
