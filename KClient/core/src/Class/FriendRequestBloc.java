package Class;

import Packets.FriendAnswerPacket;
import Packets.FriendRequestPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FriendRequestBloc extends NotificationBloc
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public FriendRequestBloc(final HomeStage homeStage, final FriendRequestPacket packet) 
	{
		super(homeStage);
		
		setMsg(packet.message, Color.TAN);
		setDate(packet.date);
		setIcone(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/friendrequest.png")));
		setAnswerStyle(packet.style);
		
		addYesListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				homeStage.main.player.getNotifications().remove(packet);
				homeStage.getNotificationsTable().refresh();
				homeStage.getNotificationsDisplay().refresh();
				
				FriendAnswerPacket answer = new FriendAnswerPacket();
				answer.answer = true;
				answer.name = packet.senderName;
				homeStage.main.network.send(answer);
				super.clicked(event, x, y);
			}
		});
		
		addNoListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				homeStage.main.player.getNotifications().remove(packet);
				homeStage.getNotificationsTable().refresh();
				homeStage.getNotificationsDisplay().refresh();
				
				FriendAnswerPacket answer = new FriendAnswerPacket();
				answer.answer = false;
				answer.name = packet.senderName;
				homeStage.main.network.send(answer);
				super.clicked(event, x, y);
			}
		});
	}
	
	/*
	 * Methods
	 */
}
