package Class;

import Packets.FriendRequestPacket;
import Packets.GameInvitationAnswerPacket;
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
		setIcone(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/friendrequest.png")));
		setAnswerStyle(packet.style);
		
		addYesListener(new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
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
