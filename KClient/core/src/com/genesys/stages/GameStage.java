package com.genesys.stages;

import Packets.ClientDataPacket;
import Packets.ClientReadyPacket;
import Packets.KangarooServerPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	private ClientDataPacket data;
	
	private Kangaroo player, opponent;
	private Texture background;
	private AnimatedProgressBar playerBar, opponentBar;
	private boolean gameReady = false; // Both client have load the stage
	private boolean gamePaused = false;
	
	private Label playerName, opponentName;
	
	/** Debug */
	private ShapeRenderer renderer;
	
	/*
	 * Constructors
	 */
	public GameStage(Main main, KangarooServerPacket pPlayer, KangarooServerPacket pOpponent)
	{
		super();
		this.main = main;
		
		background = new Texture(Gdx.files.internal("sprites/ponton.png"));
		initKangaroos(pPlayer, pOpponent);
		
		// Debug
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		renderer.setColor(Color.WHITE);
		
		setClientReady();
	}
	
	@Override
	public void act(float delta) 
	{		
		playerBar.setValue(player.getHealth());
		opponentBar.setValue(opponent.getHealth());
		
		// If both clients are ready, the game is ready
		if (gameReady && !gamePaused)
		{
			player.update();

			if (player.needUpdate())
				updateNetwork();
			
			if (Gdx.input.justTouched())
			{
				player.setHealth(player.getHealth() - 5);
				player.flip();
			}
		}
		
		// On a game paused (disconnection of a client will set gamePaused at true
		if (gamePaused && data != null)
		{
			// Actually, don't care, just leave the game stage since the game will not exist longer
			gamePaused = false;
			main.setStage(new HomeStage(main, data));
		}
		
		super.act(delta);
	}

	@Override
	public void draw()
	{
		this.getBatch().begin();
		this.getBatch().draw(background, 0, 0);
		this.getBatch().end();
		
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
	 * Kangaroos init
	 * @param p
	 */
	private void initKangaroos(KangarooServerPacket pPlayer, KangarooServerPacket pOpponent)
	{
		player = new Kangaroo(pPlayer);
		opponent = new Kangaroo(pOpponent);
		
		playerName = new Label(player.getName(), main.skin);
		opponentName = new Label(opponent.getName(), main.skin);
		
		playerBar = new AnimatedProgressBar(new Texture(Gdx.files.internal("sprites/barsheet.png")), 2, 4, 0, 100, player.getHealth());
		opponentBar = new AnimatedProgressBar(new Texture(Gdx.files.internal("sprites/barsheet.png")), 2, 4, 0, 100, opponent.getHealth());
		
		// Determine which one need to be flipped
		if (player.getX() > opponent.getX())
		{
			player.flip();
			playerName.setPosition(this.getWidth() - playerName.getWidth() - 5, this.getHeight() - playerName.getHeight() - 5);
			opponentName.setPosition(5, this.getHeight() - opponentName.getHeight() - 5);
			
			playerBar.setPosition(this.getWidth() - playerBar.getWidth() - 5, playerName.getY() - playerName.getHeight() - 10);
			opponentBar.setPosition(opponentName.getX(), opponentName.getY() - opponentName.getHeight() - 10);
		}
		else
		{
			opponent.flip();
			playerName.setPosition(5, this.getHeight() - playerName.getHeight() - 5);
			opponentName.setPosition(this.getWidth() - opponentName.getWidth() - 5, this.getHeight() - opponentName.getHeight() - 5);
			
			playerBar.setPosition(playerName.getX(), playerName.getY() - playerName.getHeight() - 10);
			opponentBar.setPosition(this.getWidth() - opponentBar.getWidth() - 5, opponentName.getY() - opponentName.getHeight() - 10);
		}
		
		opponentName.getStyle().font = main.skin.getFont("korean-font");
		
		this.addActor(playerBar);
		this.addActor(opponentBar);
		this.addActor(player);
		this.addActor(opponent);
		this.addActor(playerName);
		this.addActor(opponentName);
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
		System.out.println("Game start!");
	}	
	
	public void setGamePaused()
	{
		gamePaused = true;
		System.out.println("Game paused !");
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
	
	public void setClientData(ClientDataPacket packet)
	{
		data = packet;
	}
}
