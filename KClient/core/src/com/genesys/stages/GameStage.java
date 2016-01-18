package com.genesys.stages;

import Packets.ClientDataPacket;
import Packets.ClientReadyPacket;
import Packets.EndGamePacket;
import Packets.FriendsDataPacket;
import Packets.GamePacket;
import Packets.KangarooServerPacket;
import Packets.LadderDataPacket;
import Packets.NewsPacket;
import Utils.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.genesys.enums.States;
import com.genesys.kclient.AnimatedProgressBar;
import com.genesys.kclient.Kangaroo;
import com.genesys.kclient.Main;

/**
 * Game stage, where the fight take place
 * <p>
 * 1. Wait for the stage to load (kangaroo etc) <br>
 * 2. Send client is ready <br>
 * 3. Wait for both client to be ready <br>
 * 4. Let's fight <br>
 * </p>
 * 
 * @author Nerisma
 *
 */
public class GameStage extends Stage
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	
	// Packets
	private ClientDataPacket playerData, opponentData;
	private EndGamePacket endGamePacket;
	private LadderDataPacket ladderData;
	private FriendsDataPacket friendsData;
	private NewsPacket lastNews, lastBeforeNews;
	private GamePacket gamePacket;
	
	// Components
	private Kangaroo player, opponent;
	private Image background;
	private AnimatedProgressBar playerBar, opponentBar;
	private Label playerName, opponentName, time;
	
	// Variables
	private Timer timer;
	private boolean gameReady = false; // Both client have load the stage
	private boolean gamePaused = false;
	private boolean gameEnded = false;
	private boolean nextRound = false;
	
	/** Debug */
	private ShapeRenderer renderer;
	private Label stateLabelK1, stateLabelK2;
	
	/*
	 * Constructors
	 */
	
	public GameStage(Main main, GamePacket gamePacket)
	{
		super();
		this.main = main;
		
		// Set opacity to zero
		addAction(Actions.alpha(0));
				
		background = new Image(new Texture(Gdx.files.internal(gamePacket.mapPath)));
		this.addActor(background);
		
		initKangaroos(gamePacket.player, gamePacket.opponent);
		initHUD(gamePacket.player, gamePacket.opponent);
		
		// Debug
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		renderer.setColor(Color.WHITE);
		
		stateLabelK1 = new Label("", main.skin);
		stateLabelK1.setPosition(5, 20);
		stateLabelK2 = new Label("", main.skin);
		stateLabelK2.setPosition(this.getWidth() - 200, 20);
		
		this.addActor(stateLabelK1);
		this.addActor(stateLabelK2);
		
		setClientReady();
	}
	
	@Override
	public void act(float delta) 
	{	
		// Update players health
		playerBar.setValue(player.getHealth());
		opponentBar.setValue(opponent.getHealth());
		
		// Set next round if needed
		if (nextRound)
			main.setStage(new GameStage(main, gamePacket));
		
		// If both clients are ready, the game is ready
		if (gameReady && !gamePaused)
		{
			// Update time
			time.setText(String.format("%.1f", timer.getElapsedTime()));
			time.setX(this.getWidth() / 2 - time.getWidth() / 2);
			
			// Update players touch
			player.update();

			// If player press a key or release a key update him
			if (player.needUpdate())
				updateNetwork();
			
			// Debug
			stateLabelK1.setText("K1 state: " + States.values()[player.getState()].toString() + "| flip: " + player.isFlip());
			stateLabelK2.setText("K2 state: " + States.values()[opponent.getState()].toString() + "| flip: " + opponent.isFlip());
		}
		
		// On a game paused (disconnection of a client will set gamePaused at true)
		if (gamePaused && playerData != null && friendsData != null && ladderData != null && lastNews != null && lastBeforeNews != null)
		{
			gamePaused = false;
			main.setStage(new HomeStage(main, playerData, friendsData, ladderData, lastNews, lastBeforeNews));
		}
		
		// When the game is ended
		if (gameEnded && playerData != null && friendsData != null && opponentData != null && ladderData != null && lastNews != null && lastBeforeNews != null)
		{
			gameEnded = false;
			main.setStage(new EndGameStage(main, playerData, opponentData, getKangarooFromOpponentIp(endGamePacket.looserAddress), friendsData, ladderData, lastNews, lastBeforeNews));
		}
		
		super.act(delta);
	}

	@Override
	public void draw()
	{		
		super.draw();
		
		renderer.begin();
		player.drawDebug(renderer);
		opponent.drawDebug(renderer);
		
		renderer.end();
	}
	
	
	/*
	 * Methods
	 */
	
	/**
	 * Init a new game
	 * @param gamePacket
	 */
	public void init(GamePacket gamePacket)
	{
		initKangaroos(gamePacket.player, gamePacket.opponent);
		initHUD(gamePacket.player, gamePacket.opponent);
		
		// Debug
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		renderer.setColor(Color.WHITE);
		
		stateLabelK1 = new Label("", main.skin);
		stateLabelK1.setPosition(5, 20);
		stateLabelK2 = new Label("", main.skin);
		stateLabelK2.setPosition(this.getWidth() - 200, 20);
		
		this.addActor(stateLabelK1);
		this.addActor(stateLabelK2);
		
		setClientReady();
	}
	
	/**
	 * Kangaroos init
	 * @param p
	 */
	private void initKangaroos(KangarooServerPacket pPlayer, KangarooServerPacket pOpponent)
	{
		player = new Kangaroo(pPlayer);
		opponent = new Kangaroo(pOpponent);
	}
	
	/**
	 * Init hud
	 * @param pPlayer
	 * @param pOpponent
	 */
	private void initHUD(KangarooServerPacket pPlayer, KangarooServerPacket pOpponent)
	{		
		// Make player label and bars
		playerName = new Label(player.getName(), new LabelStyle(main.skin.getFont("default-font"), Color.WHITE));
		opponentName = new Label(opponent.getName(), new LabelStyle(main.skin.getFont("default-font"), Color.WHITE));
		
		playerBar = new AnimatedProgressBar(new Texture(Gdx.files.internal("sprites/barsheet.png")), 2, 4, 0, 100, player.getHealth());
		opponentBar = new AnimatedProgressBar(new Texture(Gdx.files.internal("sprites/barsheet.png")), 2, 4, 0, 100, opponent.getHealth());

		// Setup the game timer
		timer = new Timer();
		time = new Label("0.0", new LabelStyle(main.skin.getFont("korean-32"), Color.TAN));
		time.setPosition(this.getWidth() / 2 - time.getWidth() / 2, playerBar.getY());
				
		// Determine the side of kangaroos
		if (player.getX() > opponent.getX())
		{
			playerName.setPosition(this.getWidth() - playerName.getWidth() - 5, this.getHeight() - playerName.getHeight() - 5);
			opponentName.setPosition(5, this.getHeight() - opponentName.getHeight() - 5);
			
			playerBar.setPosition(this.getWidth() - playerBar.getWidth() - 5, playerName.getY() - playerName.getHeight() - 10);
			opponentBar.setPosition(opponentName.getX(), opponentName.getY() - opponentName.getHeight() - 10);
		}
		else
		{
			playerName.setPosition(5, this.getHeight() - playerName.getHeight() - 5);
			opponentName.setPosition(this.getWidth() - opponentName.getWidth() - 5, this.getHeight() - opponentName.getHeight() - 5);
			
			playerBar.setPosition(playerName.getX(), playerName.getY() - playerName.getHeight() - 10);
			opponentBar.setPosition(this.getWidth() - opponentBar.getWidth() - 5, opponentName.getY() - opponentName.getHeight() - 10);
		}
		
		// Attach all the components to the stage
		this.addActor(playerBar);
		this.addActor(opponentBar);
		this.addActor(player);
		this.addActor(opponent);
		this.addActor(playerName);
		this.addActor(opponentName);
		this.addActor(time);
	}
	
	/**
	 * This method will send the kangaroo datas to update the network
	 */
	private void updateNetwork()
	{
		main.network.send(player.getClientPacket());
		player.networkImage = player.getClientPacket();
	}
	
	/*
	 * Getters & Setters
	 */
	
	private void setClientReady()
	{	
		ClientReadyPacket p = new ClientReadyPacket();
		
		main.network.send(p);
	}	
	
	public void setGameReady()
	{
		gameReady = true;
		timer.restart();
		System.out.println("Game start!");
	}	
	
	public void setGamePaused()
	{
		gamePaused = true;
		System.out.println("Game paused !");
	}
	
	public void setNextRound(GamePacket packet)
	{
		gamePacket = packet;
		nextRound = true;
	}
	
	public void setGameEnded(EndGamePacket packet)
	{
		gameEnded = true;
		endGamePacket = packet;
		System.out.println("Game ended !");
	}
	
	public Kangaroo getKangarooFromIp(String ip)
	{
		if (player.getIp().equals(ip))
		{
			return player;
		}
		else if (opponent.getIp().equals(ip))
		{
			return opponent;
		}
		
		return null;
	}
	
	public Kangaroo getKangarooFromOpponentIp(String ip)
	{
		if (player.getIp().equals(ip))
		{
			return opponent;
		}
		else if (opponent.getIp().equals(ip))
		{
			return player;
		}
		
		return null;
	}
	
	public void setClientsData(ClientDataPacket playerData, ClientDataPacket opponentData)
	{
		this.playerData = playerData;
		this.opponentData = opponentData;
	}
	
	public void setLadderData(LadderDataPacket packet)
	{
		ladderData = packet;
	}
	
	public void setFriendsData(FriendsDataPacket packet)
	{
		friendsData = packet;
	}
	
	public void setNewsData(NewsPacket lastNewsData, NewsPacket lastBeforeNewsData)
	{
		lastNews = lastNewsData;
		lastBeforeNews = lastBeforeNewsData;
	}
}
