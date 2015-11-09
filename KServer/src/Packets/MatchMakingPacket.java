package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MatchMakingPacket implements Serializable
{
	public String ip;
	public boolean search; // This packet can also get off client from matchmaking
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [IP]: " + ip + "\n"
				+ "- [search]: " + search + "\n";
				
	}
}
