package Kangaroo;

import Packets.KangarooPacket;
import Packets.PlayerPacket;

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
	
	public void createKangaroo(int posX, int posY)
	{
		k = new Kangaroo(posX, posY);
	}
	
	/**
	 * @return an updatekangaroopacket with this kangaroo data
	 */
	public KangarooPacket getKangarooPacket()
	{
		return k.kStats;
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
		
		// Store the number of notification
		result = result.concat(playerPacket.notifications.size() + "\n");
		// Store notifications
		for (int i = 0; i < playerPacket.notifications.size(); i++)
		{
			result = result.concat(playerPacket.notifications.get(i).save() + "\n");
		}
		
		return result;
		
	}
}
