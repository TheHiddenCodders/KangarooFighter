package Class;

import Enums.GameStates;
import Packets.GameClientPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;
import Utils.Timer;

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

	private Kangaroo player, opponent;
	private GameClientPacket packet;
	private Timer timer;	
	private Texture background;
	private GameStates state = GameStates.Created;
	
	/*
	 * Constructors
	 */
	
	public Game(InitGamePacket gamePacket) 
	{
		super();
		
		// Init kangaroos
		player = new Kangaroo(gamePacket.player);
		opponent = new Kangaroo(gamePacket.opponent);
		
		// Init map
		background = new Texture(Gdx.files.internal("sprites/gamestage/maps/" + gamePacket.mapPath));
		
		// Init timer
		timer = new Timer();
		
		// Init game client packet
		packet = new GameClientPacket();
	}
	
	/*
	 * Methods
	 */
	
	public void update(float delta)
	{
		// Update game
		if (state == GameStates.Running)
		{
			// Update time
			timer.update();
			
			// Update kangaroos
			player.act(delta);
			opponent.act(delta);
			
			// Fill packet with player controls
			fillClientPacket();
		}
	}	
	
	public void update(GameServerPacket packet)
	{
		player.update(packet.player);
		opponent.update(packet.opponent);
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
		return timer.getElapsedTime();
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
