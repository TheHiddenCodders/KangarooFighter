package Stages;

import Class.ConnectedStage;
import Class.Game;
import Client.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class EndGameStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private Game game;
	
	/*
	 * Components
	 */
	
	private Image header;
	private Image background;
	private TextButton replay, home;
	private Label playerName, opponentName, roundDuration;
	private Label[] playerHealths, opponentHealths, playerHits, opponentHits, roundDurations;

	/*
	 * Constructors
	 */

	public EndGameStage(Main main, Game game) 
	{
		super(main);		
		
		// Store game 
		this.game = game;
		
		initDataReceived();
	}

	/*
	 * Methods
	 */
	
	@Override
	protected void askInitData() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initComponents() 
	{
		// Background
		background = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/background.png")));	
		
		// Round duration
		roundDuration = new Label("Temps", main.skin);
		roundDuration.setPosition(50, 50);
		
		// Replay button
		replay = new TextButton("Rejouer", main.skin);
		replay.setSize(100, 50);
		replay.setPosition(800 - replay.getWidth() - 10, 10);
		replay.setColor(Color.TAN);
		
		// Home button
		home = new TextButton("Accueil", main.skin);
		home.setSize(100, 50);
		home.setPosition(replay.getX() - replay.getWidth() - 10, 10);
	}

	@Override
	protected void initDataNeededComponents() 
	{
		// Load header texture depending of the winner
		Texture headerTexture;
		if (game.getWinnerName().equals(main.player.getName()))
			headerTexture = new Texture(Gdx.files.internal("sprites/endgamestage/victoryheader.png"));
		else
			headerTexture = new Texture(Gdx.files.internal("sprites/endgamestage/loseheader.png"));
			
		header = new Image(headerTexture);
		header.setY(background.getHeight());
		
		// Opponent name
		opponentName = new Label(game.getKOpponent().getName(), main.skin);
		opponentName.setPosition(50, roundDuration.getY() + roundDuration.getHeight() + 10);
		
		// Player name
		playerName = new Label(game.getKPlayer().getName(), main.skin);
		playerName.setPosition(50, opponentName.getY() + opponentName.getHeight() + 10);	
				
		// Init Labels dependng of round size
		playerHealths = new Label[game.getRoundResults().length];
		opponentHealths = new Label[game.getRoundResults().length];
		playerHits = new Label[game.getRoundResults().length];
		opponentHits = new Label[game.getRoundResults().length];
		roundDurations = new Label[game.getRoundResults().length];
		
		// Fill labels
		for (int i = 0; i < game.getRoundResults().length; i++)		
		{
			// TODO : change winners and losers by player and opponent in round result packet
			
			// Determine who is who
			if (game.getWinnerName().equals(main.player.getName()))
			{	
				// Init player health
				playerHealths[i] = new Label(String.valueOf(game.getRoundResults()[i].winner.health), main.skin);
				playerHealths[i].setPosition(200 + 100 * i, playerName.getY());
				
				// Init opponent health
				opponentHealths[i] = new Label(String.valueOf(game.getRoundResults()[i].loser.health), main.skin);
				opponentHealths[i].setPosition(200 + 100 * i, opponentName.getY());
			}
			else
			{
				// Init player health
				playerHealths[i] = new Label(String.valueOf(game.getRoundResults()[i].loser.health), main.skin);
				playerHealths[i].setPosition(200 + 100 * i, playerName.getY());
				
				// Init opponent health
				opponentHealths[i] = new Label(String.valueOf(game.getRoundResults()[i].winner.health), main.skin);
				opponentHealths[i].setPosition(200 + 100 * i, opponentName.getY());
			}
				
			// TODO : Add hits in round results
			
			// Init player hits
			playerHits[i] = new Label("0", main.skin);
			playerHits[i].setPosition(200 + 100 * i, playerName.getY());
			
			// Init opponent hits
			opponentHits[i] = new Label("0", main.skin);
			opponentHits[i].setPosition(200 + 100 * i, opponentName.getY());
			
			// Init round duration
			// TODO : Store round duration in round result packet
			roundDurations[i] = new Label("0:00", main.skin);
			roundDurations[i].setPosition(200 + 100 * i, roundDuration.getY());
		}
	}

	@Override
	protected void addListeners() 
	{
		home.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				main.setStage(new HomeStage(main));
				super.clicked(event, x, y);
			}
		});
		
		replay.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				HomeStage homeStage = new HomeStage(main);
				homeStage.launchMatchMaking();
				main.setStage(homeStage);
				super.clicked(event, x, y);
			}
		});
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
		addActor(roundDuration);
		addActor(replay);
		addActor(home);
	}

	@Override
	protected void addInitDataNeededActors() 
	{
		addActor(header);		
		addActor(playerName);
		addActor(opponentName);
		
		// Add labels
		for (int i = 0; i < playerHealths.length; i++)
		{
			addActor(playerHealths[i]);
			addActor(opponentHealths[i]);
			addActor(roundDurations[i]);
		}
	}

	@Override
	protected void onDataReceived() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setData(Object data)
	{
		// TODO Auto-generated method stub
		
	}
}
