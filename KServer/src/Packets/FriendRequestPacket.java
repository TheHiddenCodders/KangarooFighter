package Packets;



public class FriendRequestPacket extends Notification
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

	@Override
	public String save() 
	{
		String result = new String();
		result = result.concat("FriendRequestPacket|");
		result = result.concat(message + "|");
		result = result.concat(date.toString() + "|");
		result = result.concat(name + "|");
		
		return result;
	}
}