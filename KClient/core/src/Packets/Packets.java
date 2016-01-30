package Packets;

import java.io.Serializable;

public class Packets implements Serializable
{
	private static final long serialVersionUID = 254263979698874592L;
	
	public String ip;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [ip]: " + ip + "\n";
	}
}
