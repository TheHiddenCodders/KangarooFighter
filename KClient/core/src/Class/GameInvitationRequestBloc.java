package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import Packets.GameInvitationRequestPacket;
import Stages.HomeStage;

public class GameInvitationRequestBloc extends NotificationBloc
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public GameInvitationRequestBloc(HomeStage homeStage, GameInvitationRequestPacket packet) 
	{
		super(homeStage);
		
		setMsg("Invitation a une partie de <c0>" + packet.message.split(" ")[0] + "</>", Color.TAN);
		setIcone(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/gameinvitationrequest.png")));
	}
	
	/*
	 * Methods
	 */
}
