package com.genesys.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.genesys.stages.GameStage;
import com.genesys.stages.HomeStage;
import com.genesys.stages.InscriptionStage;

import Packets.GameFoundPacket;
import Packets.LoginPacket;
import Packets.ServerInfoPacket;
import Packets.UpdateKangarooPacket;

/**
 * Network handle the data exchanges between client and server.
 * @author Nerisma
 *
 */

// TODO : Order packet reception by packet type, then by stage instead of stage and then packet type
public class Network extends Client
{
	/*
	 * Attributes
	 */
	private Stage currentStage;
	private static final boolean debug = true;
	private Object o2;
	
	/*
	 * Constructor
	 */
	public Network(String ip, int port) throws UnknownHostException, IOException
	{
		super(ip, port);
	}

	/*
	 * Herited methods
	 */
	@Override
	public void onReceived(Object o)
	{
		if (debug)
		{
			System.out.println("While on stage " + currentStage.getClass().getSimpleName());
			System.out.println("Received : " + o.toString());
		}
		
		if (currentStage.getClass().getSimpleName().equals("InscriptionStage"))
			doInscriptionStageStuff(o);
		else if (currentStage.getClass().getSimpleName().equals("HomeStage"))
			doHomeStageStuff(o);
		else if (currentStage.getClass().getSimpleName().equals("GameStage") && !currentStage.getClass().getSimpleName().equals("InscriptionStage"));
			doGameStageStuff(o);
		
	}
	
	/*
	 * Inherited methods
	 */
	private void doInscriptionStageStuff(Object o)
	{
		InscriptionStage stage = (InscriptionStage) currentStage;
		
		// Get login ask answer
		if (o.getClass().isAssignableFrom(LoginPacket.class))
		{
			LoginPacket packet = (LoginPacket) o;
			
			// If the packet accepted the request
			if (packet.accepted)
				stage.loggedIn(); // Log in
		}
	}
	
	private void doHomeStageStuff(Object o)
	{
		HomeStage stage = (HomeStage) currentStage;
		
		// Get server infos
		if (o.getClass().isAssignableFrom(ServerInfoPacket.class))
		{
			ServerInfoPacket packet = (ServerInfoPacket) o;
			
			// Update the server info for the stage
			stage.updateServerInfos(packet);
		}
		
		if (o.getClass().isAssignableFrom(GameFoundPacket.class))
		{			
			stage.setGameFound();
		}
	}
	
	private void doGameStageStuff(Object o)
	{
		// Update characters (or build them if first packets)
		if (o.getClass().isAssignableFrom(UpdateKangarooPacket.class))
		{
			UpdateKangarooPacket packet = (UpdateKangarooPacket) o;
			
			if (o2 != null)
			{
				UpdateKangarooPacket packet2 = (UpdateKangarooPacket) o2;
				GameStage stage = (GameStage) currentStage;
				stage.initKangaroos(packet2, packet);
				o2 = null;
			}
			else
			{
				o2 = packet;
			}
		}
		
		// Tell game ready
		if (o.getClass().isAssignableFrom(UpdateKangarooPacket.class))
		{
			GameStage stage = (GameStage) currentStage;
			stage.setGameReady();
		}
	}
	/*
	 * Getters & Setters
	 */
	public void setStage(Stage stage)
	{
		currentStage = stage;
	}
	
	public String getIp()
	{ 	
		return this.getLocalSocketAddress().toString().replace("/", "").split(":")[0];
	}

}
