package Packets;

import Server.ClientProcessor;


public class DisconnectionPacket extends Packets 
{
	private static final long serialVersionUID = -4499570665948541789L;

	public DisconnectionPacket(ClientProcessor cp) 
	{
		this.ip = cp.getIp();
	}

	@Override
	public String toString()
	{
		return super.toString();
	}
}
