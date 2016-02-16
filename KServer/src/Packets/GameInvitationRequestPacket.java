package Packets;



public class GameInvitationRequestPacket extends Notification
{
	public GameInvitationRequestPacket(String ip) 
	{
		super(ip);
	}

	public GameInvitationRequestPacket() 
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
		return null;
	}
}