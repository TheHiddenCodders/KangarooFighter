package Kangaroo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Packets.ClientReadyPacket;
import Packets.ConnexionPacket;
import Packets.DisconnexionPacket;
import Packets.FriendAnswerPacket;
import Packets.FriendRequestPacket;
import Packets.FriendsPacket;
import Packets.GameClientPacket;
import Packets.HomePacket;
import Packets.LadderPacket;
import Packets.LoginPacket;
import Packets.MatchMakingPacket;
import Packets.Notification;
import Packets.Packets;
import Packets.SearchLadderPacket;
import Server.Server;
import ServerInfo.News;
import ServerInfo.PlayerProcessor;
import ServerInfo.ServerInfo;

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
	/* serverInfo : containing all server infos, send a ServerInfoPacket to clients when updated */
	static ServerInfo serverInfo;
	/** */
	static Server server;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Main thread : Creation of server data");
		
		// Create and launch server threads
		server = new Server();
		server.open();
		
		// Create the class that update servers infos
		serverInfo = new ServerInfo(server.sendBuffer);
		
		// Load server data from files
		pp = new PlayerProcessor("/KangarooFighters/Players", serverInfo);
		gp = new GameProcessor(pp, server.sendBuffer); // TODO : GameProcessor need a reference to serverInfo
		news = new News("/KangarooFighters/News");
		
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
						
						System.out.println("Main Thread : A new client join the server");
					}
					
					// Receive a DisconnexionPacket
					else if (readPackets.get(i).getClass().isAssignableFrom(DisconnexionPacket.class))
					{
						// Get the packet
						DisconnexionPacket disconnexionPacket = (DisconnexionPacket) readPackets.get(i);
						
						System.out.println("Main Thread : " + pp.getPlayerFromIp(disconnexionPacket.getIp()).getName() + " quit the server");
						
						// Send the packet to the GameProcessor in case the player is in match making or in a game
						gp.mainPackets.sendPacket(disconnexionPacket);
						
						// Remove the disconnected player
						pp.deconnexion(disconnexionPacket);
						//server.removeClient(disconnexionPacket);
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
							
							System.out.println("Main Thread : " + loginPlayer.getName() + " login");
							
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
					
					// Received a Notification
					else if (readPackets.get(i) instanceof Notification)
					{
						notificationReceiver((Notification) readPackets.get(i)); 
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
						
						System.out.println("Main Thread : " + pp.getPlayerFromIp(readPackets.get(i).getIp()).getName() + " is ready to start a game");
					}
					
					// Receive a GameClientPacket
					else if (readPackets.get(i).getClass().isAssignableFrom(GameClientPacket.class))
					{
						// Send this packet to the GameProcessor
						gp.mainPackets.sendPacket(readPackets.get(i));
					}
					
					// Receive an unknown packet
					else
					{
						System.err.println("Main thread : Receive an unknowned packet : " + readPackets.get(i).getClass());
					}
				}
				else // If the received packet is null.
				{
					System.out.println("Main thread : Receive a null packet");
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
		
		// TODO: Get server info packet, may be usless with ServerInfo class
		
		return packet;
	}
	
	/** Treat the receiving of a notification
	 * @param notification : the notification received
	 */
	public static void notificationReceiver(Notification notification)
	{
		if (notification instanceof FriendRequestPacket)
		{
			// Cast notification
			FriendRequestPacket packet = (FriendRequestPacket)notification;
			
			// Get a reference of the sender
			Player sender = pp.getPlayerFromIp(packet.getIp());
			
			// TODO : Check in sender notification if it have a demand from packet.name, in this case make them friend
			// TODO : check if players are already friends
			
			// Get the today date 
			Date today = Calendar.getInstance().getTime();   
			Format formatter = new SimpleDateFormat("dd/MM HH:mm");
			
			// Prepare the packet
			packet.senderName = sender.getName();
			packet.date = formatter.format(today);
			packet.message = new String("Demande d'ajout d'amis de <c0>" + sender.getName() + "</>");
			
			// Get the player who will store the notification
			Player player = pp.isPlayerExist(packet.receiverName);
			
			// Check if the player associate to the packet's name exist
			if (player != null)
			{
				// Store the notification
				packet.setIp(player.getIp());
				player.getPacket().addNotification(packet);
				
				// If the player is connected, then send him
				if (pp.isPlayerConnected(player.getName()) != null)
					server.sendBuffer.sendPacket(packet);
				
				// Send a notification to sender
				Notification succeedNotif = new Notification(sender.getIp());
				
				succeedNotif.date = formatter.format(today);
				succeedNotif.message = "Une demande d'ami a ete envoye a <c0>" + packet.receiverName + "</>";
				
				sender.getPacket().addNotification(succeedNotif);
				server.sendBuffer.sendPacket(succeedNotif);
			}
			else
			{
				// Send a notification to sender
				Notification failNotif = new Notification(sender.getIp());
				
				failNotif.date = formatter.format(today);
				failNotif.message = "Le joueur <c0>" + packet.receiverName + "</> n'existe pas";
				
				sender.getPacket().addNotification(failNotif);
				server.sendBuffer.sendPacket(failNotif);
			}
		}
		
		else if (notification instanceof FriendAnswerPacket)
		{
			// Cast notification
			FriendAnswerPacket packet = (FriendAnswerPacket)notification;
			
			// Get a reference of the sender from the global list of players
			Player sender = pp.getPlayerFromIp(packet.getIp());
			sender = pp.isPlayerExist(sender.getName());
			
			// get the future friend
			Player friend = pp.isPlayerExist(packet.name);
			
			if (friend != null)
			{
				// Get the today date
				Date today = Calendar.getInstance().getTime();   
				Format formatter = new SimpleDateFormat("dd/MM HH:mm");
				
				// Create a notification for both players
				Notification answerNotif = new Notification(friend.getIp());
				Notification senderNotif = new Notification(sender.getIp());
				
				// Check if the answer is positive
				if (packet.answer)
				{
					// Fill the answer notification
					answerNotif.message = "<c0>" + sender.getName() + "</> a accepte votre demande d'amis";
					answerNotif.date = formatter.format(today);
					senderNotif.message = "Vous etes maintenant ami avec <c0>" + friend.getName() + "</>";
					senderNotif.date = formatter.format(today);
					
					// Create new FriendsPacket
					FriendsPacket senderFriend = new FriendsPacket(friend.getPacket());
					FriendsPacket playerFriend = new FriendsPacket(sender.getPacket());
					
					// Add friend to both players
					sender.getPacket().addFriend(senderFriend);
					friend.getPacket().addFriend(playerFriend);
					
					// Store the notification
					friend.getPacket().addNotification(answerNotif);
					sender.getPacket().addNotification(senderNotif);
					
					// Send to sender his new friend and his notification
					senderFriend.setIp(sender.getIp());
					server.sendBuffer.sendPacket(senderFriend);
					server.sendBuffer.sendPacket(senderNotif);
					
					// Check if the friend is connected
					if (pp.isPlayerConnected(friend.getName()) != null)
					{
						// Send the notification
						server.sendBuffer.sendPacket(answerNotif);
						
						// Send the friend packet
						playerFriend.setIp(friend.getIp());
						server.sendBuffer.sendPacket(playerFriend);
					}
				}
				
				// If the answer is negative
				else
				{
					// Prepare the negative notification
					answerNotif.message = "<c0>" + sender.getName() + "</> a refuse votre demande d'amis";
					answerNotif.date = formatter.format(today);
					
					// Store it 
					friend.getPacket().addNotification(answerNotif);
					
					// If the friend is connected, then send him the notification
					if (pp.isPlayerConnected(friend.getName()) != null)
						server.sendBuffer.sendPacket(answerNotif);
				}
			
				// Browse sender notification
				for (int i = 0; i < sender.getPacket().notifications.size(); i++)
				{
					// try to find the friend request packet using to send this FriendAnswerPacket
					if (sender.getPacket().notifications.get(i) instanceof FriendRequestPacket)
					{
						FriendRequestPacket oldRequest = (FriendRequestPacket) sender.getPacket().notifications.get(i);
						
						// If the notification is found
						if (oldRequest.receiverName.equals(sender.getName()) && oldRequest.senderName.equals(friend.getName()))
						{
							// Delete it
							sender.getPacket().notifications.remove(sender.getPacket().notifications.get(i));
						}
					}
				}
			}
			else
			{
				// Error : the future friend doesn't exist
				System.out.println("Main Thread : receive a FriendAnswerPacket with an unknown player");
			}
		}
		
		// Receive a simple notification
		else 
		{
			// Get the sender 
			Player sender = pp.getPlayerFromIp(notification.getIp());
			
			// Browse sender notification
			for (int i = 0; i < sender.getPacket().notifications.size(); i++)
			{
				// If the message are the same, then the notification to delete is found
				if (sender.getPacket().notifications.get(i).message.equals(notification.message))
				{
					// Delete the notification
					sender.getPacket().notifications.remove(sender.getPacket().notifications.get(i));
				}
			}
		}
	}
}
