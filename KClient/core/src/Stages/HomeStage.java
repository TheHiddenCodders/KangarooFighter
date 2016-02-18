package Stages;

import BlocsDisplays.FriendsBloc;
import BlocsDisplays.LadderBloc;
import BlocsDisplays.NewsBloc;
import BlocsDisplays.PersoBloc;
import Class.ColoredLabel;
import Class.ConnectedStage;
import Class.NotificationsDisplay;
import Class.NotificationsTable;
import Client.Main;
import Packets.FriendsPacket;
import Packets.HomePacket;
import Packets.InitGamePacket;
import Packets.LadderPacket;
import Packets.MatchMakingPacket;
import Packets.Notification;
import Packets.ServerInfoPacket;

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
	private InitGamePacket gamePacket;
	private LadderPacket ladderPacket;
	private ServerInfoPacket serverInfoPacket;
	
	private boolean searchingGame = false;
	private boolean updateFriends = false;
	private boolean updateNotifications = false;
	
	/*
	 * Components
	 */
	
	private TextButton matchMakingLaunch;
	private Image background;
	private Image bottomRibbon;
	private NotificationsTable notifTable;
	private NotificationsDisplay notifDisplay;
	private ColoredLabel serverInfo;
	
	// Blocs
	private PersoBloc persoBloc;
	private LadderBloc ladderBloc;
	private FriendsBloc friendsBloc;
	private NewsBloc newsBloc, newsBloc2;
	
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
		ladderBloc = new LadderBloc(homePacket.ladder, this);
		friendsBloc = new FriendsBloc(this);
		persoBloc = new PersoBloc(this);
		newsBloc = new NewsBloc(homePacket.news[0], this);
		newsBloc.setPosition(10, 255);
		newsBloc2 = new NewsBloc(homePacket.news[1], this);
		newsBloc2.setPosition(10, 50);
		notifTable = new NotificationsTable(this);
		notifTable.setPosition(getWidth() - notifTable.getWidth() - 10, 5);
		notifDisplay = new NotificationsDisplay(this);
		notifDisplay.setPosition(10, getHeight() - 51);
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
		notifTable.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				// If notif display is on a stage (that's mean it's added)
				if (notifDisplay.getStage() != null)
				{
					// Show blocs
					showBlocs();
					
					// Remove notif display
					notifDisplay.remove();
				}
				else
				{
					// Add notifdisplay
					addActor(notifDisplay = new NotificationsDisplay(HomeStage.this));
					notifDisplay.setPosition(10, getHeight() - 51);
					
					// Hide display if they are on stage
					hideCurrentDisplay();
					
					// Hide blocs
					hideBlocs();
				}
				super.clicked(event, x, y);
			}
		});
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
		addActor(bottomRibbon);
		addActor(matchMakingLaunch);
	}
	
	@Override
	protected void addInitDataNeededActors()
	{
		addActor(ladderBloc);
		addActor(friendsBloc);
		addActor(persoBloc);
		addActor(newsBloc);
		addActor(newsBloc2);
		addActor(notifTable);
	}
	
	@Override
	protected void onDataReceived() 
	{
		if (gamePacket != null)
			main.setStage(new PreGameStage(main, gamePacket));
		
		if (ladderPacket != null)
			ladderBloc.getDisplay().refresh(ladderPacket);
		
		if (updateFriends)
		{
			friendsBloc.refresh(null);
			friendsBloc.getDisplay().refresh(null);
			updateFriends = false;
		}
		
		if (updateNotifications)
		{
			notifTable.refresh();
			notifDisplay.refresh();
			updateNotifications = false;
		}
	}

	@Override
	public void setData(Object data)
	{
		if (data.getClass().isAssignableFrom(HomePacket.class))
		{			
			// Store packet
			this.homePacket = (HomePacket) data;
			System.out.println("HomePacket received");
			
			if (main.player != null)
				initDataReceived();
		}
		
		if (data.getClass().isAssignableFrom(InitGamePacket.class))
		{			
			// Store packet
			this.gamePacket = (InitGamePacket) data;
			System.out.println("InitGamePacket received");
			dataReceived();
		}
		
		if (data.getClass().isAssignableFrom(LadderPacket.class))
		{
			// Store packet
			this.ladderPacket = (LadderPacket) data;
			System.out.println("LadderPacket received");			
			dataReceived();
		}
		
		if (data.getClass().isAssignableFrom(FriendsPacket.class))
		{
			// Fire trigger
			updateFriends = true;
			System.out.println("FriendsPacket received");	
			dataReceived();
		}
		
		if (data instanceof Notification)
		{
			// Fire trigger
			updateNotifications = true;
			System.out.println("Notification received");	
			dataReceived();
		}
		
		if (data instanceof ServerInfoPacket)
		{
			// Store packet
			this.serverInfoPacket = (ServerInfoPacket) data;
			System.out.println("ServerInfoPacket receveid");
			dataReceived();
		}
	}

	/**
	 * This method will hide the current display if there is one open
	 */
	public void hideCurrentDisplay()
	{
		if (ladderBloc.getDisplay().getStage() != null)
		{
			System.out.println("Ladder bloc is on stage");
			ladderBloc.getDisplay().remove();
			return;
		}
		else if (friendsBloc.getDisplay().getStage() != null)
		{
			System.out.println("Friends bloc is on stage");
			friendsBloc.getDisplay().remove();
			return;
		}
		else if (newsBloc.getDisplay().getStage() != null)
		{
			System.out.println("News bloc is on stage");
			newsBloc.getDisplay().remove();
			return;
		}
		else if (newsBloc2.getDisplay().getStage() != null)
		{
			System.out.println("News bloc 2 is on stage");
			newsBloc2.getDisplay().remove();
			return;
		}
	}
	
	/**
	 * This method will hide all blocs to draw a display
	 */
	public void hideBlocs() 
	{
		ladderBloc.remove();
		friendsBloc.remove();
		persoBloc.remove();
		newsBloc.remove();
		newsBloc2.remove();
	}
	
	/**
	 * This method will show all blocs after exiting a display
	 */
	public void showBlocs()
	{
		addActor(ladderBloc = new LadderBloc(homePacket.ladder, this));
		addActor(friendsBloc = new FriendsBloc(this));
		addActor(persoBloc = new PersoBloc(this));
		addActor(newsBloc = new NewsBloc(homePacket.news[0], this));
		newsBloc.setPosition(10, 255);
		addActor(newsBloc2 = new NewsBloc(homePacket.news[1], this));
		newsBloc2.setPosition(10, 50);
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
		packet.eloTolerance = 0; 
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
		main.network.send(packet);
	}
	
	public Image getBottomRibbon()
	{
		return bottomRibbon;
	}
	
	public NotificationsTable getNotificationsTable()
	{
		return notifTable;
	}
	
	public NotificationsDisplay getNotificationsDisplay()
	{
		return notifDisplay;
	}
}
