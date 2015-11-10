package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HeartBeatPacket implements Serializable
{
	public String ip;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "[ip]: " + ip + "\n";
	}
}
