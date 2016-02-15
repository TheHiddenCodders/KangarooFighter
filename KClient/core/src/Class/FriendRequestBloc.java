package Class;

import Packets.FriendRequestPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class FriendRequestBloc extends NotificationBloc
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public FriendRequestBloc(HomeStage homeStage, FriendRequestPacket packet) 
	{
		super(homeStage);
		
		setMsg("Demande d'ajout d'amis de <c0>" + packet.name + "</>", Color.TAN);
		setIcone(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/friendrequest.png")));
	}
	
	/*
	 * Methods
	 */
}
