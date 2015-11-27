package Packets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class KangarooClientPacket implements Serializable
{
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
