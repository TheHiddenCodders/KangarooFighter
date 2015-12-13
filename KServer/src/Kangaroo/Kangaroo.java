package Kangaroo;

import java.util.ArrayList;

import Packets.ClientDataPacket;
import Packets.KangarooClientPacket;
import Packets.KangarooServerPacket;
import Server.ClientProcessor;
import Utils.ServerUtils;
import Utils.Timer;
import Utils.Vector2;
import enums.BodyPart;
import enums.Direction;
import enums.States;

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
	
	// Client information
	private String name = "";
	private int health;
	private int damage = 5;
	private Vector2 position = new Vector2(0, 0);
	private int currentAnimation = 0;
	private ArrayList<ServerAnimation> animations;
	private States state = States.idle;
	private boolean flip = false;
	
	// For server only
	private Timer speedTimer;
	private float speed = 200; // In pixel per s
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
		lastPacket = new KangarooClientPacket();
		this.cp = cp;
		speedTimer = new Timer();
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
		p.state = state.ordinal();
		return p;
	}
	
	/**
	 * Update kangaroo fields by packets.
	 * @param p the packet received
	 */
	public void updateFromPacket(KangarooClientPacket p)
	{
		this.lastPacket = p;
	}
	
	public void stateMachine(Game game)
	{
		getCurrentAnimation().update();
		
		if (this.getState() != States.movement)
		{
			animations.get(States.movement.ordinal()).stop();
			speedTimer.restart();
		}
		
		// Make the state machine here
		if (this.getState() == States.idle)
		{
			/*
			 *  If the player press the left punch key
			 *  Launch the left punch state
			 */
			if (lastPacket.leftPunchKey)
			{
				this.setState(States.leftPunch);
				this.launchAnimation(States.leftPunch.ordinal());
			}
			/*
			 *  If the player press the right punch key
			 *  Launch the right punch state
			 */
			else if (lastPacket.rightPunchKey)
			{
				this.setState(States.rightPunch);
				this.launchAnimation(States.rightPunch);
			}
			/*
			 *  If the player press the left arrow key
			 *  Launch the movement state
			 */
			else if (lastPacket.leftArrowKey)
			{
				this.setState(States.movement);
				move(Direction.LEFT); 
			}
			/*
			 *  If the player press the right arrow key
			 *  Launch the movement state
			 */
			else if (lastPacket.rightArrowKey)
			{
				this.setState(States.movement);
				move(Direction.RIGHT); 
			}
		}
		
		// If the kangaroo is currently move 
		else if (this.getState() == States.movement)
		{
			/*
			 *  If the player press the left punch key
			 *  Launch the left punch state
			 */
			if (lastPacket.leftPunchKey)
			{
				this.setState(States.leftPunch);
				this.launchAnimation(States.leftPunch);
			}
			/*
			 *  If the player press the right punch key
			 *  Launch the right punch state
			 */
			else if (lastPacket.rightPunchKey)
			{
				this.setState(States.rightPunch);
				this.launchAnimation(States.rightPunch);
			}
			/*
			 *  If the player press the left arrow key
			 *  Move him to the left
			 */
			else if (lastPacket.leftArrowKey)
			{
				move(Direction.LEFT); 
			}
			/*
			 *  If the player press the right arrow key
			 *  Move him to the right
			 */
			else if (lastPacket.rightArrowKey)
			{
				move(Direction.RIGHT); 
			}
			/*
			 *  If the player don't press the left arrow key
			 *  Launch the idle state
			 */
			else if (!lastPacket.leftArrowKey && !lastPacket.rightArrowKey)
			{
				this.setState(States.idle);
			}
		}
		
		// If the kangaroo is currently left punching
		else if (this.getState() == States.leftPunch)
		{
			if (this.getCurrentAnimation().isOver())
			{
				this.setState(States.idle);
				this.launchAnimation(States.idle);
			}
		}
		
		// If the kangaroo is currently right punching
		else if (this.getState() == States.rightPunch)
		{
			if (this.getCurrentAnimation().isOver())
			{
				this.setState(States.idle);
				this.launchAnimation(States.idle);
			}
		}
		
		// If the kangaroo is currently hit
		else if (this.getState() == States.hit)
		{
			if (this.getCurrentAnimation().isOver())
			{
				this.setState(States.idle);
				this.launchAnimation(States.idle);
			}
		}
		
		
	}
	
	/**
	 * Init the name of the kangaroo.
//	 * 
	 * Load animations
	 */
	private void initAnim()
	{
		animations = new ArrayList<ServerAnimation>();
		
		animations.add(new ServerAnimation("assets/anims/idle.hba"));
		animations.add(new ServerAnimation("assets/anims/idle.hba"));
		animations.add(new ServerAnimation("assets/anims/hit.hba"));
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba"));
		animations.add(new ServerAnimation("assets/anims/rightpunch.hba"));
		
		animations.get(0).setMode(ServerAnimation.foreverPlay);
		animations.get(1).setMode(ServerAnimation.foreverPlay);
		animations.get(2).setMode(ServerAnimation.onePlay);
		animations.get(3).setMode(ServerAnimation.onePlay);
		animations.get(4).setMode(ServerAnimation.onePlay);
	}
	
	/**
	 * Launch specified animation
	 * @param index of the animation to launch
	 */
	public void launchAnimation(int index)
	{
		if (animations.get(currentAnimation).getMode() == ServerAnimation.foreverPlay)
			animations.get(currentAnimation).stop();
		
		currentAnimation = index;
		
		animations.get(index).start();
	}
	
	/**
	 * Launch specified animation
	 * @param state of the animation to launch
	 */
	public void launchAnimation(States state)
	{
		if (animations.get(currentAnimation).getMode() == ServerAnimation.foreverPlay)
			animations.get(currentAnimation).stop();
		
		currentAnimation = state.ordinal();
	
		animations.get(state.ordinal()).start();
	}
	
	/**
	 * Set player position according to direction
	 * @param direction
	 */
	private void move(Direction direction)
	{
		if (speedTimer.getElapsedTime() > 0.01)
		{
			if (direction == Direction.LEFT)
			{
				if (!flip)
					flip();
				
				setPosition( (int) getPosition().x - (speed * speedTimer.getElapsedTime()), (int) getPosition().y ); 
			}
			else if (direction == Direction.RIGHT)
			{
				if (flip)
					flip();
				setPosition( (int) getPosition().x + (speed * speedTimer.getElapsedTime()), (int) getPosition().y );
			}
			
			speedTimer.restart();
		}
	}
	
	public void flip()
	{
		flip = !flip;
		for (int i = 0; i < animations.size(); i++)
			animations.get(i).flip();
	}
	
	public boolean collidWith(Kangaroo k)
	{
		if (this.getCurrentAnimation().getKeyFrame().collidWith(k.getCurrentAnimation().getKeyFrame()) != null)
			return true;

		return false;
	}
	
	public boolean punch(Kangaroo k)
	{
		if (this.collidWith(k) && (this.getCurrentAnimation().getKeyFrame().collidWith(k.getCurrentAnimation().getKeyFrame())[0] == BodyPart.LEFTPUNCH || this.getCurrentAnimation().getKeyFrame().collidWith(k.getCurrentAnimation().getKeyFrame())[0] == BodyPart.RIGHTPUNCH) && k.getState() != States.hit && (this.getState() == States.leftPunch || this.getState() == States.rightPunch))
		{
			BodyPart touchedPart = this.getCurrentAnimation().getKeyFrame().collidWith(k.getCurrentAnimation().getKeyFrame())[1];
			
			// Do something
			switch(touchedPart)
			{
			case BODY:
				k.setHealth(k.getHealth() - this.getDamage());
				break;
			case HEAD:
				k.setHealth(k.getHealth() - this.getDamage() * 2);
				break;
			case LEFTPUNCH:
				k.setHealth(k.getHealth() - 10);
				break;
			case RIGHTPUNCH:
				k.setHealth(k.getHealth() - 10);
				break;			
			}
			
			
			return true;
		}
		
		return false;
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
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
	
	public ServerAnimation getAnimation(int index)
	{
		return animations.get(index);
	}
	
	public boolean isSameAsNetwork()
	{
		return (networkImage.damage == this.damage && networkImage.health == this.health && networkImage.state == this.state.ordinal() && networkImage.x == this.getPosition().x && networkImage.y == this.getPosition().y);
	}

	public KangarooServerPacket getNetworkImage() 
	{
		return networkImage;
	}

	public void updateNetworkImage() 
	{
		this.networkImage.damage = this.damage;
		this.networkImage.health = this.health;
		this.networkImage.state = this.state.ordinal();
		this.networkImage.x = this.getPosition().x;
		this.networkImage.y = this.getPosition().y;
	}
	
	public ClientDataPacket getClientDataPacket()
	{
		return ServerUtils.getPlayerDataPacket(this);
	}
	
	public int getGames()
	{
		return ServerUtils.getData(this, "games");
	}
	
	public int getWins()
	{
		return ServerUtils.getData(this, "wins");
	}
	
	public int getLooses()
	{
		return ServerUtils.getData(this, "looses");
	}
	
	public int getElo()
	{
		return ServerUtils.getData(this, "elo");
	}
	
	public int getStreak()
	{
		return ServerUtils.getData(this, "streak");
	}
	
	public void setGames(int value)
	{
		ServerUtils.setData(this, "games", value);
	}
	
	public void setWins(int value)
	{
		ServerUtils.setData(this, "wins", value);
	}
	
	public void setLooses(int value)
	{
		ServerUtils.setData(this, "looses", value);
	}
	
	public void setElo(int value)
	{
		ServerUtils.setData(this, "elo", value);
	}
	
	public void setStreak(int value)
	{
		ServerUtils.setData(this, "streak", value);
	}
	
	public void updateGames(int value)
	{
		ServerUtils.updateData(this, "games", value);
	}
	
	public void updateWins(int value)
	{
		ServerUtils.updateData(this, "wins", value);
	}
	
	public void updateLooses(int value)
	{
		ServerUtils.updateData(this, "looses", value);
	}
	
	public void updateElo(int value)
	{
		ServerUtils.updateData(this, "elo", value);
	}
	
	public void updateStreak(int value)
	{
		ServerUtils.updateData(this, "streak", value);
	}
	
	public Vector2 getPosition() 
	{
		return position;
	}

	public void setPosition(Vector2 position) 
	{
		this.position = position;
		
		for (int i = 0; i < animations.size(); i++)
			animations.get(i).setPosition((int) position.x, (int) position.y);
	}
	
	public void translate(int x, int y)
	{
		this.position.x += x;
		this.position.y += y;
		
		for (int i = 0; i < animations.size(); i++)
			animations.get(i).translate(x, y);
	}
	
	public void setPosition(float x, float y)
	{
		setPosition(new Vector2(x, y));
	}
	
}