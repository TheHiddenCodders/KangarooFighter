package Stages;

import Class.ConnectedStage;
import Class.FriendsBloc;
import Class.LadderBloc;
import Class.PersoBloc;
import Client.Main;
import Packets.HomePacket;
import Packets.MatchMakingPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HomeStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private HomePacket homePacket;
	private boolean searchingGame = false;
	
	/*
	 * Components
	 */
	
	private TextButton matchMakingLaunch;
	private Image background;
	private Image bottomRibbon;
	
	
	private PersoBloc persoBloc;
	private LadderBloc ladderBloc;
	private FriendsBloc friendsBloc;
	//private NewsBloc lastNewsBloc, lastBeforeNewsBloc;
	
	
	/*
	 * Constructors
	 */
	
	public HomeStage(Main main) 
	{
		super(main);
		needDataToInit = true;
	}

	@Override
	protected void askInitData() 
	{
		needDataToInit = true;
		
		HomePacket p = new HomePacket();
		main.network.send(p);
	}

	@Override
	protected void initComponents() 
	{		
		// Background of the stage
		background = new Image(new Texture(Gdx.files.internal("sprites/homestage/homestage.png")));
		
		// Matchmaking button
		matchMakingLaunch = new TextButton("Jouer", main.skin);
		matchMakingLaunch.setSize(150, 40);
		matchMakingLaunch.setColor(Color.TAN);
		matchMakingLaunch.setPosition(this.getWidth() / 2 - matchMakingLaunch.getWidth() / 2, this.getHeight() - matchMakingLaunch.getHeight() - 5);
	
		// Bottom ribbon
		bottomRibbon = new Image(new Texture(Gdx.files.internal("sprites/homestage/botribbon.png")));
	}
	
	@Override
	protected void initDataNeededComponents()
	{
		ladderBloc = new LadderBloc(this);
		friendsBloc = new FriendsBloc(this);
		persoBloc = new PersoBloc(this);
	}

	@Override
	protected void addListeners()
	{
		matchMakingLaunch.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (!searchingGame)
					launchMatchMaking();

				else
					cancelMatchMaking();
				
				super.clicked(event, x, y);
			}
		});
	}

	@Override
	protected void addInitDataNeededListeners()
	{
		
	}
	
	@Override
	protected void addAnimations() 
	{
				
	}
	
	@Override
	protected void addInitDataNeededAnimations() 
	{
		
	}

	@Override
	protected void addActors() 
	{
		addActor(background);
		addActor(matchMakingLaunch);
		addActor(bottomRibbon);
	}
	
	@Override
	protected void addInitDataNeededActors()
	{
		addActor(ladderBloc);
		addActor(friendsBloc);
		addActor(persoBloc);
	}
	
	@Override
	protected void onDataReceived() 
	{
		initComponents();
		addListeners();
		addAnimations();
		addActors();
	}

	@Override
	public void setData(Object data)
	{
		if (data.getClass().isAssignableFrom(HomePacket.class))
		{
			HomePacket homePacket = (HomePacket) data;
			
			// Store packet
			this.homePacket = homePacket;
			
			if (main.player != null)
				initDataReceived();
		}
	}

	/**
	 * This method will hide all blocs to draw a display
	 */
	public void hideBlocs() 
	{
		ladderBloc.remove();
		friendsBloc.remove();
	}
	
	/**
	 * This method will show all blocs after exiting a display
	 */
	public void showBlocs()
	{
		addActor(ladderBloc = new LadderBloc(this));
		addActor(friendsBloc = new FriendsBloc(this));
	}
	
	/**
	 * Launch the matchmaking process, including UI changes
	 */
	private void launchMatchMaking()
	{
		// UI change
		matchMakingLaunch.setColor(Color.RED);
		matchMakingLaunch.setText("Recherche..");
		searchingGame = true;
		
		MatchMakingPacket packet = new MatchMakingPacket();
		packet.search = true;
		// TODO: Packet.elotolerance
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
		searchingGame = false;
		
		MatchMakingPacket packet = new MatchMakingPacket();
		packet.search = false;
		// TODO: Packet.elotolerance
		main.network.send(packet);
	}
}
