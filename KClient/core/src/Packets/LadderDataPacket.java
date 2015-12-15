package Packets;

import java.io.Serializable;
import java.util.ArrayList;

public class LadderDataPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8410335275464293843L;

	public ArrayList<String> ladder;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ ladder.toString() + "\n";
	}
}
