package Packets;

import java.util.ArrayList;

public class FriendsDataPacket extends Packets
{
	public FriendsDataPacket(String ip) 
	{
		super(ip);
	}

	public FriendsDataPacket() 
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
}
