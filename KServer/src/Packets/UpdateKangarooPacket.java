package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateKangarooPacket implements Serializable
{
	public String ip;
	public String name;
	public float x, y;
	public int health;
	public int damage;
	public boolean punch;
	public boolean guard;

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
				+ "- [punch]: " + punch + "\n"
				+ "- [guard]: " + guard + "\n";
	}
}
