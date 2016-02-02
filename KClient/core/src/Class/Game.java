package Class;

import Enums.GameStates;
import Packets.GamePacket;
import Utils.Timer;

import com.badlogic.gdx.graphics.Texture;


public class Game 
{
	/*
	 * Attributes
	 */

	private Player p1, p2;
	private Timer timer;	
	private Texture background;
	private GameStates state = GameStates.Created;
	
	/*
	 * Constructors
	 */
	
	public Game(GamePacket gamePacket) 
	{
		super();
		
		p1 = new Player(gamePacket.playerData);
		p2 = new Player(gamePacket.opponentData);
		
		background = new Texture(gamePacket.mapPath);
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
