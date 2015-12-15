package Packets;

import java.io.Serializable;

public class ClientDataPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1319262982236758754L;
	
	public String name;
	public int games;
	public int wins;
	public int looses;
	public int elo;
	public int streak;

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
				+ "- [streak]: " + streak + "\n";
	}
}
