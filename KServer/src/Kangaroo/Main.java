package Kangaroo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Packets.ClientReadyPacket;
import Packets.ConnectionPacket;
import Packets.DisconnexionPacket;
import Packets.FriendsPacket;
import Packets.HomePacket;
import Packets.LadderPacket;
import Packets.LoginPacket;
import Packets.MatchMakingPacket;
import Packets.NewsPacket;
import Packets.Packets;
import Packets.PlayerPacket;
import Server.Server;
import Utils.FileUtils;
import Utils.ServerUtils;

public class Main 
{
	public static BufferedInputStream inputReader;
	public static String msg = "";
	
	static GameProcessor gp;
	/** players : an ArrayList containing all thz connected players*/
	static ArrayList<Player> players;
	/** ladder : contain the game ladder */
	static Ladder ladder;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Main thread : Creation of server data");
		
		// Launch server threads
		Server server = new Server();
		server.open();
		
		ladder = new Ladder("/KangarooFighters/Ladder/elo");

		players = new ArrayList<Player>();
		gp = new GameProcessor(players, server.sendBuffer);
		
		// Launch game threads
		Thread t = new Thread(gp);
		t.start();
		
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
						// TODO : fill playerPacket in -> ServerUtils.getPlayerDataPacket
						PlayerPacket playerPacket = new PlayerPacket(readPackets.get(i).getIp());
						
						players.add(new Player(playerPacket));		
						players.get(players.size() - 1).setIp( readPackets.get(i).getIp());
						
						// Update serverInfo for clients
						//serverInfoUpdated(cp, ServerInfoType.ExceptMe);
					}
					/*
					 * Receive a DisconnexionPacket
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(DisconnexionPacket.class))
					{
						// Remove the disconnected player
						players.remove(getPlayerFromIp(readPackets.get(i).getIp()));
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
							server.sendBuffer.sendPacket(getPlayerFromIp(receivedPacket.getIp()).getPacket());
							
							// Send to the client the last news
							server.sendBuffer.sendPacket(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName(), receivedPacket.getIp()));		
							server.sendBuffer.sendPacket(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName(), receivedPacket.getIp()));		
		
							// Send to the client his friends
							server.sendBuffer.sendPacket(new FriendsPacket(receivedPacket.getIp()));
							
							// Send to the client the ladder and his position
							server.sendBuffer.sendPacket(new LadderPacket(receivedPacket.getIp()));
							
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
						
						// Fill the packet
						receivedPacket = fillHomePacket(receivedPacket);
						
						// Send filled packet
						server.sendBuffer.sendPacket(receivedPacket);
						
					}
					if (readPackets.get(i).getClass().isAssignableFrom(MatchMakingPacket.class))
					{
						// Send this packet to the GameProcessor
						gp.mainPackets.sendPacket(readPackets.get(i));
					}
					if (readPackets.get(i).getClass().isAssignableFrom(ClientReadyPacket.class))
					{
						// Send this packet to the GameProcessor
						gp.mainPackets.sendPacket(readPackets.get(i));
					}
					else
					{
						System.err.println("Main thread : Received an unknowned packet" + readPackets.get(i).getClass());
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
					getPlayerFromIp(packet.getIp()).setName(packet.pseudo);
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
			System.out.println(players.get(i).getName());
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
	public static Player getPlayerFromIp(String ip)
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
	
	public static HomePacket fillHomePacket(HomePacket packet)
	{
		// Make tabs
		
		// TODO: Set position of the player before doing this
		packet.ladder = ladder.getLadderFromPosition(getPlayerFromIp(packet.getIp()).getPacket().pos);
		
		// TODO: Get news packets
		// TODO: Fill "news" with the good news
		packet.news = new NewsPacket[2];
		
		// TODO: Get server info packet
		// TODO: Fill packet with
		
		return packet;
	}
}
