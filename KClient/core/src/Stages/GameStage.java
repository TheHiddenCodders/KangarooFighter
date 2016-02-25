package Stages;

import Class.ConnectedStage;
import Class.Game;
import Class.GameTimer;
import Class.ProgressBar;
import Client.Main;
import Enums.GameStates;
import Packets.ClientReadyPacket;
import Packets.GameEndedPacket;
import Packets.GameReadyPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;
import Packets.RoundResultPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class GameStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private Game game;
	
	/*
	 * Components
	 */
	
	private Image background;
	private ProgressBar playerBar, opponentBar;
	private GameTimer timer;
	private Label playerName, opponentName;

	/*
	 * Constructors
	 */
	
	public GameStage(Main main, InitGamePacket gamePacket)
	{
		super(main);
		
		game = new Game(gamePacket);
		game.getKPlayer().setName(main.player.getName());
		game.getKOpponent().setName(gamePacket.opponentData.name);
		
		initDataReceived();
	}
	
	/*
	 * Methods
	 */

	@Override
	public void act(float delta)
	{				
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
		timer = new GameTimer(new LabelStyle(main.skin.getFont("korean-32"), Color.TAN));
		timer.setPosition(800 / 2 - timer.getWidth() / 2, 480 - 50);
	}

	@Override
	protected void initDataNeededComponents()
	{
		// Init map
		background = new Image(game.getBackground());
		
		// Init bars TODO: determine which one need to be left or right
		playerBar = new ProgressBar(new Texture(Gdx.files.internal("sprites/gamestage/sheetbar.png")), 0, 100, game.getKPlayer().getHealth());
		playerBar.setPosition(800 - playerBar.getWidth() - 5, 480 - playerBar.getHeight() - 10);
		playerBar.flip();		
		opponentBar = new ProgressBar(new Texture(Gdx.files.internal("sprites/gamestage/sheetbar.png")), 0, 100, game.getKOpponent().getHealth());
		opponentBar.setPosition(5, 480 - opponentBar.getHeight() - 10);
		
		// Init names TODO: determine which one need to be left or right
		playerName = new Label(game.getKPlayer().getName(), new LabelStyle(main.skin.getFont("default-font"), new Color(0.3f, 0.3f, 0.3f, 1)));
		playerName.setPosition(800 - playerName.getWidth() - 82, 480 - playerName.getHeight() - 37);
		opponentName = new Label(game.getKOpponent().getName(), new LabelStyle(main.skin.getFont("default-font"), new Color(0.3f, 0.3f, 0.3f, 1)));
		opponentName.setPosition(80, 480 - opponentName.getHeight() - 37);
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

	}

	@Override
	protected void addInitDataNeededActors() 
	{
		addActor(background);
		addActor(game.getKPlayer());
		addActor(game.getKOpponent());
		addActor(playerBar);
		addActor(opponentBar);
		addActor(playerName);
		addActor(opponentName);
		
		// Add timer after background and all
		addActor(timer);
	
		// Set game loaded
		game.setState(GameStates.Loaded);
		
		// Send client ready packet
		main.network.send(new ClientReadyPacket());
	}

	@Override
	protected void onDataReceived() 
	{
		if (game.getState() == GameStates.Ended)
			main.setStage(new EndGameStage(main));
	}

	@Override
	public void setData(Object data) 
	{
		if (data instanceof GameReadyPacket)
		{
			System.out.println("Game is running");
			
			// Set game running
			game.setState(GameStates.Running);
			
			// Set first client packet
			main.network.send(game.getClientPacket());
		}
		
		if (data instanceof GameServerPacket)
		{
			// Update game statements
			game.update((GameServerPacket) data);
			game.update(Gdx.graphics.getDeltaTime());
			
			// Update timer
			timer.refresh(game.getTime());
			
			// If the game isn't ended 
			if (game.getState() == GameStates.Running)
			{
				// Send new client packet
				main.network.send(game.getClientPacket());
			}
		}
		
		if (data instanceof RoundResultPacket)
		{
			System.out.println("Round is over");
			
			// End round and init next one of the game
			game.endRound((RoundResultPacket) data);
			
			// Send new client ready packet
			main.network.send(new ClientReadyPacket());
		}
		
		if (data instanceof GameEndedPacket)
		{
			System.out.println("Game is ended");
			
			// Set game ended
			game.setState(GameStates.Ended);
			
			// Tell the stage that data has been received to treat them
			dataReceived();
		}
	}

}
