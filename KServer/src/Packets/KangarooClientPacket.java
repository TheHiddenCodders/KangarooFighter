package Packets;

import java.io.Serializable;

public class KangarooClientPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4448845967749323548L;
	
	public boolean leftArrow;
	public boolean rightArrow;
	public boolean topArrow;
	public boolean bottomArrow;
	public boolean punch;
	public boolean punchTop;
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
				+ "- [punch]: " + punch + "\n"
				+ "- [punchTop]: " + punchTop + "\n"
				+ "- [guard]: " + guard + "\n"
				+ "- [ctrlPlusD]: " + ctrlPlusD + "\n";
	}
}
