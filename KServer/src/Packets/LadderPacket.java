package Packets;

import java.util.ArrayList;

public class LadderPacket extends Packets
{
	private static final long serialVersionUID = 8410335275464293843L;

	public ArrayList<PlayerPacket> ladder;
	public int playerPos;
	
	public LadderPacket(String ip) 
	{
		super(ip);
		ladder = new ArrayList<PlayerPacket>();
	}

	public LadderPacket() 
	{
		super();
	}
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [ladder]:" + ladder.toString() + "\n"
				+ "- [playerPos]: " + playerPos + "\n";
	}
	
	public void addPlayer(PlayerPacket playerPacket)
	{
		ladder.add(playerPacket);
	}
}
