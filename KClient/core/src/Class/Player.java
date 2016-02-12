package Class;

import java.util.ArrayList;

import Packets.FriendsPacket;
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
				this.packet.friends.set(i, packet);
		}
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
}
