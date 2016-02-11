package Packets;


public class FriendsPacket extends Packets
{
	public FriendsPacket(String ip) 
	{
		super(ip);
		friendsName = new String();
	}

	public FriendsPacket() 
	{
		super();
		friendsName = new String();
	}
	
	public FriendsPacket(String name, boolean online)
	{
		super();
		friendsName = name;
		friendsOnline = online;
	}

	private static final long serialVersionUID = 8410335275464293843L;

	public String friendsName;
	public boolean friendsOnline;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [friendsName]:" + friendsName.toString() + "\n"
				+ "- [friendsOnline]:" + friendsOnline + "\n";
	}
}
