package Packets;

public class ServerInfoPacket extends Packets
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3840328519507893947L;
	
	public int nKangaroosRegistered;
	public int nGamesPlayed;
	public int nKangaroosOnline;
	public int nGamesOnline;
	
	public ServerInfoPacket(ServerInfoPacket packet) 
	{
		nKangaroosRegistered = packet.nKangaroosRegistered;
		nGamesPlayed = packet.nGamesPlayed;
		nKangaroosOnline = packet.nKangaroosOnline;
		nGamesOnline = packet.nGamesOnline;
	}

	public ServerInfoPacket() 
	{
		nKangaroosRegistered = 0;
		nGamesPlayed = 0;
		nKangaroosOnline = 0;
		nGamesOnline = 0;
	}

	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [nKangaroosRegistered]: " + nKangaroosRegistered + "\n"
				+ "- [nGamesPlayed]: " + nGamesPlayed + "\n"
				+ "- [nKangaroosOnline]: " + nKangaroosOnline + "\n"
				+ "- [nGamesOnline]: " + nGamesOnline + "\n";
				
	}
}
