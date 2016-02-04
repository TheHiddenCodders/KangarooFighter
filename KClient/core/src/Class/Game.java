package Class;

import Enums.GameStates;
import Packets.InitGamePacket;
import Utils.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class Game 
{
	/*
	 * Attributes
	 */

	private Kangaroo player, opponent;
	private Timer timer;	
	private Texture background;
	private GameStates state = GameStates.Created;
	
	/*
	 * Constructors
	 */
	
	public Game(InitGamePacket gamePacket) 
	{
		super();
		
		player = new Kangaroo(gamePacket.player);
		opponent = new Kangaroo(gamePacket.opponent);
		
		background = new Texture(Gdx.files.internal("sprites/gamestage/maps/" + gamePacket.mapPath));
		timer = new Timer();
	}
	
	/*
	 * Methods
	 */
	
	public void update(float delta)
	{
		// Update game
		if (state == GameStates.Running)
		{
			timer.update();
		}
	}	
	
	/*
	 * Getters
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
}
