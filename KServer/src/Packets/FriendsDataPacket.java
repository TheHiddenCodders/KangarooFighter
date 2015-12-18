package Packets;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendsDataPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8410335275464293843L;

	public ArrayList<String> friendsName;
	public ArrayList<Boolean> friendsOnline;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [friendsName]:" + friendsName.toString() + "\n"
				+ "- [friendsOnline]:" + friendsOnline.toString() + "\n";
	}
}
