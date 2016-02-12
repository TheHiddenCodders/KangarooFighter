package Packets;



public class FriendRequestPacket extends Packets
{
	public FriendRequestPacket(String ip) 
	{
		super(ip);
	}

	public FriendRequestPacket() 
	{
		super();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 455349863221403030L;
	
	public String name;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]: " + name + "\n";
	}
}