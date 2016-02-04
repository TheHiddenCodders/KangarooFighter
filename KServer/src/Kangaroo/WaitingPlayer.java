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
	/**  */
	private volatile boolean quit;
	
	public WaitingPlayer(MatchMakingPacket mmPacket, BufferPacket sendPacket)
	{
		// Make a copy of the match making packet
		this.mmPacket = mmPacket;
		this.gameProcessorBuffer = sendPacket;
		this.quit = false;
	}
	
	@Override
	synchronized public void run() 
	{
		time = new Timer();
		time.restart();
		
		// The player wait for 15 seconds
		while (time.getElapsedTime() < 0.15 && !quit)
		{
			//System.out.println(mmPacket.getIp() + " " + time.getElapsedTime());
		}
		
		// Compute the new tolerance
		if (mmPacket.eloTolerance < 2.f)
			mmPacket.eloTolerance += 0.5f;
		
		if (!quit)
		{
			// Send it to the GameProcessor
			gameProcessorBuffer.sendPacket(mmPacket);
			System.out.println(mmPacket.getIp() + " relaunch match making " + quit);
		}
		else 
		{
			System.out.println(mmPacket.getIp() + " quit match making");
		}
	}
	
	synchronized public void quit()
	{
		this.quit = true;
	}
}
