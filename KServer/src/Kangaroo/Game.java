package Kangaroo;

/**
 * The Game class manage a game on the server. 
 * The game is create when a kangaroo don't find other. Until the kangaroo is alone, the game is waiting.
 * Then the game is launch when a second kangaroo come inside.
 * 
 * @author Kurond
 *
 */
public class Game 
{
	/** The start positionX for player 1 */
	public final static int player1X = 50;
	/** The start positionY for player 1 */
	public final static int player1Y = 0; 
	/** The start positionX for player 2 */
	public final static int player2X = 538; 
	/** The start positionY for player 2 */
	public final static int player2Y = 0; 
	
	
	/*
	 * Attributes
	 */
	private Kangaroo k1 = null, k2 = null;
	
	private boolean running;
	private boolean waiting;
	
	
	/*
	 * Constructors
	 */
	
	/**
	 * Create a game with one kangaroo and wait for one other.
	 * 
	 * @param k1 the kangaroo who create the game
	 * 
	 */
	public Game(Kangaroo k1)
	{
		this.k1 = k1;
		waiting = true;
	}
	
	/**
	 * Create a game with two kangaroos and launch it.
	 * 
	 * @param k1 the first kangaroo of the game
	 * @param k2 the second kangaroo of the game
	 * 
	 */
	public Game(Kangaroo k1, Kangaroo k2)
	{
		this(k1);
		linkKangaroo(k2);
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
	public void linkKangaroo(Kangaroo k2)
	{
		this.k2 = k2;
		waiting = false;
		running = true;
	}
	
	/**
	 * Init the game initializing kangaroo's infos. 
	 * Then send these infos to each kangaroo on the game. 
	 */
	public void init()
	{
		// Set info
		k1.setHealth(100);
		k1.setPosition(player1X, player1Y);
		
		k2.setHealth(100);
		k2.setPosition(player2X, player2Y);
		
		// Send to both players position and health
		k1.getClient().send(k1.getUpdatePacket());
		k1.getClient().send(k2.getUpdatePacket());
		
		k2.getClient().send(k2.getUpdatePacket());
		k2.getClient().send(k1.getUpdatePacket());
	}
	
	
	/*
	 * Getters and Setters
	 */
	
	/**
	 * @return true if a kangaroo is waiting for another, false otherwise
	 */
	public boolean isWaiting()
	{
		return waiting;
	}
	
	/**
	 * @return true if the game is played, false otherwise
	 */
	public boolean isRunning()
	{
		return running;
	}
	
	/**
	 * Getter for the first kangaroo
	 * @return the first kangaroo of the game
	 */
	public Kangaroo getK1()
	{
		return k1;
	}
	
	/**
	 * Getter for the second kangaroo
	 * @return the second kangaroo of the game
	 */
	public Kangaroo getK2()
	{
		return k2;
	}
}
