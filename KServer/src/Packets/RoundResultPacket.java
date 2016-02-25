package Packets;

public class RoundResultPacket extends Packets
{
	private static final long serialVersionUID = -5449562136787855633L;
	
	public KangarooPacket winner, loser;
	
	public RoundResultPacket(RoundResultPacket roundResultPacket) 
	{
		super(roundResultPacket.getIp());
		this.winner = new KangarooPacket(roundResultPacket.winner);
		this.loser = new KangarooPacket(roundResultPacket.loser);
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
