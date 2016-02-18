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
