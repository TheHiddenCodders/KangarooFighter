package Packets;

import java.io.Serializable;
import java.util.ArrayList;

public class LadderDataPacket extends Packets
{
	private static final long serialVersionUID = 8410335275464293843L;

	public ArrayList<String> ladder;
	public int playerPos;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [ladder]:" + ladder.toString() + "\n"
				+ "- [playerPos]: " + playerPos + "\n";
	}
}
