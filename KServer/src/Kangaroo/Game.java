package Kangaroo;

import java.util.Random;

import Packets.ClientDisconnectionPacket;
import Packets.EndGamePacket;
import Packets.GamePacket;
import Server.BufferPacket;
import Server.Server;
import Utils.ServerUtils;
import Utils.Timer;
import enums.Direction;
import enums.EndGameType;
import enums.GameStates;
import enums.States;

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
	 * Store this in a file
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
	/*
	 * Attributes
	 */
	private Server server;
	private Kangaroo k1 = null, k2 = null;
	private int mapIndex;
	private Timer timer;
	private float time;
	private BufferPacket readBuffer;
	private GameStates state;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Create a game with one kangaroo and wait for one other.
	 * 
	 * @param k1 the kangaroo who create the game
	 * 
	 */
	public Game(Player p1, Server server)
	{
		this.server = server;
		this.k1 = new Kangaroo(p1, Direction.RIGHT);
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
	public Game(Player p1, Player p2)
	{
		this(p1);
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
		this.k2 = new Kangaroo(p2, Direction.LEFT);
		setState(GameStates.Prepared);
		
		server.sendBuffer.addPacket(getGamePacket(k1));
		server.sendBuffer.addPacket(getGamePacket(k2));
	}
	
	// Game update 
	@Override
	public void run() 
	{
		// TODO : 
		
	}
	
	/**
	 * Get the game packet depending of the kangaroo k
	 * @param k
	 * @return game packet
	 */
	public GamePacket getGamePacket(Kangaroo k)
	{
		GamePacket p = new GamePacket();
		
		int round = k1.getWins() + k2.getWins() + 1;
		p.round = round;
		p.mapPath = map[mapIndex]; 
		
		// Player need to receive himself as first
		if (k == k1)
		{
			p.player = k1.getUpdatePacket();
			p.opponent = k2.getUpdatePacket();
			p.playerData = k1.getClientDataPacket();
			p.opponentData = k2.getClientDataPacket();
			p.playerWins = k1.getWins();
			p.opponentWins = k2.getWins();
		}
		else if (k == k2)
		{
			p.player = k2.getUpdatePacket();
			p.opponent = k1.getUpdatePacket();
			p.playerData = k2.getClientDataPacket();
			p.opponentData = k1.getClientDataPacket();
			p.playerWins = k2.getWins();
			p.opponentWins = k1.getWins();
		}
		
		return p;
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
