package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientReadyPacket implements Serializable
{
	public String ip;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [IP]: " + ip + "\n";
				
	}	
}
