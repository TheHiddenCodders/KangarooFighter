package Kangaroo;

import java.util.ArrayList;
import java.util.Random;

import Packets.ClientReadyPacket;
import Packets.DisconnexionPacket;
import Packets.GameClientPacket;
import Packets.GameReadyPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;
import Packets.Packets;
import Packets.RoundResultPacket;
import Packets.ServerGameEndedPacket;
import Server.BufferPacket;
import enums.GameStates;

/**
 * The Game class manage a game on the server. 
 * The game is create when a kangaroo don't find other. Until the kangaroo is alone, the game is waiting.
 * Then the game is launch when a second kangaroo come inside.
 * 
 * @author Kurond
 *
 */
public class Game implements Runnable
{
	/*
	 * TODO: Store this in a file
	 */
	/** The start positionX for player 1 */
	public static int[] player1X = 
	{
		50,
		50
	};
	/** The start positionY for player 1 */
	public static int[] player1Y = 
	{
		20,
		120
	};
	/** The start positionX for player 2 */
	public static int[] player2X = 
	{
		538,
		538
	}; 
	/** The start positionY for player 2 */
	public static int[] player2Y =
	{
		20,
		120
	}; 
	
	
	/** The map paths */
	public static String[] map =
	{
		"dojo.png",
		"ponton.png"
	};
	
	// - Game Infos -
	/** gp : a reference to the GameProcessor allowing communication */
	private GameProcessor gp;
	/** p1 & p2 : the 2 players in the game */
	private Player p1 = null, p2 = null;
	/** mapIndex : the index of the map (background) */
	private int mapIndex;
	/**  */
	private GameStates state;
	
	private ArrayList<RoundResultPacket> previousResult;
	
	private GameServerPacket serverPacket;
	
	// - Game Communication -
	/** gameSend : send packet to games */
	public BufferPacket gameSender;
	/** gamePackets : packets received from GameProcessor */
	private ArrayList<Packets> gamePackets;
	
	private ClientReadyPacket firstPacket;
	
	private long delta;
	private long lastTime;
	
	/** Constructor : Create a game with one kangaroo and wait for one other.
	 * @param k1 the kangaroo who create the game.
	 */
	public Game(Player p1, GameProcessor gp)
	{
		this.gp = gp;
		this.gameSender = new BufferPacket();
		
		// No first packet for the moment
		firstPacket = null;
		
		// Set the state of the game
		setState(GameStates.Waiting);
		
		// Choose randomly a map
		Random r = new Random(System.currentTimeMillis());
		mapIndex = r.nextInt(map.length);
		
		// Store the first player and create is kangaroo
		p1.getPacket().inGame = true;
		this.p1 = p1;
		this.p1.createKangaroo(player1X[mapIndex], player1Y[mapIndex]);
		this.gamePackets = new ArrayList<Packets>();
		
		serverPacket = new GameServerPacket();
		previousResult = new ArrayList<RoundResultPacket>();
	}
	
	/** Constructor : Create a game with two kangaroos and launch it.
	 * @param k1 the first kangaroo of the game
	 * @param k2 the second kangaroo of the game
	 */
	public Game(Player p1, Player p2, GameProcessor gp)
	{
		// Create the first kangaroo
		this(p1, gp);
		
		// Create the second one and initiate the game
		linkKangaroo(p2);
	}


	/** Link a second kangaroo to this game
	 * @param k2 the second kangaroo who join the game
	 */
	public void linkKangaroo(Player p2)
	{
		// Create the second kangaroo
		p2.getPacket().inGame = true;
		this.p2 = p2;
		p2.createKangaroo(player2X[mapIndex], player2Y[mapIndex]);
		
		// Set the state of the game
		setState(GameStates.Prepared);
		
		// Tell players that game is ready
		gp.mainSender.sendPacket((Packets) getInitPacket(p1));
		gp.mainSender.sendPacket((Packets) getInitPacket(this.p2));
	}
	
	// Game update 
	@Override
	public void run() 
	{
		// The game is updated when it receive a packet (synchrone)
		
		while (previousResult.size() < 3 && state != GameStates.ended)
		{
			System.err.println("round : " + previousResult.size() + ", state : " + state );

			// While the game is prepared but not launched
			while (state == GameStates.Prepared)
			{
				if (previousResult.size() > 0)
				{
					// Send a copy of to clients
					previousResult.get(previousResult.size() - 1).setIp(p1.getIp());
					gp.mainSender.sendPacket(new RoundResultPacket(previousResult.get(previousResult.size() - 1)));
	
					previousResult.get(previousResult.size() - 1).setIp(p2.getIp());
					gp.mainSender.sendPacket(new RoundResultPacket(previousResult.get(previousResult.size() - 1)));
	
					// Prepare game for a new round
					p1.createKangaroo(player1X[mapIndex], player1Y[mapIndex]);
					p2.createKangaroo(player2X[mapIndex], player2Y[mapIndex]);
					serverPacket.time = 0;
				}
				
				// Wait for new packets
				gamePackets = gameSender.readPackets();
				
				// Browse all packets
				for (int i = 0; i < gamePackets.size(); i++)
				{
					// TODO : manage disconnexion here
					// Received a ClientReadyPacket
					if (gamePackets.get(i).getClass().isAssignableFrom(ClientReadyPacket.class))
					{
						ClientReadyPacket readyPacket = (ClientReadyPacket) gamePackets.get(i);
						
						System.out.println("Game thread : " + getPlayerFromIp(readyPacket.getIp()).getName() + " is ready to start round " + previousResult.size());
						
						// If the fist player is ready, then store his packet
						if (firstPacket == null)
							firstPacket = readyPacket;
						// Else the two players are ready, then the game can start
						else			
						{
							setState(GameStates.Running);
							firstPacket = null;
						}
					}
					else if (gamePackets.get(i).getClass().isAssignableFrom(GameClientPacket.class))
					{
						System.err.println("End Round Result from Game");
						
						// Send the RoundResultPacket to the second player
						previousResult.get(previousResult.size() - 1).setIp(gamePackets.get(i).getIp());
						gp.mainSender.sendPacket(new RoundResultPacket(previousResult.get(previousResult.size() - 1)));
					}
					else
					{
						System.err.println("Game thread : received unreadable packet : " + gamePackets.get(i).getClass() + " in " + state + " state");
					}
				}
			}
			
			// Send the GameReadyPacket to both players
			GameReadyPacket p1Packet = new GameReadyPacket();
			GameReadyPacket p2Packet = new GameReadyPacket();
			
			p1Packet.setIp(p1.getIp());
			p2Packet.setIp(p2.getIp());
			
			gp.mainSender.sendPacket(p1Packet);
			gp.mainSender.sendPacket(p2Packet);
			
			// Init delta time
			lastTime = System.currentTimeMillis();
			
			// While the game is running
			while (state == GameStates.Running)
			{
				// Wait for new packets
				gamePackets = gameSender.readPackets();
				
				// Update time
				delta = System.currentTimeMillis() - lastTime;
				
				// Browse all packets
				for (int i = 0; i < gamePackets.size(); i++)
				{
					// Received a DisconnexionPacket
					if (gamePackets.get(i).getClass().isAssignableFrom(DisconnexionPacket.class))
					{
						// TODO : End the game here
						state = GameStates.ended;
						
						ServerGameEndedPacket packet = new ServerGameEndedPacket();
						packet.game = this;
						gp.mainPackets.sendPacket(packet);
						
						break;
					}
					// Received a GameClientPacket
					else if (gamePackets.get(i).getClass().isAssignableFrom(GameClientPacket.class))
					{
						// Update the game with the packet received
						update((GameClientPacket) gamePackets.get(i));
					}
				}
				
				// Update lastTime only if delta was set
				if (delta != 0)
					lastTime = System.currentTimeMillis();
			}
		
			// When a round is ended
		}
			
		// When the game is ended
		
	}
	
	/** Get the game packet depending of the kangaroo k
	 * @param k
	 * @return game packet
	 */
	public InitGamePacket getInitPacket(Player p)
	{
		// Create the packet
		InitGamePacket gamePacket = new InitGamePacket();
		
		// - Fill the packet in, player need to receive himself as first -
		gamePacket.mapPath = map[mapIndex]; 
		
		if (p == p1)
		{
			gamePacket.setIp(p1.getIp());
			
			gamePacket.player = p1.getKangarooPacket();
			gamePacket.opponent = p2.getKangarooPacket();
			gamePacket.opponent.flip = true;
			
			
			gamePacket.opponentData = p2.getPacket();
			gamePacket.playerWins = p1.getKangaroo().getWins();
			gamePacket.opponentWins = p2.getKangaroo().getWins();
		}
		else if (p == p2)
		{
			gamePacket.setIp(p2.getIp());
			gamePacket.player = p2.getKangarooPacket();
			gamePacket.opponent = p1.getKangarooPacket();
			gamePacket.player.flip = true;
			
			gamePacket.opponentData = p1.getPacket();
			gamePacket.playerWins = p2.getKangaroo().getWins();
			gamePacket.opponentWins = p1.getKangaroo().getWins();
		}
		
		return gamePacket;
	}

	public GameStates getState() 
	{
		return state;
	}

	public void setState(GameStates state) 
	{
		this.state = state;
	}
	
	/** Update the game with info received from one of the player
	 * @param packet : The packet sent by the player
	 */
	private void update(GameClientPacket packet)
	{
		Player sender = getPlayerFromIp(packet.getIp());
		
		// Update the server packet
		serverPacket.time += (delta);
		serverPacket.player = sender.getKangarooPacket();
		serverPacket.opponent = getOpponent(sender).getKangarooPacket();
		
		// If the actual round is ended (10sec)
		if (serverPacket.time >= 10000)
		{
			// Send the result packet and wait for client ready
			previousResult.add( new RoundResultPacket() );

			// Fill the packet in (NERISMA: add winner name and change winner/loser by player/opponent)
			previousResult.get(previousResult.size() - 1).winnerName = p1.getName();
			previousResult.get(previousResult.size() - 1).player = serverPacket.player;
			previousResult.get(previousResult.size() - 1).opponent = serverPacket.opponent;
			
			// If this isn't the 3rd round
			if (previousResult.size() < 3)
			{
				state = GameStates.Prepared;
				serverPacket.time = 0;
			}
			else
			{
				// Game is ended - Send the ServerGameEndedPacket to GameProcessor
				ServerGameEndedPacket gameEnded = new ServerGameEndedPacket();
				gameEnded.game = this;
				
				gp.mainPackets.sendPacket(gameEnded);
				
				state = GameStates.ended;
				
				System.err.println("Game ended");
			}
		}
		// Round running
		else
		{
			if (packet.leftArrow)
			{
				p1.getKangarooPacket().x += delta * 2;
			}
		}
		
		// If game is still running send the update to client
		if (state == GameStates.Running)
		{
			serverPacket.setIp(packet.getIp());
			gp.mainSender.sendPacket(new GameServerPacket(serverPacket));
		}
	}
	
	public Player getWinner()
	{
		int p1Win = 0;
		
		if (state != GameStates.ended)
			return null;
		
		for (RoundResultPacket result : previousResult)
		{
			if (result.winnerName == p1.getName())
				p1Win++;
		}
		
		if (p1Win == 2)
			return p1;
		
		return p2;
	}
	
	/** Return the opponent
	 * @param p
	 * @return
	 */
	public Player getOpponent(Player p)
	{
		if (p == p1)
			return p2;
		
		return p1;
	}
	
	public Player getPlayerFromIp(String ip)
	{
		if (p1.getIp().equals(ip))
			return p1;
		
		return p2;
	}
	
	// WARNING : Could have concurrent problems
	/** Tell if a player is inside the game
	 * @param ip : the player's ip
	 * @return true if the player is in this game, false otherwise
	 */
	public boolean isInside(String ip)
	{
		// Check if the player is inside
		if (p1.getIp().equals(ip))
			return true;
		else if (p2.getIp().equals(ip))
			return true;
		
		// If the player is not in this game, then return false
		return false;
	}
	
	public Player getP1()
	{
		return p1;
	}
	
	public Player getP2()
	{
		return p2;
	}
	
	public ArrayList<RoundResultPacket> getRoundResults()
	{
		return previousResult;
	}
}
