package Packets;

public class KangarooPacket extends Packets
{
	private static final long serialVersionUID = 3333230021300979600L;

	public float x, y;
	public int health;
	public int damage;
	public int state;
	public boolean flip;
	public float speed;
	public int hits;
	
	public KangarooPacket(KangarooPacket player) 
	{
		super(player.getIp());
		this.x = player.x;
		this.y = player.y;
		this.health = player.health;
		this.damage = player.damage;
		this.state = player.state;
		this.flip = player.flip;
		this.speed = player.speed;
		this.hits = player.hits;
	}

	public KangarooPacket() 
	{
		super();
	}

	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [ip]: " + ip + "\n"
				+ "- [x]: " + x + "\n"
				+ "- [y]: " + y + "\n"
				+ "- [health]: " + health + "\n"
				+ "- [damage]: " + damage + "\n"
				+ "- [state]: " + state + "\n"
				+ "- [flip]: " + flip + "\n"
				+ "- [hits]: " + hits + "\n";
	}
}
