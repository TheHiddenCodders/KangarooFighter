package Packets;

public class RoundResultPacket extends Packets
{
	private static final long serialVersionUID = -5449562136787855633L;
	
	public KangarooPacket player, opponent;
	public String winnerName;
	
	public RoundResultPacket(RoundResultPacket roundResultPacket) 
	{
		super(roundResultPacket.getIp());
		this.player = new KangarooPacket(roundResultPacket.player);
		this.opponent = new KangarooPacket(roundResultPacket.opponent);
	}

	public RoundResultPacket() 
	{
		super();
	}

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n";
	}
}
