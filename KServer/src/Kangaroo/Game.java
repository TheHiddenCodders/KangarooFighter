package Kangaroo;

import Packets.ClientDisconnectionPacket;
import Utils.ServerUtils;
import enums.EndGameType;
import enums.States;

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
	
	private boolean waiting;
	private boolean prepared;
	private boolean running;
	
	
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
		running = false;
		prepared = false;
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
		running = false;
		prepared = true;
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
		
		k2.flip();
		
		prepared();
	}
	
	/**
	 * The state machine is called when a kangaroo is update. 
	 * It checks if the kangaroos need to change their state
	 */
	public void stateMachine()
	{
		/*
		 *  Collid tests
		 */
		
		// k1 hits k2
		if (k1.getCurrentAnimation().getKeyFrame().collidWith(k2.getCurrentAnimation().getKeyFrame()) && k2.getState() != States.hit && (k1.getState() == States.leftPunch || k1.getState() == States.rightPunch))
		{
			k2.setHealth(k2.getHealth() - k1.getDamage());
			k2.setState(States.hit);
			k2.launchAnimation(States.hit);
			System.out.println("k1 hits k2");
		}
		
		// k2 hits k1
		if (k2.getCurrentAnimation().getKeyFrame().collidWith(k1.getCurrentAnimation().getKeyFrame()) && k1.getState() != States.hit && (k2.getState() == States.leftPunch || k2.getState() == States.rightPunch))
		{
			k1.setHealth(k1.getHealth() - k2.getDamage());
			k1.setState(States.hit);
			k1.launchAnimation(States.hit);
			System.out.println("k2 hits k1");
		}
		
		k1.stateMachine();
		k2.stateMachine();
		
		// Check if k1 was modified, then send modification to kangaroos
		if ( !k1.isSameAsNetwork() )
		{
			k1.getClient().send( k1.getUpdatePacket() );
			k2.getClient().send( k1.getUpdatePacket() );
		}
		
		// Check if k2 was modified, then send modification to kangaroos
		if ( !k2.isSameAsNetwork() )
		{
			k1.getClient().send( k2.getUpdatePacket() );
			k2.getClient().send( k2.getUpdatePacket() );
		}
		
		k1.updateNetworkImage();
		k2.updateNetworkImage();		
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
	 * @return true if the game is full but clients not ready
	 */
	public boolean isPrepared()
	{
		return prepared;
	}
	
	public void prepared()
	{
		prepared = true;
	}
	
	/**
	 * @return true if both playersare ready, false otherwise
	 */
	public boolean isRunning()
	{
		return running;
	}
	
	public void run()
	{
		running = true;
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
	
	/**
	 * Get kangaroo from ip
	 * @param ip
	 * @return k1, k2, otherwise : null
	 */
	public Kangaroo getKangarooFromIp(String ip)
	{
		if (k1.getClient().getIp().equals(ip))
			return k1;
		else if (k2.getClient().getIp().equals(ip))
			return k2;
		
		return null;
	}
	
	/**
	 * Get kangaroo from opponent ip
	 * @param ip
	 * @return k1, k2, otherwise : null
	 */
	public Kangaroo getKangarooFromOpponentIp(String ip)
	{
		if (k1.getClient().getIp().equals(ip))
			return k2;
		else if (k2.getClient().getIp().equals(ip))
			return k1;
		
		return null;
	}
	
	/**
	 * End the game properly
	 * @param hostAddress
	 */
	public void end(String hostAddress, EndGameType egType)
	{
		// Make a client disconnection packet
		ClientDisconnectionPacket p = new ClientDisconnectionPacket();
		p.disconnectedClientIp = hostAddress;
		
		// Then get the opponent of the disconnected kangaroo and send him the packet
		getKangarooFromOpponentIp(hostAddress).getClient().send(p);
		getKangarooFromOpponentIp(hostAddress).getClient().send(ServerUtils.getPlayerDatas(getKangarooFromOpponentIp(hostAddress)));
	}
}