package Packets;


public class ClientReadyPacket extends Packets
{
	private static final long serialVersionUID = -6842609063504303426L;

	public ClientReadyPacket() 
	{
		super();
	}
	
	public ClientReadyPacket(String ip)
	{
		super(ip);
	}
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n";		
	}	
}
