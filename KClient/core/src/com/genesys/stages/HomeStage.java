package com.genesys.stages;

import java.util.ArrayList;

import Packets.ClientDataPacket;
import Packets.GameFoundPacket;
import Packets.KangarooServerPacket;
import Packets.MatchMakingPacket;
import Packets.ServerInfoPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.genesys.kclient.Main;
import com.genesys.kclient.PersoBloc;

public class HomeStage extends Stage
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	private ServerInfoPacket updateServerInfoPacket;
	private KangarooServerPacket pPlayer, pOpponent;
	private GameFoundPacket pGameFound;
	private boolean seekingGame = false, gameFound = false;
	private ArrayList<String> bottomInfos;
	private int bottomInfosIndex = 0;
	
	// Components
	private TextButton matchMakingLaunch;
	private Label bottomText;	
	private Image background;
	
	private PersoBloc persoBloc;
	
	/*
	 * Constructors
	 */
	
	public HomeStage(Main main, ClientDataPacket data)
	{
		super();
		this.main = main;	
		
		background = new Image(new Texture(Gdx.files.internal("sprites/homestage.png")));
		this.addActor(background);
		
		matchMakingLaunch = new TextButton("JOUER", main.skin);
		matchMakingLaunch.setSize(150, 40);
		matchMakingLaunch.setColor(Color.TAN);
		matchMakingLaunch.setPosition(this.getWidth() / 2 - matchMakingLaunch.getWidth() / 2, this.getHeight() - matchMakingLaunch.getHeight() - 5);
		this.addActor(matchMakingLaunch);
		
		bottomText = new Label("", main.skin);
		bottomText.setPosition(this.getWidth(), 2);
		bottomText.setColor(Color.WHITE);
		this.addActor(bottomText);		
		
		persoBloc = new PersoBloc(data, main.skin);
		persoBloc.setPosition(this.getWidth() / 2 - persoBloc.getWidth() / 2, this.getHeight() / 2 - persoBloc.getHeight() / 2 - 10);
		this.addActor(persoBloc);
		
		bottomInfos = new ArrayList<String>();
		
		// Ask for server info
		askServerInfos();

	}
	
	@Override
	public void act(float delta)
	{			
		// Update server info if the have been changed
		if (updateServerInfoPacket != null)
			updateUIServerInfos(updateServerInfoPacket);
			
		// If game found, go to game stage
		if (gameFound && pPlayer != null && pOpponent != null)
			main.setStage(new GameStage(main, pGameFound, pPlayer, pOpponent));
		
		// Make bottom text translate and update
		if (bottomText.getX() + bottomText.getWidth() > -50)
		{
			bottomText.moveBy(-1, 0);
		}
		else
		{
			bottomText.setX(this.getWidth());
			
			if (bottomInfosIndex < bottomInfos.size() - 1)
				bottomInfosIndex++;
			else 
				bottomInfosIndex = 0;
			
			bottomText.setText(bottomInfos.get(bottomInfosIndex));
		}
		
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
		matchMakingLaunch.setColor(Color.TAN);
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
		bottomInfos.clear();
		bottomInfos.add("Vous avez en tout joues " + p.nGamesPlayed + " parties de KangarooFighters. Pas mal.");
		bottomInfos.add("En cet instant " + p.nGamesOnline + " parties de KangarooFighters se jouent.");
		bottomInfos.add("Vous etes en tout " + p.nKangaroosRegistered + " combattants Kangourous.");
		bottomInfos.add("Vous etes actuellement " + p.nKangaroosOnline + " combattants Kangourous connectes.");
		
		bottomText.setText(bottomInfos.get(bottomInfosIndex));
		bottomText.pack();
		
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
	
	public void setGameFound(GameFoundPacket p)
	{
		pGameFound = p;
		seekingGame = false;
		gameFound = true;
	}
	
	public void setKangaroosInit(KangarooServerPacket pPlayer, KangarooServerPacket pOpponent)
	{
		this.pPlayer = pPlayer;
		this.pOpponent = pOpponent;
	}
	
}
