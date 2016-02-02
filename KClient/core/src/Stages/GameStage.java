package Stages;

import Class.ConnectedStage;
import Class.Game;
import Class.Kangaroo;
import Client.Main;
import Enums.GameStates;
import Packets.ClientReadyPacket;
import Packets.GamePacket;
import Packets.GameReadyPacket;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private Game game;
	
	/** When timer reach this value, pre-game stage is over */
	private static final float DELAY = 1f;
	
	/*
	 * Components
	 */
	
	private Image background;
	private Label time;
	private Kangaroo player, opponent;

	/*
	 * Constructors
	 */
	
	public GameStage(Main main, GamePacket gamePacket)
	{
		super(main);
		
		game = new Game(gamePacket);
	}
	
	/*
	 * Methods
	 */

	@Override
	public void act(float delta)
	{
		game.update(delta);
		time.setText(String.valueOf(game.getTime()));
		
		super.act(delta);
	}
	
	@Override
	protected void askInitData()
	{
		// No init data needed to ask
	}

	@Override
	protected void initComponents()
	{
		background = new Image(game.getBackground());
		time = new Label("-", main.skin);
	}

	@Override
	protected void initDataNeededComponents()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void addListeners() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void addInitDataNeededListeners()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void addAnimations() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void addInitDataNeededAnimations() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void addActors() 
	{
		addActor(background);
		addActor(time);
		
		main.network.send(new ClientReadyPacket());
		game.setState(GameStates.Loaded);
	}

	@Override
	protected void addInitDataNeededActors() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDataReceived() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(Object data) 
	{
		if (data.getClass().isAssignableFrom(GameReadyPacket.class))
		{
			game.setState(GameStates.Running);
		}
	}

}
