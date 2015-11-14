package com.genesys.client;

import java.io.IOException;
import java.net.UnknownHostException;

import Packets.ClientDisconnectionPacket;
import Packets.GameFoundPacket;
import Packets.GameReadyPacket;
import Packets.HeartBeatPacket;
import Packets.LoginPacket;
import Packets.ServerInfoPacket;
import Packets.UpdateKangarooPacket;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.genesys.stages.GameStage;
import com.genesys.stages.HomeStage;
import com.genesys.stages.InscriptionStage;

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
	private Object o2; // Buffer object

	private static final boolean debug = true;
	
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
		
		/**
		 * Answer to the heartbeat
		 */
		if (o.getClass().isAssignableFrom(HeartBeatPacket.class))
			answerHeartBeat(o);
		
		/**
		 * Received a login packet
		 * 1. Check if pseudo is available on server
		 * 2. If it is, log the client
		 */
		else if (o.getClass().isAssignableFrom(LoginPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(InscriptionStage.class))
			{
				InscriptionStage stage = (InscriptionStage) currentStage;
				
				// Get login ask answer
				if (o.getClass().isAssignableFrom(LoginPacket.class))
				{
					LoginPacket packet = (LoginPacket) o;
					
					// If the packet accepted the request
					System.err.println("accepted : " + packet.accepted);
					if (packet.accepted)
						stage.loggedIn(); // Log in
					else
						stage.notLoggedIn(); // LogIn fail
				}
			}
			else
			{
				System.err.println("This packet isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on InscriptionStage.");
			}
		}
		
		/**
		 * Received a server info packet (this is a question --> answer packet, the same packet is used to ask and answer)
		 * 1. Transmit the info to the stage
		 */
		else if (o.getClass().isAssignableFrom(ServerInfoPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(HomeStage.class))
			{
				HomeStage stage = (HomeStage) currentStage;
				
				// Get server infos
				if (o.getClass().isAssignableFrom(ServerInfoPacket.class))
				{
					ServerInfoPacket packet = (ServerInfoPacket) o;
					
					// Update the server info for the stage
					stage.updateServerInfos(packet);
				}
			}
			else
			{
				System.err.println("This packet isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on HomeStage");
			}
		}
		
		/**
		 * Received a game found packet
		 * 1. Set GameStage
		 */
		else if (o.getClass().isAssignableFrom(GameFoundPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(HomeStage.class))
			{
				HomeStage stage = (HomeStage) currentStage;
				stage.setGameFound();
			}
			else
			{
				System.err.println("This packet isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on HomeStage");
			}
		}
		
		/**
		 * Received an update kangaroo packet
		 * 1. Receive first kangaroo
		 * 2. Receive second kangaroo
		 * 3.a) If the game hasn't start
		 * -> Init kangaroos
		 * 3.b) Else
		 * -> Update kangaroos 
		 */
		else if (o.getClass().isAssignableFrom(UpdateKangarooPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(HomeStage.class))
			{
				HomeStage stage = (HomeStage) currentStage;
				UpdateKangarooPacket packet = (UpdateKangarooPacket) o;
				
				if (o2 != null)
				{
					UpdateKangarooPacket packet2 = (UpdateKangarooPacket) o2;
					
					stage.setKangaroosInit(packet2, packet);
					o2 = null;
				}
				else
				{
					o2 = packet;
				}
			}
			else if (currentStage.getClass().isAssignableFrom(GameStage.class))
			{
				GameStage stage = (GameStage) currentStage;
				UpdateKangarooPacket packet = (UpdateKangarooPacket) o;
				
				stage.getKangarooFromIp( packet.ip ).updateFromPacket(packet);
			}
			else
			{
				System.err.println("This packet isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on HomeStage or GameStage");
			}
		}
		
		/**
		 * Received a GameReady packet
		 * 1. Set GameReady on game stage
		 */
		else if (o.getClass().isAssignableFrom(GameReadyPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(GameStage.class))
			{
				GameStage stage = (GameStage) currentStage;
				stage.setGameReady();
			}
			else
			{
				System.err.println("This packet isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on GameStage");
			}
		}
		
		/**
		 * Received a Client disconnection packet
		 * 1. Set the client on home stage
		 */
		else if (o.getClass().isAssignableFrom(ClientDisconnectionPacket.class))
		{
			ClientDisconnectionPacket packet = (ClientDisconnectionPacket) o;
			
			if (currentStage.getClass().isAssignableFrom(GameStage.class))
			{
				GameStage stage = (GameStage) currentStage;
				stage.setGameReady();
				
				System.err.println(stage.getKangarooFromIp(packet.disconnectedClientIp).getName() + " [ " + packet.disconnectedClientIp + " ] " + "has left the game");
				stage.setGamePaused();
			}
			else
			{
				System.err.println("This packet isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on GameStage");
			}
		}
		
		else
		{
			System.err.println("This packet isn't handled by Client side: " + o.getClass());
		}		
	}
	
	/*
	 * Inherited methods
	 */
	
	/**
	 * This method send a heartbeatpacket to the server to answer if it's not deconnected
	 */
	public void answerHeartBeat(Object o)
	{
		HeartBeatPacket hb = (HeartBeatPacket) o;
		send(hb);
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
		return this.getLocalSocketAddress().toString().replace("/", "");
	}

}
