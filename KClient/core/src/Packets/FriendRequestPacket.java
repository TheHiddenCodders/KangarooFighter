package Packets;

import java.io.Serializable;

public class FriendRequestPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2490418226242388006L;
	
	public String name;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]:" + name + "\n";
	}
}
