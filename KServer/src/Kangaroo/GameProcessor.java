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
	
	/** mainPackets : allow the communication between main thread, the match making thread and waiting threads */
	public BufferPacket mainPackets;
	/** mainSender : A reference to the buffer sending packets to clients */
	public BufferPacket mainSender;
	/** gameSend : send packet to games */
	public BufferPacket gameSender;
	
	/** waitingPlayers : a list of reference on player waiting for play */
	private ArrayList<MatchMakingPacket> waitingPlayers;
	/** waitingThread : same size as waitingPlayers, allow the match making to stop thread if a player was found */
	private ArrayList<WaitingPlayer> waitingThreads;
	/** connectedPlayer : a reference to all connected player */
	private ArrayList<Player> connectedPlayers;
	
	/** a copy of received packets */
	private ArrayList<Packets> packets;
	/** */
	private MatchMakingPacket mmPacket;
	
	public GameProcessor(ArrayList<Player> connectedPlayers, BufferPacket sender)
	{
		// Create attributes
		this.games = new ArrayList<Game>();
		
		this.mainPackets = new BufferPacket();
		this.mainSender = sender;
		this.gameSender = new BufferPacket();
		
		this.waitingPlayers = new ArrayList<MatchMakingPacket>();
		this.connectedPlayers = connectedPlayers;
		
		this.waitingThreads = new ArrayList<WaitingPlayer>();
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
			packets = mainPackets.readPackets();
			
			// Browse the packets received from the main thread
			for (Packets packet : packets)
			{
				// If the received packet is a MatchMakingPacket
				if (packet.getClass().isAssignableFrom(MatchMakingPacket.class))
				{
					System.out.println("GameProcessor : receive a MatchMakingPacket");
					
					// Get the match making packet
					mmPacket = (MatchMakingPacket) packet;
					
					// If the player search a game (want to play)
					if (mmPacket.search)
					{
						// Browse all the waiting players
						for (int i = 0; i < waitingPlayers.size(); i++)
						{
							// Don't check the player him self
							if (getPlayerFromIp(waitingPlayers.get(i).getIp()).getName() != getPlayerFromIp(mmPacket.getIp()).getName())
							{
								// Try to find an opponent
								if (isMatching(mmPacket, waitingPlayers.get(i)))
								{
									System.err.println(getPlayerFromIp(waitingPlayers.get(i).getIp()).getName() + " vs " + getPlayerFromIp(mmPacket.getIp()).getName());
									
									// Stop the waiting thread of the second player and remove it.
									//waitingThreads.get(i).interrupt();
									waitingThreads.get(i).quit();
									waitingThreads.remove(waitingThreads.get(i));
									
									// Remove the opponent from the waiting list
									waitingPlayers.remove(waitingPlayers.get(i));
									
									// TODO : create a game here (Nerisma add code, check it)
									// games.add(new Game(waitingPlayers.get(i), getPlayerFromIp(mmPacket.getIp()), server?));
								}
							}
						}
						// TODO : send gamePacket to the game
					
						// If no player match, then launch a waiting thread
						waitingThreads.add(new WaitingPlayer(mmPacket, mainPackets));
						Thread t = new Thread(waitingThreads.get(waitingThreads.size() - 1));
						t.start();
						
						// Add this player in the list of waiters
						waitingPlayers.add(mmPacket);
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
	
	private boolean isMatching(MatchMakingPacket p1, MatchMakingPacket p2)
	{
		// Compute the elo rate between players
		float eloRate = Math.abs(1 - (getPlayerFromIp(p1.getIp()).getElo() / getPlayerFromIp(p2.getIp()).getElo() * 100));
		
		return (eloRate < Math.min(p1.eloTolerance, p2.eloTolerance));
	}
}
