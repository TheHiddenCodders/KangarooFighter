package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientDisconnectionPacket implements Serializable
{
	public String disconnectedClientIp;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [IP]: " + disconnectedClientIp + "\n";
				
	}
}
