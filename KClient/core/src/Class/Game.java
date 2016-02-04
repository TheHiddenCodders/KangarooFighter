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

	private Kangaroo k1, k2;
	private Timer timer;	
	private Texture background;
	private GameStates state = GameStates.Created;
	
	/*
	 * Constructors
	 */
	
	public Game(InitGamePacket gamePacket) 
	{
		super();
		
		p1 = new Kangaroo(gamePacket.player);
		p2 = new Player(gamePacket.opponentData);
		
		background = new Texture(Gdx.files.internal("sprites/gamestage/maps/" + gamePacket.mapPath + ".png"));
		System.err.println("sprites/gamestage/maps/" + gamePacket.mapPath + ".png");
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
	
	public float getTime()
	{
		return timer.getElapsedTime();
	}
	
	public void setState(GameStates state)
	{
		this.state = state;
	}
}
