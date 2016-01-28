package Kangaroo;

import Packets.ClientDataPacket;
import Packets.KangarooClientPacket;
import Packets.KangarooServerPacket;
import Server.ClientProcessor;

public class Player 
{
	private String ip;
	private String name = "";
	//private Stats stats;
	
	private KangarooServerPacket networkImage;
	private KangarooClientPacket lastPacket;
	
	public Player()
	{
	}
	
	public ClientDataPacket getPacket()
	{
		ClientDataPacket packet = new ClientDataPacket();
		
		packet.ip = ip;
		packet.name = name;
		packet.elo = 0; // stats.elo;
		packet.games = 0; // stats.games;
		packet.looses = 0; // stats.looses;
		packet.wins = 0; // stats.games - stats.looses;
		packet.pos = 0; // stats.pos;
		packet.streak = 0; // ?

		return packet;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getIp() 
	{
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
