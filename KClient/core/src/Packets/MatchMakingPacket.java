package Packets;

import java.io.Serializable;

public class MatchMakingPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4023881332979491078L;
	
	public boolean search; // This packet can also get off client from matchmaking
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [search]: " + search + "\n";	
	}
}
