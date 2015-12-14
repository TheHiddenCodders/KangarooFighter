package Packets;

import java.io.Serializable;

public class EndGamePacket implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7885061444818670474L;
	
	public int endGameType;
	public String looserAddress;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [endType]: " + endGameType + "\n"
				+ "- [looser]: " + looserAddress + "\n";
	}
}
