package Kangaroo;

import Packets.UpdateKangarooPacket;
import Server.ClientProcessor;
import Utils.Vector2;

/**
 * The Kangaroo class manage one client. A Kangaroo is create when a client is connecting on the server. 
 * If a client send a request, there is a Kangaroo with this IP.
 * 
 * @author Kurond
 *
 */
public class Kangaroo 
{
	private ClientProcessor cp;
	private String name = "";
	private int health;
	private Vector2 position;
	
	private boolean ready = false;
	
	/**
	 * Create the kangaroo with the client.
	 * 
	 * @param cp
	 */
	public Kangaroo(ClientProcessor cp)
	{
		this.cp = cp;
		position = new Vector2(0, 0);
	}
	
	/*
	 * Methods
	 */
	/**
	 * @return an updatekangaroopacket with this kangaroo data
	 */
	public UpdateKangarooPacket getUpdatePacket()
	{
		UpdateKangarooPacket p = new UpdateKangarooPacket();
		p.ip = cp.getIp();
		p.name = name;
		p.x = position.x;
		p.y = position.y;
		p.health = health;
		return p;
	}
	
	/**
	 * Update kangaroo fields by packets.
	 * @param p the packet received
	 */
	public void updateFromPacket(UpdateKangarooPacket p)
	{
		// Check that packet correspond to the kangaroo by checking names match.
		if (p.name.equals(name))
		{
			position.x = p.x;
			position.y = p.y;
			health = p.health;
		}
	}
	
	/**
	 * Init the name of the kangaroo.
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	
	/*
	 * Getter - Setter
	 */
	public boolean isReady()
	{
		return ready;
	}
	
	public void setReady()
	{
		ready = true;
	}
	
	public void resetReady()
	{
		ready = false;
	}
	
	public ClientProcessor getClient()
	{
		return cp;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}

	public Vector2 getPosition() 
	{
		return position;
	}

	public void setPosition(Vector2 position) 
	{
		this.position = position;
	}
	
	public void setPosition(int x, int y) 
	{
		this.position = new Vector2(x, y);
	}
	
	
}
