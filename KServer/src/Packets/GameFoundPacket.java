package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GameFoundPacket implements Serializable
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
