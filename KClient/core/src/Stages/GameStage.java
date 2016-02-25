package Stages;

import Class.ConnectedStage;
import Class.Game;
import Class.ProgressBar;
import Client.Main;
import Enums.GameStates;
import Packets.ClientReadyPacket;
import Packets.GameEndedPacket;
import Packets.GameReadyPacket;
import Packets.GameServerPacket;
import Packets.InitGamePacket;

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
	private Label time;
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
		// Update game
		game.update(delta);
		
		// Update timer
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
		addActor(time);
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
	
		game.setState(GameStates.Loaded);
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
			game.setState(GameStates.Running);
			main.network.send(game.getClientPacket());
		}
		
		if (data instanceof GameServerPacket)
		{
			game.update((GameServerPacket) data);
			main.network.send(game.getClientPacket());
		}
		
		if (data instanceof GameEndedPacket)
		{
			System.out.println("Game is ended");
			game.setState(GameStates.Ended);
			dataReceived();
		}
	}

}
