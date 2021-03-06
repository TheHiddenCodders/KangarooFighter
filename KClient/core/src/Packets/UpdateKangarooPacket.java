package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateKangarooPacket implements Serializable
{
	public String ip;
	public String name;
	public float x, y;
	public int health;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [IP]: " + ip + "\n"
				+ "- [name]: " + name + "\n"
				+ "- [x]: " + x + "\n"
				+ "- [y]: " + y + "\n"
				+ "- [health]: " + health + "\n";
	}
}
