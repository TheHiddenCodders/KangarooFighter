package Packets;



public class DeleteFriendPacket extends Packets
{
	
	public DeleteFriendPacket(String ip) 
	{
		super(ip);
	}

	public DeleteFriendPacket() 
	{
		super();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 763282346679518021L;
	
	public FriendsPacket friend;

	@Override
	public String toString()
	{
		return super.toString();
	}
}