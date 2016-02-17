package Class;

import Packets.Notification;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InfoBloc extends NotificationBloc
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public InfoBloc(final HomeStage homeStage, final Notification notification) 
	{
		super(homeStage);
		
		setMsg(notification.message, Color.TAN);
		setIcone(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/info.png")));
		setAnswerStyle(notification.style);
		
		addOkListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				homeStage.main.player.getNotifications().remove(notification);
				homeStage.getNotificationsTable().refresh();
				homeStage.getNotificationsDisplay().refresh();
				homeStage.main.network.send(notification);
				super.clicked(event, x, y);
			}
		});	
	}
	
	/*
	 * Methods
	 */
}
