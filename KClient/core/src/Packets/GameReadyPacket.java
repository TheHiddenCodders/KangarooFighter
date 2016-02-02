package Packets;

import java.io.Serializable;

public class GameReadyPacket extends Packets
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1988963014347509043L;

	public GameReadyPacket() 
	{
		super();
	}
	
	public GameReadyPacket(String ip)
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
