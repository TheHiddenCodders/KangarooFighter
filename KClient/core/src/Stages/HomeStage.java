package Stages;

import Class.ColoredLabel;
import Class.ConnectedStage;
import Packets.HomePacket;
import Packets.MatchMakingPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.genesys.kclient.Main;

public class HomeStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private HomePacket homePacket;
	
	
	/*
	 * Components
	 */
	
	private TextButton matchMakingLaunch;
	private Image background;
	private Image bottomRibbon;
	private ColoredLabel coloredLabel;
	
	private boolean seekingGame = false;
	
	/*
	private PersoBloc persoBloc;
	private LadderBloc ladderBloc;
	private FriendsBloc friendsBloc;
	private NewsBloc lastNewsBloc, lastBeforeNewsBloc;
	*/
	
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
		matchMakingLaunch = new TextButton("JOUER", main.skin);
		matchMakingLaunch.setSize(150, 40);
		matchMakingLaunch.setColor(Color.TAN);
		matchMakingLaunch.setPosition(this.getWidth() / 2 - matchMakingLaunch.getWidth() / 2, this.getHeight() - matchMakingLaunch.getHeight() - 5);
	
		// Bottom ribbon
		bottomRibbon = new Image(new Texture(Gdx.files.internal("sprites/homestage/botribbon.png")));
	}
	
	@Override
	protected void initDataNeededComponents()
	{
		
	}

	@Override
	protected void addListeners()
	{
		matchMakingLaunch.addListener(new ClickListener()
		{
			// Call when the matchMakingLaunch is clicked
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (!seekingGame)
					launchMatchMaking();
				else
					cancelMatchMaking();
			}

			private void cancelMatchMaking() 
			{
				// Change the button to prevent player
				matchMakingLaunch.setColor(Color.TAN);
				matchMakingLaunch.setText("Jouer");
				seekingGame = false;
				
				// Send a packet to server
				MatchMakingPacket packet = new MatchMakingPacket();
				packet.search = false;
				main.network.send(packet);
			}

			private void launchMatchMaking() 
			{
				// Change the button to prevent player
				matchMakingLaunch.setColor(Color.RED);
				matchMakingLaunch.setText("Recherche..");
				seekingGame = true;
				
				// Send a packet to server
				MatchMakingPacket packet = new MatchMakingPacket();
				packet.search = true;
				main.network.send(packet);
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
}
