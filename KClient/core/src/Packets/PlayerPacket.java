package Packets;


public class PlayerPacket extends Packets
{
	public PlayerPacket(String ip) 
	{
		super(ip);
	}

	public PlayerPacket() 
	{
		super();
	}

	private static final long serialVersionUID = -1319262982236758754L;
	
	public String name;
	public int games;
	public int wins;
	public int looses;
	public int elo;
	public int streak;
	public int pos;
	public PlayerPacket[] friends;

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
				+ "- [friends]: " + friends.toString() + "\n";
	}
}
