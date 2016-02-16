package Kangaroo;

import Packets.GameServerPacket;
import Packets.PlayerPacket;
import enums.Direction;

public class Player 
{
	private Kangaroo k;
	private PlayerPacket playerPacket;
	private String password;
	
	//private KangarooServerPacket networkImage;
	//private KangarooClientPacket lastPacket;
	
	public Player(PlayerPacket packet)
	{
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
		return playerPacket.getIp();
	}

	public void setIp(String ip) 
	{
		playerPacket.setIp(ip);
	}
	
	/**
	 * Return elo (nerisma)
	 * @return
	 */
	public int getElo()
	{
		return playerPacket.elo;
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

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public String toString()
	{
		String result = new String();
		
		// Store player's stats
		result = result.concat("[name]:" + playerPacket.name + "\n");
		result = result.concat("[games]:" + playerPacket.games + "\n");
		result = result.concat("[wins]:" + playerPacket.wins + "\n");
		result = result.concat("[looses]:" + playerPacket.looses + "\n");
		result = result.concat("[elo]:" + playerPacket.elo + "\n");
		result = result.concat("[streak]:" + playerPacket.streak + "\n");
		result = result.concat("[pwd]:" + getPassword() + "\n");
		
		// Store the number of friends
		result = result.concat(playerPacket.friends.size() + "\n");
		
		// Store friends
		for (int i = 0; i < playerPacket.friends.size(); i++)
		{
			result = result.concat(playerPacket.friends.get(i).name + "\n");
		}
		
		System.err.println(result);
		
		return result;
		
	}
}
