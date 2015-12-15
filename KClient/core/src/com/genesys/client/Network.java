package com.genesys.client;

import java.io.IOException;
import java.net.UnknownHostException;

import Packets.ClientDataPacket;
import Packets.ClientDisconnectionPacket;
import Packets.EndGamePacket;
import Packets.GameFoundPacket;
import Packets.GameReadyPacket;
import Packets.HeartBeatPacket;
import Packets.KangarooServerPacket;
import Packets.LadderDataPacket;
import Packets.LoginPacket;
import Packets.ServerInfoPacket;
import Packets.SignOutPacket;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.genesys.stages.ConnexionStage;
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
			if (currentStage.getClass().isAssignableFrom(ConnexionStage.class))
			{
				ConnexionStage stage = (ConnexionStage) currentStage;
				
				LoginPacket packet = (LoginPacket) o;
				
				// If the server accepted the request
				if (packet.accepted)
					stage.loggedIn(); // Log in
				else
					stage.notLoggedIn(packet.pwdMatch, packet.pseudoExists); // LogIn fail
			}
			else
			{
				System.err.println("The LOGINPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on InscriptionStage.");
			}
		}
		
		/**
		 * Received a signout packet
		 * 1. Check if pseudo is available on server
		 * 2. If it is, log the client
		 */
		else if (o.getClass().isAssignableFrom(SignOutPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(InscriptionStage.class))
			{
				InscriptionStage stage = (InscriptionStage) currentStage;
				
				SignOutPacket packet = (SignOutPacket) o;
				
				// If the server accepted the request
				if (packet.accepted)
					stage.signedOut(); // Signed out
				else
					stage.notSignedOut(packet.pseudoExists); // SignOut failed
			}
			else
			{
				System.err.println("The SIGNOUTPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on InscriptionStage.");
			}
		}
		
		/**
		 * Received a ClientData packet
		 */
		else if (o.getClass().isAssignableFrom(ClientDataPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(InscriptionStage.class))
			{
				InscriptionStage stage = (InscriptionStage) currentStage;
				
				ClientDataPacket packet = (ClientDataPacket) o;
				
				stage.setClientData(packet);				
			}
			else if (currentStage.getClass().isAssignableFrom(ConnexionStage.class))
			{
				ConnexionStage stage = (ConnexionStage) currentStage;
				
				ClientDataPacket packet = (ClientDataPacket) o;
				
				stage.setClientData(packet);				
			}
			else if (currentStage.getClass().isAssignableFrom(GameStage.class))
			{
				GameStage stage = (GameStage) currentStage;
				
				ClientDataPacket packet = (ClientDataPacket) o;
				
				if (o2 != null)
				{
					ClientDataPacket packet2 = (ClientDataPacket) o2;
					
					stage.setClientsData(packet2, packet);
					o2 = null;
				}
				else
				{
					o2 = packet;
				}				
			}
			else
			{
				System.err.println("The CLIENTDATAPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on InscriptionStage.");
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
				
				ServerInfoPacket packet = (ServerInfoPacket) o;
				
				// Update the server info for the stage
				stage.updateServerInfos(packet);
			}
			else
			{
				System.err.println("The SERVERINFOPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on HomeStage");
			}
		}
		
		/**
		 * Received a game found packet
		 * 1. Set GameStage
		 */
		else if (o.getClass().isAssignableFrom(GameFoundPacket.class))
		{
			GameFoundPacket p = (GameFoundPacket) o;
			
			if (currentStage.getClass().isAssignableFrom(HomeStage.class))
			{
				HomeStage stage = (HomeStage) currentStage;
				stage.setGameFound(p);
			}
			else
			{
				System.err.println("The GAMEFOUNDPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on HomeStage");
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
		else if (o.getClass().isAssignableFrom(KangarooServerPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(HomeStage.class))
			{
				HomeStage stage = (HomeStage) currentStage;
				KangarooServerPacket packet = (KangarooServerPacket) o;
				
				if (o2 != null)
				{
					KangarooServerPacket packet2 = (KangarooServerPacket) o2;
					
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
				KangarooServerPacket packet = (KangarooServerPacket) o;
				
				stage.getKangarooFromIp( packet.ip ).updateFromPacket(packet);
			}
			else
			{
				System.err.println("The KANGAROOSERVERPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on HomeStage or GameStage");
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
				System.err.println("The GAMEREADYPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on GameStage");
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
				
				System.err.println(stage.getKangarooFromIp(packet.disconnectedClientIp).getName() + " [" + packet.disconnectedClientIp + "] " + "has left the game");
				stage.setGamePaused();
			}
			else
			{
				System.err.println("The CLIENTDISCONNECTIONPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on GameStage");
			}
		}
		
		/**
		 * Received an end game packet
		 */
		else if (o.getClass().isAssignableFrom(EndGamePacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(GameStage.class))
			{
				GameStage stage = (GameStage) currentStage;
				stage.setGameEnded((EndGamePacket) o);
			}
			else
			{
				System.err.println("The ENDGAMEPACKET isn't handled on this stage: " + currentStage.getClass().getSimpleName() + " but on GameStage");
			}
		}	
		
		/**
		 * Received a ladder data packet
		 */
		else if (o.getClass().isAssignableFrom(LadderDataPacket.class))
		{
			if (currentStage.getClass().isAssignableFrom(InscriptionStage.class))
			{
				InscriptionStage stage = (InscriptionStage) currentStage;
				
				LadderDataPacket packet = (LadderDataPacket) o;
				
				stage.setLadderData(packet);				
			}
			else if (currentStage.getClass().isAssignableFrom(ConnexionStage.class))
			{
				ConnexionStage stage = (ConnexionStage) currentStage;
				
				LadderDataPacket packet = (LadderDataPacket) o;
				
				stage.setLadderData(packet);				
			}
			else if (currentStage.getClass().isAssignableFrom(GameStage.class))
			{
				GameStage stage = (GameStage) currentStage;
				
				LadderDataPacket packet = (LadderDataPacket) o;
				
				stage.setLadderData(packet);				
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
