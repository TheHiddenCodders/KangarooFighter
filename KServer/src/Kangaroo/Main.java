package Kangaroo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import Packets.ClientReadyPacket;
import Packets.ConnexionPacket;
import Packets.DisconnexionPacket;
import Packets.FriendsPacket;
import Packets.HomePacket;
import Packets.LadderPacket;
import Packets.LoginPacket;
import Packets.MatchMakingPacket;
import Packets.Packets;
import Packets.SearchLadderPacket;
import Server.Server;
import ServerInfo.News;
import ServerInfo.PlayerProcessor;

public class Main 
{
	public static BufferedInputStream inputReader;
	public static String msg = "";
	
	/** gp : Manage the match making and all game instances */
	static GameProcessor gp;
	/** news : contain the server news */
	static News news;
	/** pp : an object managing all the registered players on the server*/
	static PlayerProcessor pp;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Main thread : Creation of server data");
		
		// Launch server threads
		Server server = new Server();
		server.open();
		
		news = new News("/KangarooFighters/News");

		pp = new PlayerProcessor("/KangarooFighters/Players");
		gp = new GameProcessor(pp, server.sendBuffer);
		
		// Launch game threads
		Thread t = new Thread(gp);
		t.start();
		
		// readPackets : an ArrayList containing the packet received from clients
		ArrayList<Packets> readPackets = new ArrayList<Packets>();
		
		// Read received packets by the server
		while(true)
		{
			// Read received packets
			readPackets = server.readBuffer.readPackets();
			
			for (int i = 0; i < readPackets.size(); i++)
			{
				// If the received packet is not null, analyze it.
				if (readPackets.get(i) != null)
				{
					// Receive a ConnexionPacket
					if (readPackets.get(i).getClass().isAssignableFrom(ConnexionPacket.class))
					{
						// Get the packet
						ConnexionPacket connexionPacket = (ConnexionPacket) readPackets.get(i);
						
						// Connect this client while is not login
						pp.connexion(connexionPacket);
					}
					
					// Receive a DisconnexionPacket
					else if (readPackets.get(i).getClass().isAssignableFrom(DisconnexionPacket.class))
					{
						// Get the packet
						DisconnexionPacket disconnexionPacket = (DisconnexionPacket) readPackets.get(i);
						
						// Remove the disconnected player
						pp.deconnexion(disconnexionPacket);
					}

					// Receive Login packet
					else if (readPackets.get(i).getClass().isAssignableFrom(LoginPacket.class))
					{
						// Try to login this client
						LoginPacket receivedPacket = (LoginPacket) readPackets.get(i);
						receivedPacket = pp.attemptToLogin(receivedPacket);
						
						// Send to the client (who sent the packet) the updated packet
						server.sendBuffer.sendPacket(readPackets.get(i));
						
						// If server accepted the request, send client data
						if (receivedPacket.accepted)
						{				
							// Get the player associate to this packet
							Player loginPlayer = pp.getPlayerFromIp(receivedPacket.getIp());
							
							// Send to the client his data
							server.sendBuffer.sendPacket(loginPlayer.getPacket());
							
							// Send to his connected friends he is connected
							Player player = pp.isPlayerExist(loginPlayer.getName());
							
							// Browse player's friends
							for (int j = 0; j < player.getPacket().friends.size(); j++)
							{
								// If this friend is connected
								if (player.getPacket().friends.get(j).online)
								{	
									// Get the "player" associated to this friend
									Player friendPlayer = pp.isPlayerExist(player.getPacket().friends.get(j).name);
									
									// Create a friend packet to notify the connection to a friend
									FriendsPacket friendPacket = new FriendsPacket(player.getPacket());
									friendPacket.setIp(friendPlayer.getIp());
									
									// Send this packet to the friend
									server.sendBuffer.sendPacket(friendPacket);
								}
							}
						}
					}
					
					// Receive a HomePacket
					else if (readPackets.get(i).getClass().isAssignableFrom(HomePacket.class))
					{						
						// Cast packet
						HomePacket receivedPacket = (HomePacket) readPackets.get(i);
						
						// Fill the packet
						receivedPacket = fillHomePacket(receivedPacket);
						
						// Send filled packet
						server.sendBuffer.sendPacket(receivedPacket);
						
					}
					
					// Receive a MatchMakingPacket
					else if (readPackets.get(i).getClass().isAssignableFrom(MatchMakingPacket.class))
					{
						// Send this packet to the GameProcessor
						gp.mainPackets.sendPacket(readPackets.get(i));
					}
					
					// Receive a ClientReadyPacket
					else if (readPackets.get(i).getClass().isAssignableFrom(ClientReadyPacket.class))
					{
						// Send this packet to the GameProcessor
						gp.mainPackets.sendPacket(readPackets.get(i));
					}
					
					// Receive a SearchLadderPacket
					else if (readPackets.get(i).getClass().isAssignableFrom(SearchLadderPacket.class))
					{
						SearchLadderPacket receivedPacket = (SearchLadderPacket) readPackets.get(i);
						LadderPacket packetToSend;
						
						// If the client want the Ladder of a player
						if (receivedPacket.name != null)
						{
							// Send him the ladder
							packetToSend = pp.getLadderFromPlayer(pp.isPlayerExist(receivedPacket.name));
							packetToSend.setIp(receivedPacket.getIp());
							
							server.sendBuffer.sendPacket(packetToSend);
						}
						// If the client want the Ladder of a specific position
						else
						{
							packetToSend = pp.getLadderFromPosition(receivedPacket.pos);
							packetToSend.setIp(receivedPacket.getIp());
							
							server.sendBuffer.sendPacket(packetToSend);
						}
					}
					
					// Receive an unknown packet
					else
					{
						System.err.println("Main thread : Received an unknowned packet" + readPackets.get(i).getClass());
					}
				}
				else // If the received packet is null, replace the -1.
				{
					System.out.println("A null packet was received");
				}
			}
		}
	}
	
	public static HomePacket fillHomePacket(HomePacket packet)
	{
		// Get the ladder with the player sending the packet
		packet.ladder = pp.getLadderFromPlayer(pp.getPlayerFromIp(packet.getIp()));
		
		// Get the 2 last news of the server
		packet.news = news.getLastNews(2, packet.getIp());
		
		// TODO: Get server info packet
		
		return packet;
	}
}
