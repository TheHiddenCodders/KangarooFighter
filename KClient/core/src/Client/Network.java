package Client;

import java.io.IOException;
import java.net.UnknownHostException;

import Class.ConnectedStage;

/**
 * Network handle the data exchanges between client and server.
 * @author Nerisma
 */

public class Network extends Client
{
	/*
	 * Attributes
	 */
	
	private ConnectedStage currentStage;

	private static final boolean debug = true;
	
	/*
	 * Constructor
	 */
	
	public Network(String ip, int port) throws UnknownHostException, IOException
	{
		super(ip, port);
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
		
		currentStage.setServerData(o);
		
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
