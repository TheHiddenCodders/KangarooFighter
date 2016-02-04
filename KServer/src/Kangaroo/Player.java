package Kangaroo;

import Packets.GameServerPacket;
import Packets.PlayerPacket;
import Utils.ServerUtils;
import enums.Direction;

public class Player 
{
	private String ip;
	private Kangaroo k;
	private PlayerPacket playerPacket;
	
	//private KangarooServerPacket networkImage;
	//private KangarooClientPacket lastPacket;
	
	public Player(PlayerPacket packet)
	{
		packet.name = new String("");
		packet.friends = new PlayerPacket[0];
		
		playerPacket = packet;
	}
	
	public PlayerPacket getPacket()
	{		
		return playerPacket;
	}

	public String getName() 
	{
		return playerPacket.name;
	}

	public void setName(String name) 
	{
		this.playerPacket.name = name;
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
	public GameServerPacket getGamePacket()
	{
		GameServerPacket p = new GameServerPacket();
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
