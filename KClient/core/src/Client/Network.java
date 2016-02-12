package Client;

import java.io.IOException;
import java.net.UnknownHostException;

import Class.ConnectedStage;
import Class.Player;
import Packets.FriendsPacket;
import Packets.PlayerPacket;

/**
 * Network handle the data exchanges between client and server.
 * @author Nerisma
 */

public class Network extends Client
{
	/*
	 * Attributes
	 */
	
	private Main main;
	
	private ConnectedStage currentStage;

	private static final boolean debug = true;
	
	/*
	 * Constructor
	 */
	
	public Network(Main main, String ip, int port) throws UnknownHostException, IOException
	{
		super(ip, port);
		this.main = main;
	}

	/*
	 * Methods
	 */
	
	@Override
	public void onReceived(Object o)
	{
		if (debug)
		{
			System.out.println("While on stage " + currentStage.getClass().getSimpleName());
			System.out.println("Received : " + o.toString());
		}
		
		// If it's a player packet, update player
		if (o.getClass().isAssignableFrom(PlayerPacket.class))
		{			
			if (main.player != null)
			{
				main.player.update((PlayerPacket) o);
				System.out.println("Player updated");
			}
			else
			{
				main.player = new Player((PlayerPacket) o);
				System.out.println("Player created");
			}
		}
		
		if (o.getClass().isAssignableFrom(FriendsPacket.class))
		{
			main.player.updateFriend((FriendsPacket) o);
			System.out.println("Friends updated");
		}

		currentStage.setData(o);
		
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void setStage(ConnectedStage stage)
	{
		currentStage = stage;
	}
	
	public String getIp()
	{ 	
		return this.getLocalSocketAddress().toString().replace("/", "");
	}

}
