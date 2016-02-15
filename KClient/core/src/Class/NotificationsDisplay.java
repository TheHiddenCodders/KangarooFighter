package Class;

import java.util.ArrayList;

import Packets.FriendRequestPacket;
import Packets.GameInvitationRequestPacket;
import Stages.HomeStage;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class NotificationsDisplay extends Table 
{
	/*
	 * Attributes
	 */
	
	private ArrayList<NotificationBloc> notifications;
	
	/*
	 * Constructors
	 */
	
	public NotificationsDisplay(HomeStage homeStage) 
	{
		super();
		
		// Add fade in 
		addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		
		// Load notifications
		notifications = new ArrayList<NotificationBloc>();
		
		for (int i = 0; i < homeStage.main.player.getNotifications().size(); i++)
		{
			if (homeStage.main.player.getNotifications().get(i) instanceof FriendRequestPacket)
				notifications.add(new FriendRequestBloc(homeStage, (FriendRequestPacket) homeStage.main.player.getNotifications().get(i)));
			else if (homeStage.main.player.getNotifications().get(i) instanceof GameInvitationRequestPacket)
				notifications.add(new GameInvitationRequestBloc(homeStage, (GameInvitationRequestPacket) homeStage.main.player.getNotifications().get(i)));
		
			notifications.get(i).setPosition(0, getHeight() - 10 - notifications.get(i).getHeight() - i * (notifications.get(i).getHeight() + 10));
			addActor(notifications.get(i));
		}
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public boolean remove()
	{
		
		return super.remove();
	}
}
