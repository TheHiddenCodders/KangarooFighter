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
	private int damage = 5;
	//private Vector2 position;
	private States state;
	
	private boolean ready = false;
	
	private UpdateKangarooPacket networkImage;
	
	/**
	 * Create the kangaroo with the client.
	 * 
	 * @param cp
	 */
	public Kangaroo(ClientProcessor cp)
	{
		this.cp = cp;
		this.setState(new States(this));
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
		p.x = state.getPosition().x;
		p.y = state.getPosition().y;
		p.health = health;
		p.damage = damage;
		p.state = state.getState();
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
			state.setPosition(new Vector2(p.x, p.y));
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
		return state.getPosition();
	}

	public void setPosition(Vector2 position) 
	{
		state.setPosition(position);
	}
	
	public void setPosition(int x, int y) 
	{
		state.setPosition( new Vector2(x, y) );
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public UpdateKangarooPacket getNetworkImage() 
	{
		return networkImage;
	}

	public void setNetworkImage(UpdateKangarooPacket networkImage) 
	{
		this.networkImage = networkImage;
	}
	
	
}
