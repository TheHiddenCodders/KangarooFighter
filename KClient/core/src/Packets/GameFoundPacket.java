package Packets;

import java.io.Serializable;

public class GameFoundPacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2833436810355604659L;

	public String mapPath;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n" 	
				+ "- [mapPath]:" + mapPath + "\n";
	}
}
