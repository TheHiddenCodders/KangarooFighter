package Packets;



public class HomePacket extends Packets
{
	private static final long serialVersionUID = 4367432052684494554L;

	public NewsPacket[] news;
	public LadderPacket ladder;
	public ServerInfoPacket serverInfos;
	
	public HomePacket(String ip) 
	{
		super(ip);
		ladder = new LadderPacket();
	}

	public HomePacket() 
	{
		super();
		ladder = new LadderPacket();
	}
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [ladderPlayers]: " + ladder.toString()
				+ "\n";
	}
}
