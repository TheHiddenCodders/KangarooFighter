package Packets;



public class GameInvitationRequestAnswerPacket extends Notification
{
	public GameInvitationRequestAnswerPacket(String ip) 
	{
		super(ip);
	}

	public GameInvitationRequestAnswerPacket() 
	{
		super();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 455349863221403030L;
	
	public String name;
	public boolean answer;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]: " + name + "\n"
				+ "- [answer]: " + answer + "\n";
	}

	@Override
	public String save() 
	{
		String result = new String();
		result = result.concat("GameInvitationRequestPacketAnswer-");
		result = result.concat(message + "-");
		result = result.concat(date + "-");
		result = result.concat(name + "-");
		result = result.concat(answer + "-");
		
		return result;
	}
}