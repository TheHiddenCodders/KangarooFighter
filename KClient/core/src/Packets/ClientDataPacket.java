package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientDataPacket implements Serializable
{
	public String name;
	public int games;
	public int wins;
	public int looses;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]: " + name + "\n"
				+ "- [games]: " + games + "\n"
				+ "- [wins]: " + wins + "\n"
				+ "- [looses]: " + looses + "\n";
	}
}
