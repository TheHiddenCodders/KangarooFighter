package Packets;

import java.io.Serializable;

public class ClientDisconnectionPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6007358750306979800L;
	
	public String disconnectedClientIp;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [IP]: " + disconnectedClientIp + "\n";
				
	}
}
