package Class;

import Packets.GameInvitationAnswerPacket;
import Packets.GameInvitationRequestPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameInvitationRequestBloc extends NotificationBloc
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public GameInvitationRequestBloc(final HomeStage homeStage, final GameInvitationRequestPacket packet) 
	{
		super(homeStage);
		
		setMsg(packet.message, Color.TAN);
		setIcone(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/gameinvitationrequest.png")));
		setAnswerStyle(packet.style);
		
		addYesListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				homeStage.main.player.getNotifications().remove(packet);
				homeStage.getNotificationsTable().refresh();
				homeStage.getNotificationsDisplay().refresh();
				
				GameInvitationAnswerPacket answer = new GameInvitationAnswerPacket();
				answer.answer = true;
				answer.name = packet.name;
				answer.style = "Ok";
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
				
				GameInvitationAnswerPacket answer = new GameInvitationAnswerPacket();
				answer.answer = false;
				answer.name = packet.name;
				answer.style = "Ok";
				homeStage.main.network.send(answer);
				super.clicked(event, x, y);
			}
		});
	}
	
	/*
	 * Methods
	 */
}
