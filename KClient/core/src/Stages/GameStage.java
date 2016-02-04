package Stages;

import Class.ConnectedStage;
import Class.Game;
import Client.Main;
import Enums.GameStates;
import Packets.ClientReadyPacket;
import Packets.GameReadyPacket;
import Packets.InitGamePacket;

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

	/*
	 * Constructors
	 */
	
	public GameStage(Main main, InitGamePacket gamePacket)
	{
		super(main);
		
		game = new Game(gamePacket);
		
		initDataReceived();
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
		needDataToInit = true;
	}

	@Override
	protected void initComponents()
	{
		time = new Label("-", main.skin);
	}

	@Override
	protected void initDataNeededComponents()
	{
		background = new Image(game.getBackground());
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
		addActor(time);
		
		main.network.send(new ClientReadyPacket());
	}

	@Override
	protected void addInitDataNeededActors() 
	{
		addActor(background);
		addActor(game.getKPlayer());
		addActor(game.getKOpponent());
		game.setState(GameStates.Loaded);
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
