package Kangaroo;

import java.util.ArrayList;
import java.util.Random;

import Packets.GamePacket;
import Packets.Packets;
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
		"sprites/dojo.png",
		"sprites/ponton.png"
	};

	
	/** Allow communication with game processor */
	private GameProcessor gp;
	private Player p1 = null, p2 = null;
	private int mapIndex;
	private Timer timer;
	private float time;
	private GameStates state;
	private ArrayList<Packets> gamePackets;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Create a game with one kangaroo and wait for one other.
	 * 
	 * @param k1 the kangaroo who create the game
	 * 
	 */
	public Game(Player p1, GameProcessor gp)
	{
		this.gp = gp;
		this.p1 = p1;
		this.p1.createKangaroo();
		this.gamePackets = new ArrayList<Packets>();
		
		setState(GameStates.Waiting);
		
		Random r = new Random(System.currentTimeMillis());
		mapIndex = r.nextInt(map.length);
	}
	
	/**
	 * Create a game with two kangaroos and launch it.
	 * 
	 * @param k1 the first kangaroo of the game
	 * @param k2 the second kangaroo of the game
	 * 
	 */
	public Game(Player p1, Player p2, GameProcessor gp)
	{
		this(p1, gp);
		linkKangaroo(p2);
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Link a second kangaroo to this game
	 * 
	 * @param k2 the second kangaroo who join the game
	 * 
	 */
	public void linkKangaroo(Player p2)
	{
		this.p2 = p2;
		p2.createKangaroo();
		
		setState(GameStates.Prepared);
		
		gp.mainSender.sendPacket((Packets) getGamePacket(p1));
		gp.mainSender.sendPacket((Packets) getGamePacket(p2));
	}
	
	// Game update 
	@Override
	public void run() 
	{
		// The game is updated when it receive a packet (synchrone)
		
		GamePacket readPacket; // ClientGamePacket
		// ServerGamePacket sendPacket
		
		// Play the game until the end
		while (state != GameStates.ended)
		{
			// Wait for new packets
			gamePackets = gp.gameSender.readPackets();
			
			for (int i = 0; i < gamePackets.size(); i++)
			{
				readPacket = (GamePacket) gamePackets.get(i);
				
				// TODO : Game update here
				
				// TODO : Send the evolution to the sender using sendPacket
			}
		}
	}
	
	/**
	 * Get the game packet depending of the kangaroo k
	 * @param k
	 * @return game packet
	 */
	public GamePacket getGamePacket(Player p)
	{
		GamePacket gamePacket = new GamePacket();
		
		gamePacket.mapPath = map[mapIndex]; 
		
		// Player need to receive himself as first
		if (p == p1)
		{
			gamePacket.player = p1.getUpdatePacket();
			gamePacket.opponent = p2.getUpdatePacket();
			gamePacket.opponentData = p2.getPacket();
			gamePacket.playerWins = p1.getKangaroo().getWins();
			gamePacket.opponentWins = p2.getKangaroo().getWins();
		}
		else if (p == p2)
		{
			gamePacket.player = p2.getUpdatePacket();
			gamePacket.opponent = p1.getUpdatePacket();
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
}
