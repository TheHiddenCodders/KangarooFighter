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
	private boolean clientReady = false; // client have load the stage
	private boolean gameReady = false; // Both client have load the stage
	
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
		
		playerName = new Label(player.getName(), main.skin);
		playerName.setPosition(5, this.getHeight() - playerName.getHeight() - 5);
		opponentName = new Label(opponent.getName(), main.skin);
		opponentName.setPosition(this.getWidth() - opponentName.getWidth() - 5, this.getHeight() - opponentName.getHeight() - 5);
		
		this.addActor(playerName);
		this.addActor(opponentName);
	}
	
	@Override
	public void act(float delta) 
	{			
		// If player & opponent loaded, client is ready
		if (player != null && opponent != null && !clientReady)
			setClientReady();
		
		// If both clients are ready, the game is ready
		if (gameReady)
		{
			// TODO
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
		
		// Determine which one need to be flipped
		if (player.getSprite().getX() > opponent.getSprite().getX())
			player.flip();
		else
			opponent.flip();
		
		this.addActor(player);
		this.addActor(opponent);
	}
	
	/*
	 * Getters & Setters
	 */
	
	private void setClientReady()
	{
		clientReady = true;
		
		ClientReadyPacket p = new ClientReadyPacket();
		p.ip = main.network.getIp();
		
		main.network.send(p);
	}	
	
	public void setGameReady()
	{
		gameReady = true;
		System.out.println("Game start!");
	}	
}
