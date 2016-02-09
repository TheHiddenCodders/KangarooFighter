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
import Packets.NewsPacket;
import Packets.Packets;
import Server.Server;
import ServerInfo.Ladder;
import ServerInfo.News;
import ServerInfo.PlayerProcessor;

public class Main 
{
	public static BufferedInputStream inputReader;
	public static String msg = "";
	
	static GameProcessor gp;
	/** ladder : contain the game ladder */
	static Ladder ladder;
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
		
		ladder = new Ladder("/KangarooFighters/Ladder/elo");
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
			readPackets = server.readBuffer.readPackets();
			
			for (int i = 0; i < readPackets.size(); i++)
			{
				// If the received packet is not null, analyze it.
				if (readPackets.get(i) != null)
				{
					/*
					 * Receive a ConnexionPacket
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(ConnexionPacket.class))
					{
						// Get the packet
						ConnexionPacket connexionPacket = (ConnexionPacket) readPackets.get(i);
						
						// Connect this client while is not login
						pp.connexion(connexionPacket);
					}
					/*
					 * Receive a DisconnexionPacket
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(DisconnexionPacket.class))
					{
						// Get the packet
						DisconnexionPacket disconnexionPacket = (DisconnexionPacket) readPackets.get(i);
						
						// Remove the disconnected player
						pp.deconnexion(disconnexionPacket);
					}
					/*
					 * Receive Login packet 
					 */
					if (readPackets.get(i).getClass().isAssignableFrom(LoginPacket.class))
					{
						// Try to login this client
						LoginPacket receivedPacket = (LoginPacket) readPackets.get(i);
						receivedPacket = pp.attemptToLogin(receivedPacket);
						
						// Send to the client (who sent the packet) the updated packet
						server.sendBuffer.sendPacket(readPackets.get(i));
						
						// If server accepted the request, send client data
						if (receivedPacket.accepted)
						{				
							// Send to the client his data
							server.sendBuffer.sendPacket(pp.getPlayerFromIp(receivedPacket.getIp()).getPacket());
							
							// Send to the client the last news
							NewsPacket[] newsBuffer = news.getLastNews(2, receivedPacket.getIp());
							server.sendBuffer.sendPacket(newsBuffer[0]);		
							server.sendBuffer.sendPacket(newsBuffer[1]);		
		
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
	
	public static HomePacket fillHomePacket(HomePacket packet)
	{
		// Make tabs
		
		// TODO: Set position of the player before doing this
		packet.ladder = ladder.getLadderFromPosition(pp.getPlayerFromIp(packet.getIp()).getPacket().pos);
		
		// Get the 2 last news of the server
		packet.news = news.getLastNews(2, packet.getIp());
		
		// TODO: Get server info packet
		// TODO: Fill packet with
		
		return packet;
	}
}
