package Packets;

import java.util.ArrayList;


public class PlayerPacket extends Packets
{
	public PlayerPacket(String ip) 
	{
		super(ip);
		
		friends = new ArrayList<FriendsPacket>();
	}

	public PlayerPacket() 
	{
		super();
		
		friends = new ArrayList<FriendsPacket>();
	}

	private static final long serialVersionUID = -1319262982236758754L;
	
	public String name;
	public int games;
	public int wins;
	public int looses;
	public int elo;
	public int streak;
	public int pos;
	public ArrayList<FriendsPacket> friends;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]: " + name + "\n"
				+ "- [games]: " + games + "\n"
				+ "- [wins]: " + wins + "\n"
				+ "- [looses]: " + looses + "\n"
				+ "- [elo]: " + elo + "\n"
				+ "- [streak]: " + streak + "\n"
				+ "- [pos]: " + pos + "\n"
				+ "- [friends]: " + friends.toString() + "\n";
	}
	
	public void addFriend(String name, boolean online)
	{
		friends.add(new FriendsPacket(name, online));
	}
}
