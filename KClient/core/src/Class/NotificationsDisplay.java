package Class;

import java.util.ArrayList;

import Packets.FriendRequestPacket;
import Packets.GameInvitationRequestPacket;
import Packets.Notification;
import Stages.HomeStage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NotificationsDisplay extends Table 
{
	/*
	 * Attributes
	 */
	
	private int page = 0;
	private HomeStage homeStage;
	private ArrayList<NotificationBloc> notifications;
	private Label pageInfo;
	private TextButton prev, next;
	
	/*
	 * Constructors
	 */
	
	public NotificationsDisplay(HomeStage homeStage) 
	{
		super();
		
		this.homeStage = homeStage;
		
		// Add fade in 
		addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		
		// Add page label
		pageInfo = new Label(page + 1 + "/" + (int) Math.ceil((homeStage.main.player.getNotifications().size() / 8)), homeStage.main.skin);
		
		// Add browsing buttons
		prev = new TextButton("<", homeStage.main.skin);
		prev.setWidth(60);
		prev.setPosition(200, -380);
		prev.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				page--;
				refresh();
				super.clicked(event, x, y);
			}
		});
		
		next = new TextButton(">", homeStage.main.skin);
		next.setWidth(60);
		next.setPosition(520, -380);
		next.addListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				page++;
				refresh();
				super.clicked(event, x, y);
			}
		});
		
		// Load notifications
		refresh();
	}
	
	/*
	 * Methods
	 */
	
	public void refresh()
	{
		clear();
		
		// Clear notification if it's init
		if (notifications != null)
			notifications.clear();
		
		// Load notifications
		notifications = new ArrayList<NotificationBloc>();
		
		// Page info
		pageInfo.setText(page + 1 + "/" + (int) Math.ceil(homeStage.main.player.getNotifications().size() / 8f));
		pageInfo.setX(390 - pageInfo.getWidth() / 2);
		pageInfo.setY(-380);
		addActor(pageInfo);
		
		// Add browsing buttons
		if (page == 0)
		{
			prev.setText("-");
			prev.setTouchable(Touchable.disabled);
		}
		else
		{
			prev.setText("<");
			prev.setTouchable(Touchable.enabled);
		}
		
		if (page < (int) Math.ceil(homeStage.main.player.getNotifications().size() / 8f) - 1)
		{
			next.setText(">");
			next.setTouchable(Touchable.enabled);
		}
		else
		{
			next.setText("-");
			next.setTouchable(Touchable.disabled);
		}
		
		addActor(prev);
		addActor(next);
		
		System.out.println(homeStage.main.player.getNotifications().size() + " notifications");
		// Load ALL notifications
		for (int i = 0; i < homeStage.main.player.getNotifications().size(); i++)
		{
			// Make corresponding blocs
			if (homeStage.main.player.getNotifications().get(i) instanceof FriendRequestPacket)
				notifications.add(new FriendRequestBloc(homeStage, (FriendRequestPacket) homeStage.main.player.getNotifications().get(i)));
			else if (homeStage.main.player.getNotifications().get(i) instanceof GameInvitationRequestPacket)
				notifications.add(new GameInvitationRequestBloc(homeStage, (GameInvitationRequestPacket) homeStage.main.player.getNotifications().get(i)));
			else if (homeStage.main.player.getNotifications().get(i).getClass().isAssignableFrom(Notification.class))
				notifications.add(new InfoBloc(homeStage, (Notification) homeStage.main.player.getNotifications().get(i)));
		}	
		
		// Place the needed ones
		for (int i = page * 8; i < (page + 1) * 8; i++)
		{
			// If i > notification size, don't do more
			if (i == homeStage.main.player.getNotifications().size())
				break;
			
			// Place notifications side by side
			if (i % 2 == 0)
				notifications.get(i).setPosition(-2, getHeight() - 10 - notifications.get(i).getHeight() - ((i - page * 8) / 2) * (notifications.get(i).getHeight() + 5));
			else
				notifications.get(i).setPosition(392, getHeight() - 10 - notifications.get(i).getHeight() - (int) ((i - page * 8) / 2) * (notifications.get(i).getHeight() + 5));
			
			addActor(notifications.get(i));
		}
	}
}
