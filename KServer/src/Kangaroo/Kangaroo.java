package Kangaroo;

import java.util.ArrayList;

import Packets.KangarooClientPacket;
import Packets.KangarooServerPacket;
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
	private Vector2 position = new Vector2(0, 0);
	private int currentAnimation = 0;
	private ArrayList<ServerAnimation> animations;
	private States state;
	
	private boolean ready = false;
	
	private KangarooServerPacket networkImage;
	private KangarooClientPacket lastPacket;
	
	/**
	 * Create the kangaroo with the client.
	 * 
	 * @param cp
	 */
	public Kangaroo(ClientProcessor cp)
	{
		networkImage = new KangarooServerPacket();
		this.lastPacket = new KangarooClientPacket();
		this.cp = cp;
		state = new States();
		
		initAnim();
	}
	
	/*
	 * Methods
	 */
	/**
	 * @return an updatekangaroopacket with this kangaroo data
	 */
	public KangarooServerPacket getUpdatePacket()
	{
		KangarooServerPacket p = new KangarooServerPacket();
		p.ip = cp.getIp();
		p.name = name;
		p.x = position.x;
		p.y = position.y;
		p.health = health;
		p.damage = damage;
		p.state = state.getState();
		return p;
	}
	
	public void updateFromPacket(KangarooClientPacket p)
	{
		this.lastPacket = p;
	}
	
	/**
	 * Update kangaroo fields by packets.
	 * @param p the packet received
	 */
	public void stateMachine()
	{
		// Make the state machine here
		if (this.getState().getState() == States.idle)
		{
			/*
			 *  If the player press the left punch key
			 *  Launch the left punch state
			 */
			if (lastPacket.leftPunchKey)
			{
				this.getState().setState(States.leftPunch);
				this.launchAnimation(States.leftPunch);
			}
			/*
			 *  If the player press the right punch key
			 *  Launch the right punch state
			 */
			else if (lastPacket.rightPunchKey)
			{
				this.getState().setState(States.rightPunch);
			}
			/*
			 *  If the player press the left arrow key
			 *  Launch the movement state
			 */
			else if (lastPacket.leftArrowKey)
			{
				this.getState().setState(States.movement);
				this.setPosition( (int) this.getPosition().x - 1, (int) this.getPosition().y );
			}
			/*
			 *  If the player press the right arrow key
			 *  Launch the movement state
			 */
			else if (lastPacket.rightArrowKey)
			{
				this.getState().setState(States.movement);
				this.setPosition( (int) this.getPosition().x + 1, (int) this.getPosition().y );
			}
			/*else
			{
				if (receivedPacket.x != k.getPosition().x)
				{
					k.getState().setState(States.movement);
					k.setPosition( (int)receivedPacket.x, (int)receivedPacket.y );
				}
			}*/	
		}
		
		// If the kangaroo is currently move 
		else if (this.getState().getState() == States.movement)
		{
			/*
			 *  If the player press the left punch key
			 *  Launch the left punch state
			 */
			if (lastPacket.leftPunchKey)
			{
				this.getState().setState(States.leftPunch);
			}
			/*
			 *  If the player press the right punch key
			 *  Launch the right punch state
			 */
			else if (lastPacket.rightPunchKey)
			{
				this.getState().setState(States.rightPunch);
			}
			/*
			 *  If the player press the left arrow key
			 *  Move him to the left
			 */
			else if (lastPacket.leftArrowKey)
			{
				this.setPosition( (int) this.getPosition().x - 1, (int) this.getPosition().y ); 
			}
			/*
			 *  If the player press the right arrow key
			 *  Move him to the right
			 */
			else if (lastPacket.rightArrowKey)
			{
				this.setPosition( (int) this.getPosition().x + 1, (int) this.getPosition().y ); 
			}
			/*
			 *  If the player don't press the left arrow key
			 *  Launch the idle state
			 */
			else if (!lastPacket.leftArrowKey && !lastPacket.rightArrowKey)
			{
				this.getState().setState(States.idle);
			}
		}
		
		// If the kangaroo is currently left punching
		else if (this.getState().getState() == States.leftPunch)
		{
			if (this.getCurrentAnimation().isOver())
			{
				this.getState().setState(States.idle);
				this.launchAnimation(States.idle);
			}
		}
	}
	
	/**
	 * Load animations
	 */
	private void initAnim()
	{
		animations = new ArrayList<ServerAnimation>();
		
		animations.add(new ServerAnimation("assets/anims/idle.hba"));
		animations.add(new ServerAnimation("assets/anims/hit.hba"));
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba"));
		animations.add(new ServerAnimation("assets/anims/rightpunch.hba"));
		
		animations.get(0).setMode(ServerAnimation.foreverPlay);
		animations.get(1).setMode(ServerAnimation.onePlay);
		animations.get(2).setMode(ServerAnimation.onePlay);
		animations.get(3).setMode(ServerAnimation.onePlay);
	}
	
	/**
	 * Launch specified animation
	 * @param index of the animation to launch
	 */
	private void launchAnimation(int index)
	{
		currentAnimation = index;
		animations.get(index).start(state);
	}
	
	/*
	 * Getters - Setters
	 */
	
	/**
	 * Init the name of the kangaroo.	 * 
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

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}
	
	public ServerAnimation getCurrentAnimation()
	{
		return animations.get(currentAnimation);
	}
	
	public boolean isSameAsNetwork()
	{
		return (networkImage.damage == this.damage && networkImage.health == this.health && networkImage.state == this.state.getState() && networkImage.x == this.getPosition().x && networkImage.y == this.getPosition().y);
	}

	public KangarooServerPacket getNetworkImage() 
	{
		return networkImage;
	}

	public void updateNetworkImage() 
	{
		this.networkImage.damage = this.damage;
		this.networkImage.health = this.health;
		this.networkImage.state = this.state.getState();
		this.networkImage.x = this.getPosition().x;
		this.networkImage.y = this.getPosition().y;
	}
	
	public Vector2 getPosition() 
	{
		return position;
	}

	public void setPosition(Vector2 position) 
	{
		this.position = position;
	}
	
	public void setPosition(float x, float y)
	{
		this.position.x = x;
		this.position.y = y;
	}
	
}