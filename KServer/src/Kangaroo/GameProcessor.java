package Kangaroo;

import java.util.ArrayList;

import Packets.GamePacket;
import Packets.MatchMakingPacket;
import Packets.Packets;
import Server.BufferPacket;

public class GameProcessor implements Runnable
{
	/** games : a list containing all games */
	private ArrayList<Game> games;
	
	/** waitingPlayers : a list of reference on player waiting for play */
	private ArrayList<Player> waitingPlayers;
	/** waitingThread : same size as waitingPlayers, allow the match making to stop thread if a player was found */
	private ArrayList<Thread> waitingThreads;
	/** connectedPlayer : a reference to all connected player */
	private ArrayList<Player> connectedPlayers;
	
	/** gamePackets : allow the communication between main thread, the match making thread and waiting threads*/
	public BufferPacket gamePackets;
	/** a copy of received packets */
	private ArrayList<Packets> packets;
	/** */
	private MatchMakingPacket mmPacket;
	
	public GameProcessor(ArrayList<Player> connectedPlayers)
	{
		// Create attributes
		this.games = new ArrayList<Game>();
		this.waitingPlayers = new ArrayList<Player>();
		this.connectedPlayers = connectedPlayers;
		this.gamePackets = new BufferPacket();
		this.waitingThreads = new ArrayList<Thread>();
	}
	
	public int howManyPlayerWaiting()
	{
		return waitingPlayers.size();
	}

	@Override
	public void run() 
	{
		while (true)
		{
			// Wait the receiving of packets
			packets = gamePackets.readPackets();
			
			// Browse the packets received from the main thread
			for (Packets packet : packets)
			{
				// If the received packet is a MatchMakingPacket
				if (packet.getClass().isAssignableFrom(MatchMakingPacket.class))
				{
					// Get the match making packet
					mmPacket = (MatchMakingPacket) packet;
					
					// If the player search a game (want to play)
					if (mmPacket.search)
					{
						System.err.println("koukou");
						// Browse all the waiting players
						for (int i = 0; i < waitingPlayers.size(); i++)
						{
							// Try to find an opponent
							if (waitingPlayers.get(i).getElo() / getPlayerFromIp(mmPacket.getIp()).getElo() <= mmPacket.eloTolerance)
							{
								// Stop the waiting thread of the second player
								waitingThreads.get(i).interrupt();
								
								// TODO : create a game here (Check in main)
							}
						}
					
						// If no player match, then launch a waiting thread
						waitingThreads.add(new Thread(new WaitingPlayer(mmPacket, gamePackets) ));
						
						// Add this player in the list of waiters
						waitingPlayers.add(getPlayerFromIp(mmPacket.getIp()));
					}
					else // If the player quit the queue
					{
						waitingPlayers.remove(getPlayerFromIp(mmPacket.getIp()));
					}
				}
			}
		}
	}

	private Player getPlayerFromIp(String ip) 
	{
		// Browse the list of connected players
		for (Player player : connectedPlayers)
		{
			// If the player is found
			if (player.getIp().equals(ip))
				return player;
		}
		
		System.out.println("null");
		
		// If no player matching with the ip
		return null;
	}
}
