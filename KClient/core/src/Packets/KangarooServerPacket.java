package Packets;

import java.io.Serializable;


public class KangarooServerPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3276163525805452816L;
	
	public String ip;
	public String name;
	public float x, y;
	public int health;
	public int damage;
	public int state;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [ip]: " + ip + "\n"
				+ "- [name]: " + name + "\n"
				+ "- [x]: " + x + "\n"
				+ "- [y]: " + y + "\n"
				+ "- [health]: " + health + "\n"
				+ "- [damage]: " + damage + "\n"
				+ "- [state]: " + state + "\n";
	}
}
