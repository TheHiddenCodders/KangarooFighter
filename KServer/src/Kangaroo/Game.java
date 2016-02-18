package Kangaroo;

import java.util.ArrayList;
import java.util.Random;

import Packets.ClientReadyPacket;
import Packets.GameClientPacket;
import Packets.GameReadyPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;
import Packets.Packets;
import Server.BufferPacket;
import Utils.Timer;
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
	/** timer : compute the time of the game */
	//private Timer timer;
	/** time ; the current time of the game */
	//private float time;
	/**  */
	private GameStates state;
	
	private GameServerPacket serverPacket;
	
	// - Game Communication -
	/** gameSend : send packet to games */
	public BufferPacket gameSender;
	/** gamePackets : packets received from GameProcessor */
	private ArrayList<Packets> gamePackets;
	
	private Timer timer;
	
	private ClientReadyPacket firstPacket;
	
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
		this.p1 = p1;
		this.p1.createKangaroo(player1X[mapIndex], player1Y[mapIndex]);
		this.gamePackets = new ArrayList<Packets>();
		this.timer = new Timer();
		
		serverPacket = new GameServerPacket();
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
		
		timer.restart();
		
		// While the game is prepared but not launched
		while (state == GameStates.Prepared)
		{
			// Wait for new packets
			gamePackets = gameSender.readPackets();
			
			for (int i = 0; i < gamePackets.size(); i++)
			{
				if (gamePackets.get(i).getClass().isAssignableFrom(ClientReadyPacket.class))
				{
					ClientReadyPacket readyPacket = (ClientReadyPacket) gamePackets.get(i);
					
					if (firstPacket == null)
						firstPacket = readyPacket;
					else
					{
						// Send the GameReadyPacket to both players
						GameReadyPacket p1Packet = new GameReadyPacket();
						GameReadyPacket p2Packet = new GameReadyPacket();
						
						p1Packet.setIp(p1.getIp());
						p2Packet.setIp(p2.getIp());
						
						gp.mainSender.sendPacket(p1Packet);
						gp.mainSender.sendPacket(p2Packet);
					}
					
					// TODO : Send the evolution to the sender using sendPacket
				}
				else
				{
					System.err.println("Game thread : received unreadable packet : " + gamePackets.get(i).getClass());
				}
			}
		}
		
		// While the game is running
		while (state == GameStates.Running)
		{
			// Wait for new packets
			gamePackets = gameSender.readPackets();
			
			for (int i = 0; i < gamePackets.size(); i++)
			{
				if (gamePackets.get(i).getClass().isAssignableFrom(GameClientPacket.class))
				{
					// Update the game with the packet received
					update((GameClientPacket) gamePackets.get(i));
				}
			}
		}
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
		
		serverPacket.time = timer.getElapsedTime();
		serverPacket.player = sender.getKangarooPacket();
		serverPacket.opponent = getOpponent(sender).getKangarooPacket();
		
		gp.mainSender.sendPacket(serverPacket);
	}
	
	/** Return the opponent
	 * @param p
	 * @return
	 */
	private Player getOpponent(Player p)
	{
		if (p == p1)
			return p2;
		
		return p1;
	}
	
	private Player getPlayerFromIp(String ip)
	{
		if (p1.getIp() == ip)
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
}
