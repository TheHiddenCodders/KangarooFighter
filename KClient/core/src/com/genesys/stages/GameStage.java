package com.genesys.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.genesys.kclient.Kangaroo;
import com.genesys.kclient.Main;

import Packets.ClientReadyPacket;
import Packets.UpdateKangarooPacket;

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
	
	private Kangaroo player, opponent;
	private Texture background;
	private boolean gameReady = false; // Both client have load the stage
	private boolean gamePaused = false;
	
	private Label playerName, opponentName;
	
	/*
	 * Constructors
	 */
	public GameStage(Main main, UpdateKangarooPacket pPlayer, UpdateKangarooPacket pOpponent)
	{
		super();
		this.main = main;
		
		background = new Texture(Gdx.files.internal("sprites/dojo.png"));
		initKangaroos(pPlayer, pOpponent);
		setClientReady();
	}
	
	@Override
	public void act(float delta) 
	{					
		// If both clients are ready, the game is ready
		if (gameReady && !gamePaused)
		{
			player.update();

			if (!player.isSameAsNetwork())
				updateNetwork();
		}
		
		// On a game paused (disconnection of a client will set gamePaused at true
		if (gamePaused)
		{
			// Actually, don't care, just leave the game stage since the game will not exist longer
			gamePaused = false;
			main.setStage(new HomeStage(main));
		}
	}

	@Override
	public void draw()
	{
		this.getBatch().begin();
		this.getBatch().draw(background, 0, 0);
		this.getBatch().end();
		super.draw();
	}
	/*
	 * Methods
	 */
	/**
	 * Kangaroos init
	 * @param p
	 */
	private void initKangaroos(UpdateKangarooPacket pPlayer, UpdateKangarooPacket pOpponent)
	{
		player = new Kangaroo(pPlayer);
		opponent = new Kangaroo(pOpponent);
		
		playerName = new Label(player.getName(), main.skin);
		opponentName = new Label(opponent.getName(), main.skin);
		
		// Determine which one need to be flipped
		if (player.getSprite().getX() > opponent.getSprite().getX())
		{
			player.flip();
			playerName.setPosition(this.getWidth() - playerName.getWidth() - 5, this.getHeight() - playerName.getHeight() - 5);
			opponentName.setPosition(5, this.getHeight() - opponentName.getHeight() - 5);
		}
		else
		{
			opponent.flip();
			playerName.setPosition(5, this.getHeight() - playerName.getHeight() - 5);
			opponentName.setPosition(this.getWidth() - opponentName.getWidth() - 5, this.getHeight() - opponentName.getHeight() - 5);
		}
		
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
		main.network.send(player.getUpdatePacket());
		player.networkImage = player.getUpdatePacket();
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
}
