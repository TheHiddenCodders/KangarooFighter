package Kangaroo;

import java.util.ArrayList;

import Packets.ClientReadyPacket;
import Packets.DisconnexionPacket;
import Packets.GameClientPacket;
import Packets.GameReadyPacket;
import Packets.MatchMakingPacket;
import Packets.Packets;
import Server.BufferPacket;
import ServerInfo.PlayerProcessor;

public class GameProcessor implements Runnable
{
	/** games : a list containing all games */
	private ArrayList<Game> games;
	
	/** mainPackets : allow the communication between main thread, the match making thread and waiting threads */
	public BufferPacket mainPackets;
	/** mainSender : A reference to the buffer sending packets to clients */
	public BufferPacket mainSender;
	
	/** waitingPlayers : a list of reference on player waiting for play */
	private ArrayList<MatchMakingPacket> waitingPlayers;
	/** pp : a reference to all connected player */
	private PlayerProcessor pp;
	
	/** a copy of received packets */
	private ArrayList<Packets> packets;
	
	// TODO : send packet to main thread when modification occured to update server infos
	
	public GameProcessor(PlayerProcessor pp, BufferPacket sender)
	{	
		// Create attributes
		this.games = new ArrayList<Game>();
		
		this.mainPackets = new BufferPacket();
		this.mainSender = sender;
		
		this.waitingPlayers = new ArrayList<MatchMakingPacket>();
		//this.waitingTimers = new ArrayList<Timer>();
		this.pp = pp;
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
				// TODO / received a GameEndedPacket from game, to delete this game
				// If the received packet is a MatchMakingPacket
				if (packet.getClass().isAssignableFrom(MatchMakingPacket.class))
				{
					// Update the match making
					matchMakingUpdate((MatchMakingPacket) packet);
				}
				else if (packet.getClass().isAssignableFrom(DisconnexionPacket.class))
				{
					int id = isWaiter(packet.getIp());
					
					if (id != -1)
						waitingPlayers.remove(id);
				}
				// If the received packet is a GameReadyPacket
				else if (packet.getClass().isAssignableFrom(GameReadyPacket.class))
				{
					// Send it to the appropriate game
					sendToGame(packet);
				}
				// If the received packet is a ClientReadyPacket
				else if (packet.getClass().isAssignableFrom(ClientReadyPacket.class))
				{
					// Send it to the appropriate game
					sendToGame(packet);
				}
				// If the received packet is a GameClientPacket
				else if (packet.getClass().isAssignableFrom(GameClientPacket.class))
				{
					// Send it to the appropriate game
					sendToGame(packet);
				}
			}
		}
	}
	
	/** Tell if two player can launch a game together.
	 * @param p1 : the first player
	 * @param p2 : the second player
	 * @return true if players can launch a game, else otherwise
	 */
	private boolean isMatching(MatchMakingPacket p1, MatchMakingPacket p2)
	{
		// Compute the elo rate between players
		//float eloRate = Math.abs(1 - (pp.getPlayerFromIp(p1.getIp()).getElo() / pp.getPlayerFromIp(p2.getIp()).getElo() * 100));
		float eloRate = (float) (1/(1 + Math.pow(10, -(pp.getPlayerFromIp(p1.getIp()).getElo() - pp.getPlayerFromIp(p2.getIp()).getElo())/400)));
		float minTolerance  = Math.min(p1.eloTolerance, p2.eloTolerance);
		
		System.err.println( (0.5 - minTolerance) + " < " + eloRate + " < " + (0.5 + minTolerance) );

		return (eloRate >= 0.5 - minTolerance && eloRate <= 0.5 + minTolerance);
	}
	
	/** Tell if a players have already waited
	 * @param p : the player to check
	 * @return the position of the player in the wanting list
	 */
	private int isWaiter(String playerIp)
	{
		// Browse the list of waiting players
		for (int i = 0; i < waitingPlayers.size(); i++)
		{
			// If the player is found
			if (waitingPlayers.get(i).getIp().equals(playerIp))
			{
				return i;
			}
		}
		
		// If the player is nor waiting
		return -1;
	}
	
	/** Update the match making when receiving a new packet
	 * @param mmPacket : The packet received
	 */
	private void matchMakingUpdate(MatchMakingPacket mmPacket)
	{
		boolean findGame = false;
		
		// If the player search a game (want to play)
		if (mmPacket.search)
		{
			// Browse all the waiting players
			for (int i = 0; i < waitingPlayers.size(); i++)
			{
				// Try to find an opponent
				if (isMatching(mmPacket, waitingPlayers.get(i)))
				{
					// Create a game and launch it in a thread
					games.add(new Game(pp.getPlayerFromIp(waitingPlayers.get(i).getIp()), pp.getPlayerFromIp(mmPacket.getIp()), this));
					Thread t = new Thread(games.get(games.size() - 1));
					t.start();
					
					// Remove the opponent from the waiting list
					waitingPlayers.remove(waitingPlayers.get(i));
					
					// Remove the current packet is the player was waiting
					if (isWaiter(mmPacket.getIp()) > -1)
					{
						waitingPlayers.remove(isWaiter(mmPacket.getIp()));
					}
					
					findGame = true;
					break;
				}
			}
		
			// - If no player match, make this player wait -
			
			// If the player don't find game
			if (!findGame)
			{
				waitingPlayers.add(mmPacket);
			}
		}
		else // If the player quit the queue
		{
			int id = isWaiter(mmPacket.getIp());
			
			if (id != -1)
				waitingPlayers.remove(id);
		}
	}
	
	/** Send a packet the game where player is
	 * @param packet : the packet to send
	 */
	private void sendToGame(Packets packet)
	{
		// Browse all games
		for (int i = 0; i < games.size(); i++)
		{
			// If the player is in this game
			if(games.get(i).isInside(packet.getIp()))
			{
				// Send the packet to this game
				games.get(i).gameSender.sendPacket(packet);
				break;
			}
		}
	}
}
