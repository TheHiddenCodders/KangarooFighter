package ServerInfo;

import java.util.ArrayList;

import Kangaroo.GameProcessor;
import Kangaroo.Player;
import Packets.ServerInfoPacket;
import Server.BufferPacket;

public class ServerInfo 
{
	/** sendBuffer : A reference to the main sendBuffer allowing this class to send packets to clients */
	private BufferPacket sendBuffer;
	/** */
	private ServerInfoPacket packet;
	/** connectedPlayers : a reference to the connected players */
	private ArrayList<Player> connectedPlayers;
	
	public ServerInfo(BufferPacket sendBuffer)
	{
		this.sendBuffer = sendBuffer;
		this.packet = new ServerInfoPacket();
	}
	
	/** Update the ServerInfoPacket with the state of the GameProcessor
	 * @param gp : the GameProcessor used in the main thread
	 */
	public void update(GameProcessor gp)
	{
		// TODO : update ServerInfoPacket with GameProcessor
	}
	
	/** Update the ServerInfoPacket with the state of the PlayerProcessor
	 * @param pp : the PlayerProcessor used in the main thread
	 */
	public void update(PlayerProcessor pp)
	{
		// Update the packet
		this.packet.nKangaroosRegistered = pp.howManyRegisteredPlayer();
		this.packet.nKangaroosOnline = pp.howManyConnectedPlayer();
		
		this.connectedPlayers = pp.getConnectedPlayers();
		
		// Browse all connected players to send them the updated packet
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			this.packet.setIp(connectedPlayers.get(i).getIp());
			sendBuffer.sendPacket(this.packet);
		}
		
	}
}
