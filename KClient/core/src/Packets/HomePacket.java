package Packets;


public class HomePacket extends Packets
{
	private static final long serialVersionUID = 4367432052684494554L;

	public NewsPacket[] news;
	public PlayerPacket[] ladderPlayers;
	public ServerInfoPacket serverInfos;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n";
	}
}
