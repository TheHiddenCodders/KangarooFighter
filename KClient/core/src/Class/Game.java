package Class;

import Enums.GameStates;
import Packets.GameClientPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;
import Packets.RoundResultPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;


public class Game 
{
	/*
	 * Controls
	 */
	
	private static int AKey = Keys.A;
	private static int ZKey = Keys.Z;
	private static int RKey = Keys.R;
	private static int leftKey = Keys.LEFT;
	private static int rightKey = Keys.RIGHT;
	private static int upKey = Keys.UP;
	private static int downKey = Keys.DOWN;
	
	/*
	 * Attributes
	 */

	private InitGamePacket initGamePacket;
	private Kangaroo player, opponent;
	private GameClientPacket packet;	
	private Texture background;
	private GameStates state = GameStates.Created;
	private RoundResultPacket[] roundResults;
	private float time;
	
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
		
		// Init game client packet
		packet = new GameClientPacket();
		
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
			
			// Fill packet with player controls
			fillClientPacket();
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
		// Get position of the new result packet
		int position = 0;
		
		for (int i = 0; i < roundResults.length; i++)
		{
			if (roundResults[i] == null)
				position = i;
		}
			
		roundResults[position] = packet;
		
		// Init next round
		initRound();
	}
	
	/**
	 * This method will fill the client packet with the keys
	 */
	private void fillClientPacket()
	{
		if (Gdx.input.isKeyPressed(leftKey))
			packet.leftArrow = true;
		else
			packet.leftArrow = false;
		
		if (Gdx.input.isKeyPressed(rightKey))
			packet.rightArrow = true;
		else
			packet.rightArrow = false;
		
		if (Gdx.input.isKeyPressed(upKey))
			packet.topArrow = true;
		else
			packet.topArrow = false;
		
		if (Gdx.input.isKeyPressed(downKey))
			packet.bottomArrow = true;
		else
			packet.bottomArrow = false;
		
		if (Gdx.input.isKeyPressed(AKey))
			packet.leftPunch = true;
		else
			packet.leftPunch = false;
		
		if (Gdx.input.isKeyPressed(ZKey))
			packet.rightPunch = true;
		else
			packet.rightPunch= false;
		
		if (Gdx.input.isKeyPressed(RKey))
			packet.guard = true;
		else
			packet.guard = false;
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
	
	public GameStates getState()
	{
		return state;
	}
	
	public GameClientPacket getClientPacket()
	{
		return packet;
	}
}
