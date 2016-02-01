package Stages;

import java.io.IOException;
import java.net.UnknownHostException;

import Client.Main;
import Client.Network;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ServerAccesStage extends Stage
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	protected Main main;
	private boolean connected;
	
	/*
	 * Constructors
	 */
	
	public ServerAccesStage(Main main)
	{
		super();
		this.main = main;	
		
		connected = connect();
	}
	
	@Override
	public void act(float delta)
	{
		if (connected)
			main.setStage(new ConnexionStage(main));
		else
			Gdx.app.exit();	
		
		super.act(delta);
	}
	
	/**
	 * Connect to the server, return true if succeed, false otherwise
	 * @return
	 */
	@SuppressWarnings("static-access")
	private boolean connect()
	{
		try
		{
			String ip = "";
			
			if (Gdx.app.getType().equals(ApplicationType.Desktop))
				ip =  "localhost";
			else
				ip = "192.168.0.8";
			
			main.network = new Network(main, ip, 9999);
			
			Thread t = new Thread(main.network);
			t.start();
			
			// This little time, let the client build himself
			t.sleep(20);

			System.out.println("Connected to the server");
			return true;	
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
