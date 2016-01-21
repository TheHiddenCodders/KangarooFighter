package Kangaroo;

import java.util.Random;

import Packets.ClientDisconnectionPacket;
import Packets.EndGamePacket;
import Packets.GamePacket;
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
		"sprites/dojo.png",
		"sprites/ponton.png"
	};
	/*
	 * Attributes
	 */
	private Kangaroo k1 = null, k2 = null;
	private int[] baseElo;
	private int mapIndex;
	private int round;
	private int k1Wins;
	private int k2Wins;
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
		
		// Store the base elo for calculating at the end of the game
		baseElo = new int[]{ k1.getElo(), k2.getElo() };
		
		// Setting the state of the game
		waiting = false;
		running = false;
		prepared = true;
		ended = false;
		round = 0;
		k1Wins = 0;
		k2Wins = 0;
	}
	
	/**
	 * Init the game initializing kangaroo's infos. 
	 * Then send these infos to each kangaroo on the game. 
	 */
	public void init()
	{		 
		// Set info
		k1.setHealth(100);
		k1.setPosition(player1X[mapIndex], player1Y[mapIndex]);
		k2.setHealth(100);
		k2.setPosition(player2X[mapIndex], player2Y[mapIndex]);
		
		k1.setState(States.idle);
		k2.setState(States.idle);
		
		// Flip k2 because of his side (k2 is on the right) but don't if he is already flipped
		if (!k2.isFlipped())
			k2.flip(); 
		
		// Send to both players the game datas
		k1.getClient().send(getGamePacket(k1));
		k2.getClient().send(getGamePacket(k2));
		
		// After all of this, the game is prepared
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
			//k2.setState(States.hit);
			//k2.launchAnimation(States.hit);
			
			k2.setTouched(true);
		}
		
		// k2 hits k1
		if (k2.punch(k1))
		{
			//k1.setState(States.hit);
			//k1.launchAnimation(States.hit);
			
			k1.setTouched(true);
		}
		
		k1.stateMachine(this);
		k2.stateMachine(this);
		
		// Check if k1 was modified, then send modification to kangaroos
		if ( !k1.isSameAsNetwork() )
		{
			k1.getClient().send( k1.getUpdatePacket() );
			k2.getClient().send( k1.getUpdatePacket() );
			
			if (k1.isDead())
			{
				if (k2Wins == 1)
					endGame(k1.getClient().getIp(), EndGameType.Legit);
				else
					endRound(k1);
					
			}
		}
		
		// Check if k2 was modified, then send modification to kangaroos
		if ( !k2.isSameAsNetwork() )
		{
			k1.getClient().send( k2.getUpdatePacket() );
			k2.getClient().send( k2.getUpdatePacket() );
			
			if (k2.isDead())
			{
				if (k1Wins == 1)
					endGame(k2.getClient().getIp(), EndGameType.Legit);
				else
					endRound(k2);
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
	public void endGame(String looserAdress, EndGameType egType)
	{
		if (egType == EndGameType.Disconnection)
		{
			// TODO : Change ClientDisconnectionPacket to EndGamePacket.
			
			Kangaroo stayingKangaroo = getKangarooFromOpponentIp(looserAdress);
			
			// Send his data
			stayingKangaroo.getClient().send(stayingKangaroo.getClientDataPacket());
			stayingKangaroo.getClient().send(getKangarooFromIp(looserAdress).getClientDataPacket());
		
			// Send his friends
			stayingKangaroo.getClient().send(stayingKangaroo.getFriendsDataPacket());
			
			// Send the ladder and his pos
			stayingKangaroo.getClient().send(stayingKangaroo.getLadderDataPacket());
			
			// Send news
			stayingKangaroo.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
			stayingKangaroo.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));	
			
			// Make a client disconnection packet
			ClientDisconnectionPacket p = new ClientDisconnectionPacket();
			p.disconnectedClientIp = looserAdress;
			
			// Then get the opponent of the disconnected kangaroo and send him the packet
			stayingKangaroo.getClient().send(p);	
		
		}
		else
		{			
			// Get the time of the round
			time = timer.getElapsedTime();
			
			// Make end game packet
			EndGamePacket p = new EndGamePacket();
			p.endGameType = egType.ordinal();
			p.looserAddress = looserAdress;
			
			// Store the winner and the looser
			winner = getKangarooFromOpponentIp(looserAdress);
			looser = getKangarooFromIp(looserAdress);
			
			// Save the game
			ServerUtils.save(this);
			
			// Send the end game packet
			winner.getClient().send(p);
			looser.getClient().send(p);
		
			// Make the associated things of a game end
			winner.end(this);
			looser.end(this);
			
			// Update the ladder
			ServerUtils.updateLadder();
		
			// Send players data
			winner.getClient().send(winner.getClientDataPacket());
			winner.getClient().send(looser.getClientDataPacket());
			looser.getClient().send(looser.getClientDataPacket());
			looser.getClient().send(winner.getClientDataPacket());
			
			// Send friends
			winner.getClient().send(winner.getFriendsDataPacket());
			looser.getClient().send(looser.getFriendsDataPacket());
			
			// Send ladder
			winner.getClient().send(winner.getLadderDataPacket());
			looser.getClient().send(looser.getLadderDataPacket());
			
			// Send last news
			winner.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
			winner.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));				
			looser.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
			looser.getClient().send(ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));			
		}
		
		ended = true;
		running = false;
	}
	
	/**
	 * End the round properly
	 * @param winner
	 */
	public void endRound(Kangaroo looser)
	{
		// Init the game
		init();
		
		// Set next round
		round++;
		
		// Up wins
		if (k1 == looser)
			k2Wins++;
		else if (k2 == looser)
			k1Wins++;
		
		// Send new game packet
		k1.getClient().send(getGamePacket(k1));
		k2.getClient().send(getGamePacket(k2));
	}
	
	/**
	 * This method calculate the elo difference to add to the kangaroo k
	 * @param k
	 * @return
	 */
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
	
	/**
	 * Get the game packet depending of the kangaroo k
	 * @param k
	 * @return game packet
	 */
	public GamePacket getGamePacket(Kangaroo k)
	{
		GamePacket p = new GamePacket();
		p.mapPath = map[mapIndex];
		p.round = round;
		
		// Player need to receive himself as first
		if (k == k1)
		{
			p.player = k1.getUpdatePacket();
			p.opponent = k2.getUpdatePacket();
			p.playerData = k1.getClientDataPacket();
			p.opponentData = k2.getClientDataPacket();
			p.playerWins = k1Wins;
			p.opponentWins = k2Wins;
		}
		else if (k == k2)
		{
			p.player = k2.getUpdatePacket();
			p.opponent = k1.getUpdatePacket();
			p.playerData = k2.getClientDataPacket();
			p.opponentData = k1.getClientDataPacket();
			p.playerWins = k2Wins;
			p.opponentWins = k1Wins;
		}
		
		return p;
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
