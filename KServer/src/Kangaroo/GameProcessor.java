package Kangaroo;

import java.util.ArrayList;

import Packets.ClientReadyPacket;
import Packets.DisconnexionPacket;
import Packets.GameClientPacket;
import Packets.GameEndedPacket;
import Packets.GameReadyPacket;
import Packets.MatchMakingPacket;
import Packets.Packets;
import Packets.ServerGameEndedPacket;
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
					// If the player is waiting for a game in match making, then remove it
					removePlayerFromMatchMaking(packet.getIp());
					
					// If the player is in game, send the packet to the game
					sendToGame(packet); // Game will send an GameEndedPacket when finished properly
				}
				else if (packet.getClass().isAssignableFrom(ServerGameEndedPacket.class))
				{
					ServerGameEndedPacket serverGameEnded = (ServerGameEndedPacket) packet;
					
					games.remove(serverGameEnded.game);
					
					// If player are still in game
					if (serverGameEnded.game.getP1() != null)
					{
						// Send a GameEndedPacket to p1
						GameEndedPacket P1gameEnded = new GameEndedPacket();
						
						// TODO : fill roundResult
						P1gameEnded.roundResults = null;
						// TODO : manage elo change
						P1gameEnded.eloChange = 0;
						
						P1gameEnded.setIp(serverGameEnded.game.getP1().getIp());
						mainSender.sendPacket(P1gameEnded);
					}
					if (serverGameEnded.game.getP2() != null)
					{
						// Send a GameEndedPacket to p1
						GameEndedPacket P2gameEnded = new GameEndedPacket();
						
						// TODO : fill roundResult
						P2gameEnded.roundResults = null;
						// TODO : manage elo change
						P2gameEnded.eloChange = 0;
						
						P2gameEnded.setIp(serverGameEnded.game.getP2().getIp());
						mainSender.sendPacket(P2gameEnded);
					}
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
					boolean inGame = sendToGame(packet);
					
					// If the client is not in game
					if (!inGame)
					{
						// TODO : Send him a packet to tell him the game is ended
					}
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
			System.out.println("GameProcessor : " + pp.getPlayerFromIp(mmPacket.getIp()).getName() + " go to match making");
			
			// Browse all the waiting players
			for (int i = 0; i < waitingPlayers.size(); i++)
			{
				// Try to find an opponent
				if (isMatching(mmPacket, waitingPlayers.get(i)))
				{
					// Create a game and launch it in a thread
					createGame(pp.getPlayerFromIp(waitingPlayers.get(i).getIp()),  pp.getPlayerFromIp(mmPacket.getIp()));
					
					// Remove the opponent from the waiting list
					waitingPlayers.remove(waitingPlayers.get(i));
					
					// Remove the current packet if the player was waiting for
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
			// Remove it from the match making
			removePlayerFromMatchMaking(mmPacket.getIp());
		}
	}
	
	private void createGame(Player p1, Player p2)
	{
		games.add(new Game(p1, p2, this));
		Thread t = new Thread(games.get(games.size() - 1));
		t.start();
	}
	
	private boolean removePlayerFromMatchMaking(String playerIp)
	{
		// Check if the player is waiting for game in the match making
		int id = isWaiter(playerIp);
		
		// Then remove it
		if (id != -1)
		{
			waitingPlayers.remove(id);
			
			// If the player is not disconnected
			if (pp.getPlayerFromIp(playerIp) != null)
				System.out.println("GameProcessor : " + pp.getPlayerFromIp(playerIp).getName() + " quit the match making");
			else
				System.out.println("GameProcessor : a disconnected player quit the match making");
			
			return true;
		}
		
		return false;
	}
	
	/** Send a packet the game where player is.
	 * @param packet : the packet to send.
	 * @return true if the player was in game, false otherwise.
	 */
	private boolean sendToGame(Packets packet)
	{
		// Browse all games
		for (int i = 0; i < games.size(); i++)
		{
			// If the player is in this game
			if(games.get(i).isInside(packet.getIp()))
			{
				// Send the packet to this game
				games.get(i).gameSender.sendPacket(packet);
				return true;
			}
		}
		
		return false;
	}
}
