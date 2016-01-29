package Packets;

import java.io.Serializable;

public class NewsPacket extends Packets
{
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
