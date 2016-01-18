package com.genesys.stages;

import Packets.GamePacket;
import Utils.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.genesys.kclient.Main;

public class PreGameStage extends Stage 
{
	private static final float timeBeforeGame = 8f; // In s
	
	/*
	 * Attributes
	 */
	
	/** Used as a wire between stage to access client for example */
	public Main main;
	private Timer timer;
	private GamePacket gamePacket;
	
	/*
	 *  Components
	 */

	private Image background;
	private Image player, opponent;
	private Image filter;
	private Image fight;
	
	private Label playerName, opponentName;
	private Label playerElo, opponentElo;
	private Label mapName;
	
	/*
	 * Constructors
	 */
	
	public PreGameStage(Main main, GamePacket gamePacket)
	{
		super();
		this.main = main;
		this.gamePacket = gamePacket;
		timer = new Timer();
		
		// Set opacity to zero
		addAction(Actions.alpha(0));
				
		// Init images
		background = new Image(new Texture(Gdx.files.internal(gamePacket.mapPath)));
		filter = new Image(new Texture(Gdx.files.internal("sprites/filter.png")));
		player = new Image(new Texture(Gdx.files.internal("sprites/kangourou.png")));
		player.setPosition(- player.getWidth() * 2 + 50, getHeight() / 2 - player.getHeight() / 2);
		opponent = new Image(new Texture(Gdx.files.internal("sprites/kangourouflipped.png")));
		opponent.setPosition(getWidth() + opponent.getWidth() - 50, getHeight() / 2 - opponent.getHeight() / 2);
		fight = new Image(new Texture(Gdx.files.internal("sprites/fight.png")));	
		fight.setPosition(getWidth() / 2 - fight.getWidth() / 2, getHeight() / 2 - fight.getHeight() / 2);
		
		// Init text
		mapName = new Label("\"" + gamePacket.mapPath.replace("sprites/", "").replace(".png", "") + "\"", new LabelStyle(main.skin.getFont("korean-32"), Color.TAN));
		mapName.setPosition(getWidth() / 2 - mapName.getWidth() / 2, getHeight() - 100);
		playerName = new Label(gamePacket.player.name, new LabelStyle(main.skin.getFont("korean"), new Color(120f/255f, 140f/255f, 237f/255f, 1f)));
		playerName.setPosition(50 + player.getWidth() / 2 - playerName.getWidth() / 2, player.getY() + player.getHeight() + 20);
		opponentName = new Label(gamePacket.opponent.name, new LabelStyle(main.skin.getFont("korean"), new Color(228f/255f, 97f/255f, 97f/255f, 1f)));
		opponentName.setPosition(getWidth() - 50 - opponent.getWidth() / 2 - opponentName.getWidth() / 2, opponent.getY() + opponent.getHeight() + 20);
			
		// Anims 
		mapName.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		player.addAction(Actions.moveBy(player.getWidth() * 2, 0, 2.5f, Interpolation.linear));
		opponent.addAction(Actions.moveBy(- opponent.getWidth() * 2, 0, 2.5f, Interpolation.linear));		
		playerName.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		opponentName.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		
		addActor(background);
		addActor(filter);
		addActor(player);
		addActor(opponent);
		addActor(fight);
		addActor(mapName);
	}
	
	@Override
	public void act(float delta)
	{					
		timer.update();
		
		if (timer.getElapsedTime() > timeBeforeGame * 0.3)
		{
			addActor(playerName);
			addActor(opponentName);
			addActor(playerElo);
			addActor(opponentElo);
		}
			
		if (timer.getElapsedTime() > timeBeforeGame * 0.8)
			addAction(Actions.fadeOut(timeBeforeGame * 0.15f));
		
		if (timer.getElapsedTime() > timeBeforeGame)
			main.setStage(new GameStage(main, gamePacket));
		
		super.act(delta);
	}
}
