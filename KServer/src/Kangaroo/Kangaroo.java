package Kangaroo;

import java.util.ArrayList;

import Packets.KangarooServerPacket;
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
	private int health = 100;
	private int damage = 5;
	private Vector2 position = new Vector2(0, 0);
	private ArrayList<ServerAnimation> animations;
	private int currentAnimation = 0;
	private boolean flip = false;
	private float speed = 200; // In pixel per s
	private States state = States.idle;
	private int	win = 0;
	private boolean ready = false;
	
	private Timer speedTimer;

	
	/**
	 * Create the kangaroo with the client.
	 * 
	 * @param cp
	 */
	public Kangaroo(Direction position)
	{
		this.speedTimer = new Timer();
		
		if (position == Direction.RIGHT)
			flip();
		
		initAnim();
	}
	
	/**
	 * Init the name of the kangaroo.
	 * 
	 * Load animations
	 */
	private void initAnim()
	{
		animations = new ArrayList<ServerAnimation>();
		
		animations.add(new ServerAnimation("assets/anims/idle.hba"));
		animations.add(new ServerAnimation("assets/anims/walk.hba")); // Movement
		animations.add(new ServerAnimation("assets/anims/hit.hba"));
		animations.add(new ServerAnimation("assets/anims/guard.hba")); // Guard 
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba"));
		animations.add(new ServerAnimation("assets/anims/rightpunch.hba"));
		//animations.add(new ServerAnimation("assets/anims/idleGuard.hba"));
		
		animations.get(States.idle.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.movement.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.hit.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.guard.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.leftPunch.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.rightPunch.ordinal()).setMode(ServerAnimation.onePlay);
		//animations.get(States.idleGuard.ordinal()).setMode(ServerAnimation.foreverPlay);
	}
	
	public void flip()
	{
		flip = !flip;
		
		for (int i = 0; i < animations.size(); i++)
			animations.get(i).flip();
	}
	
	/*
	 * Methods
	 */
	
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
		//if (animations.get(currentAnimation).getMode() == ServerAnimation.foreverPlay)
		animations.get(currentAnimation).stop();
		
		currentAnimation = state.ordinal();
	
		animations.get(state.ordinal()).start();
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
				k.setHealth(k.getHealth() - 50);
				break;
			case HEAD:
				k.setHealth(k.getHealth() - 100);
				break;
			case LEFTPUNCH:
				k.setHealth(k.getHealth() - 10);
				this.getCurrentAnimation().stop();
				break;
			case RIGHTPUNCH:
				k.setHealth(k.getHealth() - 20);
				this.getCurrentAnimation().stop();
				break;			
			}
			
			return true;
		}
		
		return false;
	}
	
	/*
	 * Getters - Setters
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

	public void setState(States state)
	{
		// Cancel previous offset
		setPosition(getPosition().x + getAnimation(this.state.ordinal()).getOffset().x, getPosition().y);
		
		this.state = state;
		
		// Add the offset of this new animation
		setPosition(getPosition().x - getAnimation(state.ordinal()).getOffset().x, getPosition().y);
	}
	
	private void setPosition(float x, float y) 
	{
		position.x = x;
		position.y = y;
	}

	public Vector2 getPosition() 
	{
		return position;
	}

	public ServerAnimation getCurrentAnimation()
	{
		return animations.get(currentAnimation);
	}
	
	public ServerAnimation getAnimation(int index)
	{
		return animations.get(index);
	}

	public int getWins() 
	{
		return win;
	}

	public boolean getFlip() 
	{
		return flip;
	}
	
}