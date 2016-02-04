package Packets;

import java.io.Serializable;

public class GameClientPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4448845967749323548L;
	
	public boolean leftArrow;
	public boolean rightArrow;
	public boolean topArrow;
	public boolean bottomArrow;
	public boolean leftPunch;
	public boolean rightPunch;
	public boolean guard;
	
	public boolean ctrlPlusD;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [leftArrow]: " + leftArrow + "\n"
				+ "- [rightArrow]: " + rightArrow + "\n"
				+ "- [topArrow]: " + topArrow + "\n"
				+ "- [bottomArrow]: " + bottomArrow + "\n"
				+ "- [leftPunch]: " + leftPunch + "\n"
				+ "- [rightPunch]: " + rightPunch + "\n"
				+ "- [guard]: " + guard + "\n"
				+ "- [ctrlPlusD]: " + ctrlPlusD + "\n";
	}
}
