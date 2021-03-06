package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ServerInfoPacket implements Serializable
{
	public String ip;
	public int nKangaroosRegistered;
	public int nGamesPlayed;
	public int nKangaroosOnline;
	public int nGamesOnline;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [IP]: " + ip + "\n"
				+ "- [nKangaroosRegistered]: " + nKangaroosRegistered + "\n"
				+ "- [nGamesPlayed]: " + nGamesPlayed + "\n"
				+ "- [nKangaroosOnline]: " + nKangaroosOnline + "\n"
				+ "- [nGamesOnline]: " + nGamesOnline + "\n";
				
	}
}
