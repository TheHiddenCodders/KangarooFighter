package Class;

import Packets.FriendsPacket;
import Packets.GameInvitationRequestPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FriendActionBloc extends Table 
{
	/*
	 * Attributes
	 */
	
	private HomeStage homeStage;
	private boolean animated = false;
	
	/*
	 * Components
	 */
	
	private Image background;
	private Image gameRequest, del;
	
	/*
	 * Constructors
	 */
	
	public FriendActionBloc(HomeStage homeStage) 
	{
		super();
		
		this.homeStage = homeStage;
		
		// Add fade in
		addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		
		// Set background
		background = new Image(new Texture(Gdx.files.internal("sprites/homestage/displays/friends/actionsframe.png")));
		addActor(background);
		setSize(background.getWidth(), background.getHeight());
		
		// Load buttons
		gameRequest = new Image(new Texture(Gdx.files.internal("sprites/homestage/displays/friends/gameinvitationrequest.png")));
		gameRequest.setPosition(10, 30);
		
		del = new Image(new Texture(Gdx.files.internal("sprites/homestage/displays/friends/deletefriend.png")));
		del.setPosition(getWidth() - del.getWidth() - 15, 30);
		
		// Add buttons
		addActor(gameRequest);
		addActor(del);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta) 
	{
		// Wait for fade out animation to be over before removing the display
		if (getActions().size == 0 && animated)
		{
			super.remove();	
			animated = false;
		}
		
		super.act(delta);
	}
	
	/**
	 * Overrided remove method so display will fade out before beeing removed of stage
	 */
	@Override
	public boolean remove() 
	{
		addAction(Actions.fadeOut(0.5f));
		
		animated = true;
		
		return true;
	}
	
	/**
	 * Set the friend so listeners can be made
	 * @param packet
	 */
	public void setFriend(final FriendsPacket friend)
	{
		gameRequest.clearListeners();
		del.clearListeners();
		
		// Game request listener
		gameRequest.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				GameInvitationRequestPacket packet = new GameInvitationRequestPacket();
				packet.receiverName = friend.name;
				homeStage.main.network.send(packet);
				remove();
				super.clicked(event, x, y);
			}
		});
		
		// Deletre friend listener
		del.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				// TODO
				remove();
				super.clicked(event, x, y);
			}
		});
	}
}
