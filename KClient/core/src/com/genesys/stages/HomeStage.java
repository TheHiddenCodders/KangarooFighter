package com.genesys.stages;

import java.util.ArrayList;
import java.util.Random;

import Packets.ClientDataPacket;
import Packets.FriendsDataPacket;
import Packets.GamePacket;
import Packets.LadderDataPacket;
import Packets.MatchMakingPacket;
import Packets.NewsPacket;
import Packets.ServerInfoPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.genesys.kclient.ExpandableBloc;
import com.genesys.kclient.FriendsBloc;
import com.genesys.kclient.LadderBloc;
import com.genesys.kclient.Main;
import com.genesys.kclient.NewsBloc;
import com.genesys.kclient.PersoBloc;

public class HomeStage extends Stage
{
	/*
	 * Attributes
	 */
	
	/** Used as a wire between stage to access client for example */
	public Main main;
	private ServerInfoPacket updateServerInfoPacket;
	private GamePacket gamePacket;
	private FriendsDataPacket friendsData;
	private boolean seekingGame = false;
	private ArrayList<String> bottomInfos;
	private int bottomInfosIndex = 0;
	
	/*
	 *  Components
	 */
	
	private TextButton matchMakingLaunch;
	private Label bottomTextBegin, bottomTextVariable, bottomTextEnd;	
	private Image background;
	private Image bottomRibbon;
	
	private PersoBloc persoBloc;
	private LadderBloc ladderBloc;
	private FriendsBloc friendsBloc;
	private NewsBloc lastNewsBloc, lastBeforeNewsBloc;
	
	/*
	 * Constructors
	 */
	
	public HomeStage(Main main, ClientDataPacket clientData, FriendsDataPacket friendsData, LadderDataPacket ladderData, NewsPacket lastNewsData, NewsPacket lastBeforeNewsData)
	{
		super();
		this.main = main;	
		
		// Set opacity to zero
		addAction(Actions.alpha(0));
		
		// Background of the stage
		background = new Image(new Texture(Gdx.files.internal("sprites/homestage.png")));
		this.addActor(background);
		
		// Matchmaking button
		matchMakingLaunch = new TextButton("JOUER", main.skin);
		matchMakingLaunch.setSize(150, 40);
		matchMakingLaunch.setColor(Color.TAN);
		matchMakingLaunch.setPosition(this.getWidth() / 2 - matchMakingLaunch.getWidth() / 2, this.getHeight() - matchMakingLaunch.getHeight() - 5);
		this.addActor(matchMakingLaunch);
		
		// Text translating at the bottom
		bottomTextBegin = new Label("", main.skin);
		bottomTextBegin.setPosition(this.getWidth() - 30, 2);
		bottomTextBegin.setStyle(new LabelStyle(main.skin.getFont("default-font"), Color.WHITE));
		this.addActor(bottomTextBegin);	
		bottomTextVariable = new Label("", main.skin);
		bottomTextVariable.setPosition(bottomTextBegin.getX() + bottomTextBegin.getWidth(), 2);
		bottomTextVariable.setStyle(new LabelStyle(main.skin.getFont("default-font"), Color.TAN));
		this.addActor(bottomTextVariable);		
		bottomTextEnd = new Label("", main.skin);
		bottomTextEnd.setPosition(bottomTextVariable.getX() + bottomTextVariable.getWidth(), 2);
		bottomTextEnd.setStyle(new LabelStyle(main.skin.getFont("default-font"), Color.WHITE));
		this.addActor(bottomTextEnd);
		
		// Perso bloc
		persoBloc = new PersoBloc(clientData, main.skin);
		persoBloc.setPosition(this.getWidth() / 2 - persoBloc.getWidth() / 2, this.getHeight() / 2 - persoBloc.getHeight() / 2 - 10);
		this.addActor(persoBloc);
		
		// Ladder bloc
		ladderBloc = new LadderBloc(clientData, friendsData, ladderData, main.skin);
		ladderBloc.setPosition(this.getWidth() / 4 + this.getWidth() / 2 - 70, this.getHeight() / 2 - 7);
		this.addActor(ladderBloc);
		
		// Friends bloc
		friendsBloc = new FriendsBloc(friendsData, main.skin);
		friendsBloc.setPosition(this.getWidth() / 4 + this.getWidth() / 2 - 70, this.getHeight() / 2 - friendsBloc.getHeight() - 13);
		this.addActor(friendsBloc);
		
		// Last news bloc
		lastNewsBloc = new NewsBloc(lastNewsData, main.skin);
		lastNewsBloc.setPosition(this.getWidth() / 2 - this.getWidth() / 4 - 181, this.getHeight() - this.getHeight() / 3 - 63);
		this.addActor(lastNewsBloc);
		
		// Last before news bloc
		lastBeforeNewsBloc = new NewsBloc(lastBeforeNewsData, main.skin);
		lastBeforeNewsBloc.setPosition(this.getWidth() / 2 - this.getWidth() / 4 - 181, this.getHeight() / 2 - lastBeforeNewsBloc.getHeight() / 2 - 65);
		this.addActor(lastBeforeNewsBloc);
				
		// Bottom infos array
		bottomInfos = new ArrayList<String>();
		
		// Bottom ribbon
		bottomRibbon = new Image(new Texture(Gdx.files.internal("sprites/botribbon.png")));
		this.addActor(bottomRibbon);
		
		// Ask for server info
		askServerInfos();

	}
	
	@Override
	public void act(float delta)
	{			
		// Update server info if the have been changed
		if (updateServerInfoPacket != null)
			updateUIServerInfos(updateServerInfoPacket);
		
		// Update friends if they have been changed
		if (friendsData != null)
		{
			friendsBloc.refresh(friendsData, main.skin);
			friendsData = null;
		}
		
		// If game is not null, a game has been found
		if (gamePacket != null)
			main.setStage(new PreGameStage(main, gamePacket));
		
		// Make bottom text translate and update
		if (bottomTextEnd.getX() + bottomTextEnd.getWidth() > 30)
		{
			bottomTextBegin.moveBy(-2, 0);
			bottomTextVariable.moveBy(-2, 0);
			bottomTextEnd.moveBy(-2, 0);
		}
		else
		{			
			if (bottomInfosIndex < bottomInfos.size() - 1)
				bottomInfosIndex++;
			else 
				bottomInfosIndex = 0;
			
			bottomTextBegin.setText(bottomInfos.get(bottomInfosIndex).split("-")[0]);
			bottomTextVariable.setText(bottomInfos.get(bottomInfosIndex).split("-")[1]);
			bottomTextEnd.setText(bottomInfos.get(bottomInfosIndex).split("-")[2]);
			
			bottomTextBegin.pack();
			bottomTextVariable.pack();
			bottomTextEnd.pack();
			
			bottomTextBegin.setX(this.getWidth() - 30);
			bottomTextVariable.setX(bottomTextBegin.getX() + bottomTextBegin.getWidth());
			bottomTextEnd.setX(bottomTextVariable.getX() + bottomTextVariable.getWidth());
		}
		
		// Input update
		if (Gdx.input.justTouched())
		{
			// Match making
			if (matchMakingLaunch.isPressed())
			{
				if (!seekingGame)
					launchMatchMaking();
				else
					cancelMatchMaking();
			}
			
			// News
			if (lastNewsBloc.isPressed())
			{
				hideBlocsExcept(lastNewsBloc);
				lastNewsBloc.expand();
			}
		}
		
		super.act(delta);
	}
	
	/*
	 * Inherited methods
	 */
	
	/**
	 * Hide all the blocs by a fade out
	 */
	private void hideBlocsExcept(ExpandableBloc bloc)
	{
		persoBloc.addAction(Actions.fadeOut(0.3f));
		ladderBloc.addAction(Actions.fadeOut(0.3f));
		friendsBloc.addAction(Actions.fadeOut(0.3f));
		
		if (bloc != lastNewsBloc)
			lastNewsBloc.addAction(Actions.fadeOut(0.3f));
		
		lastBeforeNewsBloc.addAction(Actions.fadeOut(0.3f));
	}
	
	/**
	 * Show all the blocs by a fade in
	 */
	@SuppressWarnings("unused")
	private void showBlocsExcept(ExpandableBloc bloc)
	{
		persoBloc.addAction(Actions.fadeIn(0.3f));
		ladderBloc.addAction(Actions.fadeIn(0.3f));
		friendsBloc.addAction(Actions.fadeIn(0.3f));
		
		if (bloc != lastNewsBloc)
			lastNewsBloc.addAction(Actions.fadeIn(0.3f));
		
		lastBeforeNewsBloc.addAction(Actions.fadeIn(0.3f));
	}
	
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
		if (bottomInfos.size() == 0)
		{
			bottomInfos.add("Au total -" + p.nGamesPlayed + "- combats de Kangourous ont eu lieus. Pas mal.");
			bottomInfos.add("En ce moment meme -" + p.nGamesOnline + "- combats de Kangourous se jouent.");
			bottomInfos.add("Au total, vous etes -" + p.nKangaroosRegistered + "- combattants Kangourous.");
			bottomInfos.add("En ce moment meme, vous etes -" + p.nKangaroosOnline + "- combattants Kangourous en ligne.");
			
			Random r = new Random(System.currentTimeMillis());
			bottomInfosIndex = r.nextInt(bottomInfos.size());
			
			bottomTextBegin.setText(bottomInfos.get(bottomInfosIndex).split("-")[0]);
			bottomTextVariable.setText(bottomInfos.get(bottomInfosIndex).split("-")[1]);
			bottomTextEnd.setText(bottomInfos.get(bottomInfosIndex).split("-")[2]);
			
			bottomTextBegin.pack();
			bottomTextVariable.pack();
			bottomTextEnd.pack();
			
			bottomTextBegin.setX(this.getWidth());
			bottomTextVariable.setX(bottomTextBegin.getX() + bottomTextBegin.getWidth());
			bottomTextEnd.setX(bottomTextVariable.getX() + bottomTextVariable.getWidth());
		}
		else
		{
			bottomInfos.clear();
			bottomInfos.add("Au total -" + p.nGamesPlayed + "- combats de Kangourous ont eu lieus. Pas mal.");
			bottomInfos.add("En ce moment meme -" + p.nGamesOnline + "- combats de Kangourous se jouent.");
			bottomInfos.add("Au total, vous etes -" + p.nKangaroosRegistered + "- combattants Kangourous.");
			bottomInfos.add("En ce moment meme, vous etes -" + p.nKangaroosOnline + "- combattants Kangourous en ligne.");
			
			bottomTextBegin.setText(bottomInfos.get(bottomInfosIndex).split("-")[0]);
			bottomTextVariable.setText(bottomInfos.get(bottomInfosIndex).split("-")[1]);
			bottomTextEnd.setText(bottomInfos.get(bottomInfosIndex).split("-")[2]);
		}
		
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
	
	/**
	 * Function called by network, a game has been found
	 * @param p
	 */
	public void setGameFound(GamePacket p)
	{
		seekingGame = false;
		gamePacket = p;
	}
	
	/**
	 * Function called by network, update the friends bloc
	 * @param p
	 */
	public void refreshFriendsBloc(FriendsDataPacket p)
	{
		friendsData = p;
	}
}
