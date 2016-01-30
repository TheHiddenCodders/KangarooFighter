package Packets;


public class NewsPacket extends Packets
{
	public NewsPacket(String ip) 
	{
		super(ip);
	}

	public NewsPacket() 
	{
		super();
	}

	private static final long serialVersionUID = 6971145096053758406L;
	
	public String name;
	public byte[] banner, news;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [name]: " + name + "\n";
	}
}
