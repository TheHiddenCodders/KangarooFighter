package Kangaroo;

import java.util.Random;

import Packets.ClientDisconnectionPacket;
import Packets.EndGamePacket;
import Packets.LadderDataPacket;
import Utils.ServerUtils;
import Utils.Timer;
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
	/*
	 * Attributes
	 */
	private Kangaroo k1 = null, k2 = null;
	private int[] baseElo;
	private int mapIndex;
	private Timer timer;
	private float time;
	
	private boolean waiting;
	private boolean prepared;
	private boolean running;
	private boolean ended;
	
	private Kangaroo winner = null, looser = null;
	
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
		ended = false;
		
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
		ended = false;
	}
	
	/**
	 * Init the game initializing kangaroo's infos. 
	 * Then send these infos to each kangaroo on the game. 
	 */
	public void init()
	{
		baseElo = new int[]{ k1.getElo(), k2.getElo() };
		 
		// Set info
		k1.setHealth(100);
		k1.setPosition(player1X[mapIndex], player1Y[mapIndex]);
		
		k2.setHealth(100);
		k2.setPosition(player2X[mapIndex], player2Y[mapIndex]);
		
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
		 *  Collide tests
		 */
		
		// k1 hits k2
		if (k1.punch(k2))
		{
			k2.setState(States.hit);
			k2.launchAnimation(States.hit);
		}
		
		// k2 hits k1
		if (k2.punch(k1))
		{
			k1.setState(States.hit);
			k1.launchAnimation(States.hit);
		}
		
		k1.stateMachine(this);
		k2.stateMachine(this);
		
		// Check if k1 was modified, then send modification to kangaroos
		if ( !k1.isSameAsNetwork() )
		{
			k1.getClient().send( k1.getUpdatePacket() );
			k2.getClient().send( k1.getUpdatePacket() );
			
			// If K1 is dead
			if (k1.getHealth() <= 0)
			{
				this.end(k1.getClient().getIp(), EndGameType.Legit);
				System.out.println("k1 loose the game");
			}
		}
		
		// Check if k2 was modified, then send modification to kangaroos
		if ( !k2.isSameAsNetwork() )
		{
			k1.getClient().send( k2.getUpdatePacket() );
			k2.getClient().send( k2.getUpdatePacket() );
			
			// If K2 is dead
			if (k2.getHealth() <= 0)
			{
				this.end(k2.getClient().getIp(), EndGameType.Legit);
				System.out.println("k2 loose the game");
			}
		}
		
		k1.updateNetworkImage();
		k2.updateNetworkImage();		
	}
	
	/**
	 * End the game properly
	 * @param hostAddress the address of the loosing kangaroo
	 * @param egType how the game is ending ?
	 */
	public void end(String hostAddress, EndGameType egType)
	{
		if (egType == EndGameType.Disconnection)
		{
			System.out.println("Game.end()");
			// TODO : Change ClientDisconnectionPacket to EndGamePacket.
			
			// Send his data
			getKangarooFromOpponentIp(hostAddress).getClient().send(ServerUtils.getPlayerDataPacket(getKangarooFromOpponentIp(hostAddress)));
			getKangarooFromOpponentIp(hostAddress).getClient().send(ServerUtils.getPlayerDataPacket(getKangarooFromIp(hostAddress)));
		
			// Send ladder data
			LadderDataPacket ladderDataPacket = ServerUtils.getLadderDataPacket();
			ladderDataPacket.playerPos = ServerUtils.getLadderPosition(getKangarooFromOpponentIp(hostAddress));
			getKangarooFromOpponentIp(hostAddress).getClient().send(ladderDataPacket);
			
			// Send news
			getKangarooFromOpponentIp(hostAddress).getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
			getKangarooFromOpponentIp(hostAddress).getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));	
			
			// Make a client disconnection packet
			ClientDisconnectionPacket p = new ClientDisconnectionPacket();
			p.disconnectedClientIp = hostAddress;
			
			// Then get the opponent of the disconnected kangaroo and send him the packet
			getKangarooFromOpponentIp(hostAddress).getClient().send(p);	
		
		}
		else
		{			
			time = timer.getElapsedTime();
			
			EndGamePacket p = new EndGamePacket();
			p.endGameType = egType.ordinal();
			p.looserAddress = hostAddress;
			
			winner = getKangarooFromOpponentIp(hostAddress);
			looser = getKangarooFromIp(hostAddress);
			
			ServerUtils.save(this);
			
			winner.getClient().send(p);
			looser.getClient().send(p);
		
			winner.end(this);
			looser.end(this);
			
			ServerUtils.updateLadder();
		
			winner.getClient().send(winner.getClientDataPacket());
			winner.getClient().send(looser.getClientDataPacket());
			looser.getClient().send(looser.getClientDataPacket());
			looser.getClient().send(winner.getClientDataPacket());
			
			// Send to the client the last news
			winner.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
			winner.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));				
			looser.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
			looser.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));				
			
			LadderDataPacket winnerLadderPacket = ServerUtils.getLadderDataPacket();
			winnerLadderPacket.playerPos = ServerUtils.getLadderPosition(winner);
			LadderDataPacket looserLadderPacket = ServerUtils.getLadderDataPacket();
			looserLadderPacket.playerPos = ServerUtils.getLadderPosition(looser);

			winner.getClient().send(winnerLadderPacket);
			looser.getClient().send(looserLadderPacket);
		}
		
		ended = true;
		running = false;
	}
	
	public int getEloChange(Kangaroo k)
	{
		// Elo changes
		int eloDiff = Math.abs(baseElo[0] - baseElo[1]);
		double probaWinForBestElo = 1f / (1f + Math.pow(10f,(-eloDiff / 400f)));
		double probaWinForLessElo = 1 - (1f / (1f + Math.pow(10f,(-eloDiff / 400f))));
		
		if (k.equals(winner))
		{
			if (k.getElo() == (int) Math.max(baseElo[0], baseElo[1]))
				return (int) (Math.round(k.getKCoef() * (1f - probaWinForBestElo)));
			else
				return (int) (Math.round(k.getKCoef() * (1f - probaWinForLessElo)));
		}
		else
		{
			if (k.getElo() == (int) Math.max(baseElo[0], baseElo[1]))
				return (int) (Math.round(k.getKCoef() * (0f - probaWinForBestElo)));
			else
				return (int) (Math.round(k.getKCoef() * (0f - probaWinForLessElo)));
		}
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
		timer = new Timer();
	}       
	
	
	public boolean isEnded()
	{
		return ended;
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
	
	public String getMapPath()
	{
		return "sprites/" + map[mapIndex];
	}
	
	public Kangaroo getWinner()
	{
		return winner;
	}
	
	public Kangaroo getLooser()
	{
		return looser;
	}
	
	public int[] getBaseElo()
	{
		return baseElo;
	}
	
	public float getDuration()
	{
		return time;
	}
}
