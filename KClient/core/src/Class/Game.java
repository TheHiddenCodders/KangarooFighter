package Class;

import Enums.GameStates;
import Packets.GameEndedPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;
import Packets.RoundResultPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class Game 
{
	/*
	 * Attributes
	 */

	private InitGamePacket initGamePacket;
	private Kangaroo player, opponent;
	private Texture background;
	private GameStates state = GameStates.Created;
	private RoundResultPacket[] roundResults;
	private GameEndedPacket gameEndedPacket;
	private String winnerName;
	private float time;
	private int round = 0;
	
	/*
	 * Constructors
	 */
	
	public Game(InitGamePacket gamePacket) 
	{
		super();
		
		// Store game packet
		this.initGamePacket = gamePacket;
		
		// Init kangaroos
		player = new Kangaroo(gamePacket.player);
		opponent = new Kangaroo(gamePacket.opponent);
		
		// Init map
		background = new Texture(Gdx.files.internal("sprites/gamestage/maps/" + gamePacket.mapPath));
		
		// Init round results packets
		roundResults = new RoundResultPacket[3];
		
		// Init first round
		initRound();
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * This method will update the players animatons and store the controls of the player
	 * @param delta
	 */
	public void update(float delta)
	{
		// Update game
		if (state == GameStates.Running)
		{			
			// Update kangaroos
			player.act(delta);
			opponent.act(delta);
		}
	}	
	
	/**
	 * This method will update players and opponents statistics (positions life etc)
	 * @param packet
	 */
	public void update(GameServerPacket packet)
	{
		// Update time
		time = ((int) (packet.time / 100)) / 10f;
		
		// Update players statistics
		player.update(packet.player);
		opponent.update(packet.opponent);
	}
	
	/**
	 * This method init a round
	 */
	private void initRound()
	{
		// Reset time
		time = 0;
		
		// Reset players
		player.update(initGamePacket.player);
		opponent.update(initGamePacket.opponent);
		
		// Change game state
		setState(GameStates.Loaded);
	}
	
	/**
	 * This method end a round, reseting kangaroos and time (also storing round result packet)
	 * @param packet
	 */
	public void endRound(RoundResultPacket packet)
	{		
		for (int i = 0; i < roundResults.length; i++)
		{
			if (roundResults[i] == null)
			{
				roundResults[i] = packet;
				System.out.println("Store round result packet at index " + i);
				break;
			}
		}
		
		// Add round
		round++;
		
		// Init next round
		initRound();
	}
	
	/*
	 * Getters - Setters
	 */
	
	public Texture getBackground()
	{
		return background;
	}
	
	public Kangaroo getKPlayer()
	{
		return player;
	}
	
	public Kangaroo getKOpponent()
	{
		return opponent;
	}
	
	public float getTime()
	{
		return time;
	}
	
	public void setState(GameStates state)
	{
		this.state = state;
	}
	
	public int getRound()
	{
		return round;
	}
	
	public GameStates getState()
	{
		return state;
	}
	
	public RoundResultPacket[] getRoundResults()
	{
		return roundResults;
	}
	
	public InitGamePacket getInitGamePacket()
	{
		return initGamePacket;
	}
	
	public String getWinnerName()
	{
		return winnerName;
	}

	public void setEndGamePacket(GameEndedPacket data)
	{
		gameEndedPacket = data;
		roundResults = data.roundResults;
		
		if (data.eloChange > 0)
			winnerName = player.getName();
		else
			winnerName = opponent.getName();
	}
	
	public GameEndedPacket getGameEndedPacket()
	{
		return gameEndedPacket;
	}
}
