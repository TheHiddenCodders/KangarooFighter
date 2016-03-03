package Packets;



public class FriendsPacket extends Packets
{
	public FriendsPacket(PlayerPacket packet)
	{
		super(packet.ip);
		
		name = packet.name;
		elo = packet.elo;
		games = packet.games;
		looses = packet.looses;
		wins = packet.wins;
		pos = packet.pos;
		online = packet.online;
		inGame = packet.inGame;
	}
	
	public FriendsPacket(String ip) 
	{
		super(ip);
	}

	public FriendsPacket() 
	{
		super();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 763282346679518021L;
	
	public String name;
	public int games;
	public int wins;
	public int looses;
	public int elo;
	public int streak;
	public int pos;
	public boolean online;
	public boolean inGame;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]: " + name + "\n"
				+ "- [games]: " + games + "\n"
				+ "- [wins]: " + wins + "\n"
				+ "- [looses]: " + looses + "\n"
				+ "- [elo]: " + elo + "\n"
				+ "- [streak]: " + streak + "\n"
				+ "- [pos]: " + pos + "\n"
				+ "- [online]: " + online + "\n"
				+ "- [in game]: " + inGame + "\n";
	}
}