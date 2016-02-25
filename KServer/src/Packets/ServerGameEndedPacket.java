package Packets;

import Kangaroo.Game;

public class ServerGameEndedPacket extends Packets
{
	private static final long serialVersionUID = -2489030316034670199L;

	public Game game;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n";
	}
}
