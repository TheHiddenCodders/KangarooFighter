package Kangaroo;

import Packets.KangarooServerPacket;
import Packets.PlayerPacket;
import Utils.ServerUtils;
import enums.Direction;

public class Player 
{
	private String ip;
	private String name = "";
	private Kangaroo k;
	//private Stats stats;
	
	//private KangarooServerPacket networkImage;
	//private KangarooClientPacket lastPacket;
	
	public Player()
	{
	}
	
	public PlayerPacket getPacket()
	{
		PlayerPacket packet = new PlayerPacket(ip);
		
		packet.name = name;
		packet.elo = 0; // stats.elo;
		packet.games = 0; // stats.games;
		packet.looses = 0; // stats.looses;
		packet.wins = 0; // stats.games - stats.looses;
		packet.pos = 0; // stats.pos;
		packet.streak = 0; // ?
		packet.friends = new PlayerPacket[1]; // ?
		
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
	
	/**
	 * Return elo (nerisma)
	 * @return
	 */
	public int getElo()
	{
		return ServerUtils.getData(this, "elo");
	}
	
	public void createKangaroo()
	{
		k = new Kangaroo(Direction.RIGHT);
	}
	
	/**
	 * @return an updatekangaroopacket with this kangaroo data
	 */
	public KangarooServerPacket getUpdatePacket()
	{
		KangarooServerPacket p = new KangarooServerPacket();
		p.ip = getIp();
		p.x = k.getPosition().x;
		p.y = k.getPosition().y;
		p.health = k.getHealth();
		p.damage = k.getDamage();
		p.state = k.getState().ordinal();
		p.flip = k.getFlip();
		return p;
	}

	public Kangaroo getKangaroo() 
	{
		return k;
	}
}
