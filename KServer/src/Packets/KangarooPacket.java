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
	
	public KangarooPacket(KangarooPacket player) 
	{
		this.x = player.x;
		this.y = player.y;
		this.health = player.health;
		this.damage = player.damage;
		this.state = player.state;
		this.flip = player.flip;
		this.speed = player.speed;
	}

	public KangarooPacket() 
	{
	
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
				+ "- [flip]: " + flip + "\n";
	}
}
