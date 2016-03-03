package Packets;

public class RoundResultPacket extends Packets
{
	private static final long serialVersionUID = -5449562136787855633L;
	
	public KangarooPacket winner, loser;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n";
	}
}
