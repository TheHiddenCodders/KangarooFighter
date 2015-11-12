package com.genesys.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.genesys.kclient.Main;

import Packets.MatchMakingPacket;
import Packets.ServerInfoPacket;
import Packets.UpdateKangarooPacket;

public class HomeStage extends Stage
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	private ServerInfoPacket updateServerInfoPacket;
	private UpdateKangarooPacket pPlayer, pOpponent;
	private boolean seekingGame = false, gameFound = false;
	
	// Components
	private Label name;
	private TextButton matchMakingLaunch;
	
	private Label nKangaroosRegisteredText, nKangaroosRegisteredValue;
	private Label nKangaroosOnlineText, nKangaroosOnlineValue;
	private Label nGamesPlayedText, nGamesPlayedValue;
	private Label nGamesOnlineText, nGamesOnlineValue;
	
	/*
	 * Constructors
	 */
	public HomeStage(Main main)
	{
		super();
		this.main = main;	
		
		name = new Label(main.prefs.getString("[pseudo]"), main.skin);
		name.setColor(Color.ORANGE);
		name.setPosition(5, this.getHeight() - name.getHeight() - 5);
		this.addActor(name);
		
		matchMakingLaunch = new TextButton("Jouer", main.skin);
		matchMakingLaunch.setSize(100, 40);
		matchMakingLaunch.setColor(Color.ORANGE);
		matchMakingLaunch.setPosition(this.getWidth() / 2 - matchMakingLaunch.getWidth() / 2, this.getHeight() - matchMakingLaunch.getHeight() - 10);
		this.addActor(matchMakingLaunch);
		
		nKangaroosRegisteredText = new Label("Kangourous: ", main.skin);
		nKangaroosRegisteredText.setPosition(0, 0);
		nKangaroosRegisteredValue = new Label("", main.skin);
		nKangaroosRegisteredValue.setColor(Color.ORANGE);
		nKangaroosRegisteredValue.setPosition(nKangaroosRegisteredText.getWidth(), 0);
		this.addActor(nKangaroosRegisteredText);
		this.addActor(nKangaroosRegisteredValue);
		
		nKangaroosOnlineText = new Label(" | En ligne: ", main.skin);
		nKangaroosOnlineText.setPosition(nKangaroosRegisteredValue.getX() + nKangaroosRegisteredValue.getWidth(), 0);
		nKangaroosOnlineValue = new Label("", main.skin);
		nKangaroosOnlineValue.setColor(Color.ORANGE);
		nKangaroosOnlineValue.setPosition(nKangaroosOnlineText.getX() + nKangaroosOnlineText.getWidth(), 0);
		this.addActor(nKangaroosOnlineText);
		this.addActor(nKangaroosOnlineValue);
		
		nGamesPlayedText = new Label(" | Parties: ", main.skin);
		nGamesPlayedText.setPosition(nKangaroosOnlineValue.getX() + nKangaroosOnlineValue.getWidth(), 0);
		nGamesPlayedValue = new Label("", main.skin);
		nGamesPlayedValue.setColor(Color.ORANGE);
		nGamesPlayedValue.setPosition(nGamesPlayedText.getX() + nGamesPlayedText.getWidth(), 0);
		this.addActor(nGamesPlayedText);
		this.addActor(nGamesPlayedValue);
		
		nGamesOnlineText = new Label(" | En cours: ", main.skin);
		nGamesOnlineText.setPosition(nGamesPlayedValue.getX() + nGamesPlayedValue.getWidth(), 0);
		nGamesOnlineValue = new Label("", main.skin);
		nGamesOnlineValue.setColor(Color.ORANGE);
		nGamesOnlineValue.setPosition(nGamesOnlineText.getX() + nGamesOnlineText.getWidth(), 0);
		this.addActor(nGamesOnlineText);
		this.addActor(nGamesOnlineValue);
		
		// Ask for server info
		askServerInfos();

	}
	
	@Override
	public void act(float delta)
	{			
		if (updateServerInfoPacket != null)
			updateUIServerInfos(updateServerInfoPacket);
			
		if (gameFound && pPlayer != null && pOpponent != null)
			main.setStage(new GameStage(main, pPlayer, pOpponent));
		
		// Input update
		if (Gdx.input.justTouched())
		{
			if (matchMakingLaunch.isPressed())
			{
				if (!seekingGame)
					launchMatchMaking();
				else
					cancelMatchMaking();
			}
		}
		super.act(delta);
	}
	
	/*
	 * Inherited methods
	 */
	/**
	 * Launch the matchmaking process, including UI changes
	 */
	private void launchMatchMaking()
	{
		// UI change
		matchMakingLaunch.setColor(Color.RED);
		matchMakingLaunch.setText("Recherche..");
		seekingGame = true;
		
		MatchMakingPacket packet = new MatchMakingPacket();
		packet.search = true;
		main.network.send(packet);
	}
	
	/**
	 * Cancel the matchmaking process, UI is reset
	 */
	private void cancelMatchMaking()
	{
		// UI change
		matchMakingLaunch.setColor(Color.ORANGE);
		matchMakingLaunch.setText("Jouer");
		seekingGame = false;
		
		MatchMakingPacket packet = new MatchMakingPacket();
		packet.search = false;
		main.network.send(packet);
	}
	
	/**
	 * Send an empty ServeurInfoPacket, it will be filled by the server and returned to the client
	 */
	private void askServerInfos()
	{
		ServerInfoPacket packet = new ServerInfoPacket();
		main.network.send(packet);
	}
	
	/**
	 * Update the UI linked to the server info
	 * @param p the server info packet
	 */
	private void updateUIServerInfos(ServerInfoPacket p)
	{
		nGamesOnlineValue.setText(String.valueOf(p.nGamesOnline));
		nGamesOnlineValue.pack();
		nGamesPlayedValue.setText(String.valueOf(p.nGamesPlayed));
		nGamesPlayedValue.pack();
		nKangaroosRegisteredValue.setText(String.valueOf(p.nKangaroosRegistered));
		nKangaroosRegisteredValue.pack();
		nKangaroosOnlineValue.setText(String.valueOf(p.nKangaroosOnline));
		nKangaroosOnlineValue.pack();
		
		nKangaroosRegisteredValue.setX(nKangaroosRegisteredText.getWidth());
		nKangaroosOnlineText.setX(nKangaroosRegisteredValue.getX() + nKangaroosRegisteredValue.getWidth());
		nKangaroosOnlineValue.setX(nKangaroosOnlineText.getX() + nKangaroosOnlineText.getWidth());
		nGamesPlayedText.setX(nKangaroosOnlineValue.getX() + nKangaroosOnlineValue.getWidth());
		nGamesPlayedValue.setX(nGamesPlayedText.getX() + nGamesPlayedText.getWidth());
		nGamesOnlineText.setX(nGamesPlayedValue.getX() + nGamesPlayedValue.getWidth());
		nGamesOnlineValue.setX(nGamesOnlineText.getX() + nGamesOnlineText.getWidth());
		
		
		// To avoid build ui again and again
		p = null;
	}
	
	/**
	 * Function called by network, update this updateServerInfoPacket
	 * @param p
	 */
	public void updateServerInfos(ServerInfoPacket p)
	{
		updateServerInfoPacket = p;
	}
	
	public void setGameFound()
	{
		seekingGame = false;
		gameFound = true;
	}
	
	public void setKangaroosInit(UpdateKangarooPacket pPlayer, UpdateKangarooPacket pOpponent)
	{
		this.pPlayer = pPlayer;
		this.pOpponent = pOpponent;
	}
	
}
