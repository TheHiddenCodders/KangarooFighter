package Packets;

import java.util.ArrayList;

public class FriendsPacket extends Packets
{
	public FriendsPacket(String ip) 
	{
		super(ip);
		friendsName = new ArrayList<String>();
		friendsOnline = new ArrayList<Boolean>();
	}

	public FriendsPacket() 
	{
		super();
		friendsName = new ArrayList<String>();
		friendsOnline = new ArrayList<Boolean>();
	}

	private static final long serialVersionUID = 8410335275464293843L;

	public ArrayList<String> friendsName;
	public ArrayList<Boolean> friendsOnline;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [friendsName]:" + friendsName.toString() + "\n"
				+ "- [friendsOnline]:" + friendsOnline.toString() + "\n";
	}
	
	public void addFriend(String name, boolean online)
	{
		friendsName.add(name);
		friendsOnline.add(online);
	}
}
