package Kangaroo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Packets.ConnectionPacket;
import Packets.FriendsDataPacket;
import Packets.HomePacket;
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
	
	/** games : an ArrayList containing all games*/
	static ArrayList<Game> games;
	/** players : an ArrayList containing all thz connected players*/
	static ArrayList<Player> players;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Main thread : Creation of server data");
		Server server = new Server();
		server.open();
		
		games = new ArrayList<Game>();
		players = new ArrayList<Player>();
		
		// readPackets : an ArrayList containing the packet received from clients
		ArrayList<Packets> readPackets = new ArrayList<Packets>();
		
		// Read received packets by the server
		while(true)
		{
			readPackets = server.readBuffer.readPackets();
			
			for (int i = 0; i < readPackets.size(); i++)
			{
				// If the received packet is not null, analyze it.
				if (readPackets.get(i) != null)
				{
					/*
					 * Receive a ConnexionPacket
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(ConnectionPacket.class))
					{
						// When a client has been connected, create a player with his ip;
						players.add(new Player());		
						players.get(players.size() - 1).setIp( readPackets.get(i).getIp());
						
						// Update serverInfo for clients
						//serverInfoUpdated(cp, ServerInfoType.ExceptMe);
					}
					/*
					 * Receive Login packet 
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(LoginPacket.class))
					{
						// Try to login this client
						LoginPacket receivedPacket = (LoginPacket) readPackets.get(i);
						attemptToLogin(receivedPacket);
						
						// Send to the client (who sent the packet) the updated packet
						server.sendBuffer.sendPacket(readPackets.get(i));
						
						// If server accepted the request, send client data
						if (receivedPacket.accepted)
						{				
							// Send to the client his data
							server.sendBuffer.sendPacket(getPlayerFromIP(receivedPacket.getIp()).getPacket());
							
							// Send to the client the last news
							server.sendBuffer.sendPacket(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName(), receivedPacket.getIp()));		
							server.sendBuffer.sendPacket(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName(), receivedPacket.getIp()));		
		
							// Send to the client his friends
							server.sendBuffer.sendPacket(new FriendsDataPacket(receivedPacket.getIp()));
							
							// Send to the client the ladder and his position
							server.sendBuffer.sendPacket(new LadderDataPacket(receivedPacket.getIp()));
							
							// Send to his connected friends he is connected
							// TODO : send packets to his friends
						}
					}
					/*
					 * Receive a HomePacket (Nerisma)
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(HomePacket.class))
					{						
						// Cast packet
						HomePacket receivedPacket = (HomePacket) readPackets.get(i);
						
						// TODO fill packet
						
						// Re send filled packet
						server.sendBuffer.sendPacket(receivedPacket);
						
					}
					
					// TODO : manage the creation of games when receiving MatchMakingPacket
					/*{
						Thread t = new Thread(games.get(index));
						t.start();
					}*/
				}
				else // If the received packet is null, replace the -1.
				{
					System.out.println("A null packet was received");
				}
			}
		}
	}
	
	/**	Check if the client pseudo exists and check the password match with the pseudo
	 * 	@param packet the login packet
	 * 	@return true if it's exists and pwd match, false if isn't
	 */
	public static void attemptToLogin(LoginPacket packet)
	{	
		// If the player is already connected
		if (getPlayerFromPseudo(packet.pseudo) != null)
		{
			// TODO : disconnect the last cp and connect this new client
			packet.accepted = false;
			return;
		}
		
		// If this client is not connected
		
		// Everyone could connect with this pseudo
		if (packet.pseudo.equals("Kurond"))
		{
			packet.pwdMatch = true;
			packet.accepted = true;				
			getPlayerFromIP(packet.getIp()).setName(packet.pseudo);
		}
		else
		{
			// Check fields
			// TODO : this loop need to browse an ArrayList<Player>
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
						getPlayerFromIP(packet.getIp()).setName(packet.pseudo);
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
	}
	
	/** return a connected Player object matching with the pseudo in parameter
	 * @param pseudo : the player pseudo
	 * @return the player if it is connected, null otherwise
	 */
	public static Player getPlayerFromPseudo(String pseudo)
	{
		// Browse the list of connected players
		for (int i = 0; i < players.size(); i++)
		{
			// If the player is found
			if (players.get(i).getName().equals(pseudo))
				return players.get(i);
		}
		
		// If no player matching with the pseudo
		return null;
	}
	
	/**
	 * Get a kangaroo from the ip of a client
	 * @param ip
	 * @return the associated kangaroo or null if no kangaroo exist with this ip
	 */
	public static Player getPlayerFromIP(String ip)
	{
		// Browse the list of connected players
		for (Player player : players)
		{
			System.out.println(player.getIp() + " vs " + ip);
			
			// If the player is found
			if (player.getIp().equals(ip))
				return player;
		}
		
		System.out.println("null");
		
		// If no player matching with the ip
		return null;
	}

}
