package Packets;


public class GameServerPacket extends Packets
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3276163525805452816L;
	
	public KangarooPacket player;
	public KangarooPacket opponent;
	
	public int round;
	public float time;

	public GameServerPacket() 
	{
		this.player = new KangarooPacket();
		this.opponent = new KangarooPacket();
		
		this.time = 0;
	}
	
	public GameServerPacket(GameServerPacket serverPacket) 
	{
		super(serverPacket.getIp());
		
		this.player = new KangarooPacket(serverPacket.player);
		this.opponent = new KangarooPacket(serverPacket.opponent);
		
		this.time = serverPacket.time;
	}

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [time]: " + time + "\n";
	}
}
