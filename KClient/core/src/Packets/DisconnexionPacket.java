package Packets;


public class DisconnexionPacket extends Packets 
{
	private static final long serialVersionUID = -4499570665948541789L;
	
	public DisconnexionPacket(String ip)
	{
		super(ip);
	}

	public DisconnexionPacket() 
	{
		super();
	}

	@Override
	public String toString()
	{
		return super.toString();
	}
}
