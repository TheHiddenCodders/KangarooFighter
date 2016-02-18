package Packets;


public class GameServerPacket extends Packets
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3276163525805452816L;
	
	public String ip;
	public float x, y;
	public int health;
	public int damage;
	public int state;
	public boolean flip;

	@Override
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
