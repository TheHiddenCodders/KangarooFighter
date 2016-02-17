package Class;

import java.util.ArrayList;

import Packets.FriendsPacket;
import Packets.Notification;
import Packets.PlayerPacket;

public class Player 
{
	/*
	 * Attributes
	 */
	
	private PlayerPacket packet;
	
	/*
	 * Constructors
	 */
	
	public Player()
	{
		
	}
	
	public Player(PlayerPacket packet)
	{
		this.packet = packet;
		
		/* Temp
		FriendRequestPacket test0 = new FriendRequestPacket();
		test0.name = "Kurond";
		GameInvitationRequestPacket test1 = new GameInvitationRequestPacket();
		test1.name = "Kurond";
		
		packet.notifications.add(test0);
		packet.notifications.add(test1);*/
	}
	
	/*
	 * Methods
	 */
	
	public void update(PlayerPacket packet)
	{
		this.packet = packet;
	}
	
	public void updateFriend(FriendsPacket packet)
	{
		// Find index of this friend
		for (int i = 0; i < this.packet.friends.size(); i++)
		{
			// Replace old element by new one
			if (this.packet.friends.get(i).name.equals(packet.name))
			{
				this.packet.friends.set(i, packet);
				return;
			}	
		}
		
		// If the friend didn't exists for the moment, add him
		this.packet.friends.add(packet);		
	}
	
	public void updateNotification(Notification packet)
	{
		// Try to find the notification
		for (int i = 0; i < this.packet.notifications.size(); i++)
		{
			// Replace old element by new one
			if (this.packet.notifications.get(i).message.equals(packet.message))
			{
				this.packet.friends.remove(i);
				return;
			}	
		}
		
		// If the notifications didn't exists add her
		this.packet.notifications.add(packet);
	}

	public String getName() {
		return packet.name;
	}

	public int getElo() {
		return packet.elo;
	}

	public int getGames() {
		return packet.games;
	}

	public int getLoses() {
		return packet.looses;
	}

	public int getWins() {
		return packet.wins;
	}

	public int getPos() {
		return packet.pos;
	}

	public boolean isOnline() {
		return packet.online;
	}

	public ArrayList<FriendsPacket> getFriends() {
		return packet.friends;
	}
	
	public ArrayList<Notification> getNotifications() {
		return packet.notifications;
	}
}
