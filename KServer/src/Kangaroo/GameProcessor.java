package Kangaroo;

import java.util.ArrayList;

import Packets.MatchMakingPacket;
import Packets.Packets;
import Server.BufferPacket;
import Utils.Timer;

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
	/** waitingTimers : compute the time a player is waiting*/
	private ArrayList<Timer> waitingTimers;
	/** connectedPlayer : a reference to all connected player */
	private ArrayList<Player> connectedPlayers;
	
	/** a copy of received packets */
	private ArrayList<Packets> packets;
	
	public GameProcessor(ArrayList<Player> connectedPlayers, BufferPacket sender)
	{
		// Create attributes
		this.games = new ArrayList<Game>();
		
		this.mainPackets = new BufferPacket();
		this.mainSender = sender;
		this.gameSender = new BufferPacket();
		
		this.waitingPlayers = new ArrayList<MatchMakingPacket>();
		this.waitingTimers = new ArrayList<Timer>();
		this.connectedPlayers = connectedPlayers;
	}
	
	public int howManyPlayerWaiting()
	{
		return waitingPlayers.size();
	}

	@Override
	public void run() 
	{
		boolean findGame = false;
		
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
					 MatchMakingPacket mmPacket = (MatchMakingPacket) packet;
					
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
									
									games.add(new Game(getPlayerFromIp(waitingPlayers.get(i).getIp()), getPlayerFromIp(mmPacket.getIp()), this));
									
									// Remove the opponent from the waiting list
									waitingPlayers.remove(waitingPlayers.get(i));
									waitingTimers.remove(waitingTimers.get(i));
									
									// Remove the current packet is the player was waiting
									if (isWaiter(mmPacket) > -1)
									{
										waitingPlayers.remove(isWaiter(mmPacket));
										waitingTimers.remove(isWaiter(mmPacket));
									}
									
									findGame = true;
									break;
								}
							}
						}
						// TODO : send gamePacket to the game
					
						// - If no player match, then launch a waiting thread -
						
						// If the player don't find game
						if (!findGame)
						{
							// If it is a new player
							if (isWaiter(mmPacket) == -1)
							{
								// Create a new player and launch his associated timer
								waitingPlayers.add(mmPacket);
								waitingTimers.add(new Timer());
							}
							
							// Browse all the waiting player
							for (int i = 0; i < waitingPlayers.size(); i++)
							{
								// If they wait for more than 5 seconds
								if (waitingTimers.get(isWaiter(mmPacket)).getElapsedTime() >= 5.f)
								{
									// Raise the tolerance
									waitingPlayers.get(isWaiter(mmPacket)).eloTolerance += 0.5;
									
									// Try to find an opponent
									mainPackets.sendPacket(mmPacket);
								}
							}
						}
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
	
	/** Tell if two player can launch a game together.
	 * @param p1 : the first player
	 * @param p2 : the second player
	 * @return true if players can launch a game, else otherwise
	 */
	private boolean isMatching(MatchMakingPacket p1, MatchMakingPacket p2)
	{
		// Compute the elo rate between players
		float eloRate = Math.abs(1 - (getPlayerFromIp(p1.getIp()).getElo() / getPlayerFromIp(p2.getIp()).getElo() * 100));
		
		return (eloRate <= Math.min(p1.eloTolerance, p2.eloTolerance));
	}
	
	/** Tell if a players have already waited
	 * @param p : the player to check
	 * @return the position of the player in the wanting list
	 */
	private int isWaiter(MatchMakingPacket p)
	{
		// Brawse the list of waiting players
		for (int i = 0; i < waitingPlayers.size(); i++)
		{
			// If the player is found
			if (waitingPlayers.get(i).getIp() == p.getIp())
			{
				return i;
			}
		}
		
		// If the player is nor waiting
		return -1;
	}
}
