package Stages;

import Class.ColoredLabel;
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
	private Image[] rounds, healths, hits;
	private TextButton replay, home;
	private ColoredLabel eloChange;
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
		
		// To test 
		this.game.getRoundResults()[0].winnerName = game.getKPlayer().getName();
		this.game.getRoundResults()[0].player.health = 11;
		this.game.getRoundResults()[0].player.hits = 12;
		this.game.getRoundResults()[0].opponent.health = 0;
		this.game.getRoundResults()[0].opponent.hits = 15;
		this.game.getRoundResults()[1].winnerName = game.getKPlayer().getName();
		this.game.getRoundResults()[1].player.health = 11;
		this.game.getRoundResults()[1].player.hits = 12;
		this.game.getRoundResults()[1].opponent.health = 0;
		this.game.getRoundResults()[1].opponent.hits = 15;
		this.game.getRoundResults()[2] = null;
		
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
		replay.setSize(100, 35);
		replay.setPosition(800 - replay.getWidth() - 5, 5);
		replay.setColor(Color.TAN);
		
		// Home button
		home = new TextButton("Accueil", main.skin);
		home.setSize(100, 35);
		home.setPosition(5, 5);
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
		
		// Elo change
		if (game.getGameEndedPacket().eloChange > 0)
			eloChange = new ColoredLabel("+" + game.getGameEndedPacket().eloChange + "<c0> elo </>", main.skin, Color.TAN);
		else
			eloChange = new ColoredLabel(game.getGameEndedPacket().eloChange + "<c0> elo </>", main.skin, Color.TAN);
		
		eloChange.setPosition(800 / 2 - eloChange.getWidth() / 2, 310);
		
		// Opponent name
		opponentName = new Label(game.getKOpponent().getName(), main.skin);
		opponentName.setPosition(50, roundDuration.getY() + roundDuration.getHeight() + 10);
		
		// Player name
		playerName = new Label(game.getKPlayer().getName(), main.skin);
		playerName.setColor(Color.TAN);
		playerName.setPosition(50, opponentName.getY() + opponentName.getHeight() + 10);	
				
		// Init Labels depending of round size
		playerHealths = new Label[game.getRoundResults().length];
		opponentHealths = new Label[game.getRoundResults().length];
		playerHits = new Label[game.getRoundResults().length];
		opponentHits = new Label[game.getRoundResults().length];
		roundDurations = new Label[game.getRoundResults().length];
		
		// Init images depending of round size
		rounds = new Image[game.getRoundResults().length];
		healths = new Image[game.getRoundResults().length];
		hits = new Image[game.getRoundResults().length];
		
		
		// Fill labels
		System.err.println("Rounds : " + game.getRoundResults().length);
		for (int i = 0; i < game.getRoundResults().length; i++)		
		{
			if (game.getRoundResults()[i] != null)
			{
				// This means player win the round i
				if (game.getRoundResults()[i].winnerName == main.player.getName())
					rounds[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/roundwin.png")));
				else
					rounds[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/roundlose.png")));
				
				// Place the round image
				rounds[i].setPosition(165 + i * 200, 200);
				
				// Labels
				// Init player health
				playerHealths[i] = new Label(String.valueOf(game.getRoundResults()[i].player.health), main.skin);
				
				// Init opponent health
				opponentHealths[i] = new Label(String.valueOf(game.getRoundResults()[i].opponent.health), main.skin);
				
				// Init player hits
				playerHits[i] = new Label(String.valueOf(game.getRoundResults()[i].player.hits), main.skin);
				
				// Init opponent hits
				opponentHits[i] = new Label(String.valueOf(game.getRoundResults()[i].opponent.hits), main.skin);
				
				// Health and hit images
				// If player health > to opponent health
				if (game.getRoundResults()[i].player.health > game.getRoundResults()[i].opponent.health)
				{
					healths[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/lifewin.png")));
					playerHealths[i].setColor(Color.TAN);
					opponentHealths[i].setColor(Color.LIGHT_GRAY);
				}
				else if (game.getRoundResults()[i].player.health < game.getRoundResults()[i].opponent.health)
				{
					healths[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/lifelose.png")));
					opponentHealths[i].setColor(Color.TAN);
					playerHealths[i].setColor(Color.LIGHT_GRAY);
				}
				else
					healths[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/life.png")));
				
				// Set pos
				healths[i].setPosition(rounds[i].getX(), rounds[i].getY() - healths[i].getHeight() - 10);
				
				// If player hits > to opponent hits
				if (game.getRoundResults()[i].player.hits > game.getRoundResults()[i].opponent.hits)
				{
					hits[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/hitwin.png")));
					playerHits[i].setColor(Color.TAN);
					opponentHits[i].setColor(Color.LIGHT_GRAY);
				}
				else if (game.getRoundResults()[i].player.hits < game.getRoundResults()[i].opponent.hits)
				{
					hits[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/hitlose.png")));
					opponentHits[i].setColor(Color.TAN);
					playerHits[i].setColor(Color.LIGHT_GRAY);
				}
				else
					hits[i] = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/hit.png")));
					
				// Set pos
				hits[i].setPosition(healths[i].getX() + healths[i].getWidth() + 15, rounds[i].getY() - hits[i].getHeight() - 10);
				playerHealths[i].setPosition(healths[i].getX() + healths[i].getWidth() / 2 - playerHealths[i].getWidth() / 2, playerName.getY());
				opponentHealths[i].setPosition(healths[i].getX() + healths[i].getWidth() / 2 - opponentHealths[i].getWidth() / 2, opponentName.getY());
				playerHits[i].setPosition(hits[i].getX() + hits[i].getWidth() / 2 - playerHits[i].getWidth() / 2, playerName.getY());
				opponentHits[i].setPosition(hits[i].getX() + hits[i].getWidth() / 2 - opponentHits[i].getWidth() / 2, opponentName.getY());
				
				// Init round duration
				// TODO : Store round duration in round result packet
				roundDurations[i] = new Label("0:00", main.skin);
				roundDurations[i].setPosition(rounds[i].getX() + rounds[i].getWidth() / 2 - roundDurations[i].getWidth() / 2, roundDuration.getY());
			}
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
		addActor(eloChange);
		
		// Add labels and images
		for (int i = 0; i < game.getRoundResults().length; i++)
		{
			if (game.getRoundResults()[i] != null)
			{
				addActor(rounds[i]);
				addActor(healths[i]);
				addActor(hits[i]);
				addActor(playerHealths[i]);
				addActor(opponentHealths[i]);
				addActor(playerHits[i]);
				addActor(opponentHits[i]);
				addActor(roundDurations[i]);
			}
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
