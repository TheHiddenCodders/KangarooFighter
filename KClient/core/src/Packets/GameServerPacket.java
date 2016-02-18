package Packets;


public class GameServerPacket extends Packets
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3276163525805452816L;
	
	public KangarooPacket player;
	public KangarooPacket opponent;
	
	public float time;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [time]: " + time + "\n";
	}
}
