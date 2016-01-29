package Packets;

import Server.ClientProcessor;

public class ConnectionPacket extends Packets 
{
	private static final long serialVersionUID = 7843668593522499265L;

	public ConnectionPacket(ClientProcessor cp) 
	{
		this.ip = cp.getIp();
	}

	@Override
	public String toString()
	{
		return super.toString();
	}
}
