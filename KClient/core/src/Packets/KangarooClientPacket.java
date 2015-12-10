package Packets;

import java.io.Serializable;

public class KangarooClientPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4448845967749323548L;
	
	public boolean leftArrowKey;
	public boolean rightArrowKey;
	public boolean leftPunchKey;
	public boolean rightPunchKey;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [leftArrowKey]: " + leftArrowKey + "\n"
				+ "- [rightArrowKey]: " + rightArrowKey + "\n"
				+ "- [leftPunchKey]: " + leftPunchKey + "\n"
				+ "- [rightPunchKey]: " + rightPunchKey + "\n";
	}
}
