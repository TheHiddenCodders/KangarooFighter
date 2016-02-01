package Kangaroo;

import Packets.MatchMakingPacket;
import Server.BufferPacket;
import Utils.Timer;

public class WaitingPlayer implements Runnable
{
	/** timer : allow counting time in this thread*/
	private Timer time;
	/** mmPacket : the packet representing the player in the match making */
	private MatchMakingPacket mmPacket;
	/** sendPacket : a reference to the GameProcessor BufferPacket to send it the new mmPcket */
	private BufferPacket gameProcessorBuffer;
	
	public WaitingPlayer(MatchMakingPacket mmPacket, BufferPacket sendPacket)
	{
		// Make a copy of the match making packet
		this.mmPacket = mmPacket;
		this.gameProcessorBuffer = sendPacket;
	}
	
	@Override
	public void run() 
	{
		time = new Timer();
		time.restart();
		
		// The player wait for 15 seconds
		while (time.getElapsedTime() < 0.15);
		
		// Compute the new tolerance
		if (mmPacket.eloTolerance < 2.f)
			mmPacket.eloTolerance += 0.5f;
		
		// Send it to the GameProcessor
		gameProcessorBuffer.sendPacket(mmPacket);
	}
}
