package Server;

import Packets.HomePacket;
import Packets.NewsPacket;

/**
 * This class build a packet in function of the packet received
 * which is given by parameters to the create function.
 * The create function return the given packet filled with the good parameters 
 * @author Sébastien
 *
 */
public class PacketFactory
{
	/**
	 * The create function return the given packet filled with the good parameters 
	 * @param packet
	 * @return same packet filled
	 */
	public static <T> T create(Object packet)
	{
		// Fill packet
		T filledPacket = fillPacket(packet);
		
		return filledPacket;
	}
	
	/**
	 * This method will first find the type of the packet
	 * @param packet 
	 * @return same packet but filled
	 */
	@SuppressWarnings("unchecked")
	private static <T> T fillPacket(Object packet)
	{
		if (packet.getClass().isAssignableFrom(HomePacket.class))
		{
			HomePacket castedPacket = (HomePacket) packet;
			
			return (T) getFilledHomePacket(castedPacket);
		}		
		else
			return null;
	}
	
	/**
	 * Fill a home packet
	 * @param packet
	 * @return filled home packet
	 */
	private static HomePacket getFilledHomePacket(HomePacket packet)
	{
		packet.news = new NewsPacket[2];
		//packet.ladderPlayers = new PlayerPacket[5];
		//packet.serverInfos = new ServerInfoPacket();
		
		return packet;
	}
	
}
