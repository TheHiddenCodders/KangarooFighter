package Packets;

import java.util.ArrayList;

public class LadderPacket extends Packets
{
	private static final long serialVersionUID = 8410335275464293843L;

	public ArrayList<PlayerPacket> ladderList;
	public int playerPos;
	
	public LadderPacket(String ip) 
	{
		super(ip);
		ladderList = new ArrayList<PlayerPacket>();
	}

	public LadderPacket() 
	{
		super();
		ladderList = new ArrayList<PlayerPacket>();
	}
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [ladder]:" + ladderList.toString() + "\n"
				+ "- [playerPos]: " + playerPos + "\n";
	}
	
	public void addPlayer(PlayerPacket playerPacket)
	{
		ladderList.add(playerPacket);
	}
}
