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
	private Vector2 velocity = new Vector2(0, 0);
	private float gravity = -98.1f;
	private int currentAnimation = 0;
	private ArrayList<ServerAnimation> animations;
	private States state = States.idle;
	private boolean flip = false;
	
	// For server only
	private Timer speedTimer;
	private Timer punchTimer;
	private float speed = 200; // In pixel per s
	private boolean ready = false;
	
	private KangarooServerPacket networkImage;
	private KangarooClientPacket lastPacket;
	
	// State machine boolean
	private boolean touched;
	
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
		punchTimer = new Timer();
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
		if (getState() == States.idle)
		{
			//Stop the movement animation
			animations.get(States.movement.ordinal()).stop();
			speedTimer.restart();
			
			/*
			 * Jump test
			 */
			if (lastPacket.topArrow)
			{
				velocity.y = 200;
				jump(Direction.LEFT);
				setPosition(getPosition().x, getPosition().y + 10);
				setState(States.jump);
			}
			
			// If the client press the punch key, change state to Punch
			if (lastPacket.punchLeft || lastPacket.punchRight)
			{
				setState(States.punch);
				punchTimer.restart();
			}
			// If the client press the punch top key, change state to punchTop and launch the associate animation
			else if (lastPacket.punchTop)
			{
				setState(States.upperPunch);
				launchAnimation(States.upperPunch);
			}
			// If the guard key is press, change state to guard and launch the associate animation
			else if (lastPacket.guard)
			{
				setState(States.guard);
				launchAnimation(States.guard);
			}
			// If the movement key is press, change state to movement, launch the associate animation and start to move
			else if (lastPacket.leftArrow)
			{
				setState(States.movement);
				launchAnimation(States.movement);
				move(Direction.LEFT); 
			}
			// If the movement key is press, change state to movement, launch the associate animation and start to move
			else if (lastPacket.rightArrow)
			{
				setState(States.movement);
				launchAnimation(States.movement);
				move(Direction.RIGHT); 
			}
			// If the kangaroo is touched
			else if (touched)
			{
				setState(States.hit);
				launchAnimation(States.hit);
			}
		}
		else if (getState() == States.punch)
		{
			// If the delay is over, launch the forward punch
			if (punchTimer.getElapsedTime() >= 0.1)
			{
				setState(States.forwardPunch);
				launchAnimation(States.forwardPunch);
			}
			// If the top arrow key is press, launch top punch
			else if (lastPacket.topArrow)
			{
				setState(States.topPunch);
				launchAnimation(States.topPunch);
			}
			// If the bottom arrow key is press, launch bottom punch
			else if (lastPacket.bottomArrow)
			{
				setState(States.bottomPunch);
				launchAnimation(States.bottomPunch);
			}
			// If the kangaroo is touched, the punch is canceled
			else if (touched)
			{
				setState(States.transitoryState);
				launchAnimation(States.transitoryState);
			}
		}
		else if (getState() == States.topPunch)
		{
			// If the animation is over
			if (this.getCurrentAnimation().isOver())
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
			// If the kangaroo is touched, the punch is canceled
			else if (touched)
			{
				setState(States.transitoryState);
				launchAnimation(States.transitoryState);
			}
		}
		else if (getState() == States.forwardPunch)
		{
			// If the animation is over
			if (this.getCurrentAnimation().isOver())
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
			// If the kangaroo is touched, the punch is canceled
			else if (touched)
			{
				setState(States.transitoryState);
				launchAnimation(States.transitoryState);
			}
		}
		else if (getState() == States.bottomPunch)
		{
			// If the animation is over
			if (this.getCurrentAnimation().isOver())
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
			// If the kangaroo is touched, the punch is canceled
			else if (touched)
			{
				setState(States.transitoryState);
				launchAnimation(States.transitoryState);
			}
		}
		else if (getState() == States.upperPunch)
		{
			// If the animation is over
			if (this.getCurrentAnimation().isOver())
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
			// If the kangaroo is touched, the punch is canceled
			else if (touched)
			{
				setState(States.transitoryState);
				launchAnimation(States.transitoryState);
			}
		}
		else if (getState() == States.hit)
		{
			if (this.getCurrentAnimation().isOver())
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
		}
		else if (getState() == States.guard)
		{
			if (!lastPacket.guard)
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
		}
		else if (getState() == States.movement)
		{
			if (!lastPacket.leftArrow && !lastPacket.rightArrow)
			{
				setState(States.idle);
				launchAnimation(States.idle);
			}
			else if (lastPacket.leftArrow)
			{
				move(Direction.LEFT); 
			}
			else if (lastPacket.rightArrow)
			{
				move(Direction.RIGHT);; 
			}
		}	
		// Jump tets
		else if (getState() == States.jump)
		{
			jump(Direction.LEFT);
			
			if (getPosition().y <= 0)
			{
				setPosition(getPosition().x, 0);
				setState(States.idle);
				launchAnimation(States.idle);
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
		animations.add(new ServerAnimation("assets/anims/idle.hba")); // Movement
		animations.add(new ServerAnimation("assets/anims/hit.hba"));
		animations.add(new ServerAnimation("assets/anims/idle.hba")); // Guard 
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba")); // Forward punch
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba")); // Upper punch
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba")); // Top punch
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba"));
		animations.add(new ServerAnimation("assets/anims/rightpunch.hba"));
		
		System.out.println(States.rightPunch.ordinal());
		animations.get(States.idle.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.movement.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.hit.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.guard.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.forwardPunch.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.upperPunch.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.topPunch.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.leftPunch.ordinal()).setMode(ServerAnimation.onePlay);
		//animations.get(States.rightPunch.ordinal()).setMode(ServerAnimation.onePlay);
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
				
				// Don't go out screen
				if (getPosition().x > 0 - getCurrentAnimation().getKeyFrame().w / 2)
					setPosition( (int) getPosition().x - (speed * speedTimer.getElapsedTime()), (int) getPosition().y ); 
			}
			else if (direction == Direction.RIGHT)
			{
				if (flip)
					flip();
			
				// Don't go out screen
				if (getPosition().x < 800 - getCurrentAnimation().getKeyFrame().w / 2)
					setPosition( (int) getPosition().x + (speed * speedTimer.getElapsedTime()), (int) getPosition().y );
			}
			
			speedTimer.restart();
		}
	}
	
	private void jump(Direction direction)
	{
		if (speedTimer.getElapsedTime() > 0.01)
		{
			if (direction == Direction.LEFT)
			{
				if (!flip)
					flip();
				
				// Don't go out screen
				if (getPosition().x > 0 - getCurrentAnimation().getKeyFrame().w / 2)
				{
					setPosition( (int) getPosition().x, (int) getPosition().y + (velocity.y * speedTimer.getElapsedTime()) ); 
					velocity.y += gravity * speedTimer.getElapsedTime();
				}
					
			}
			else if (direction == Direction.RIGHT)
			{
				if (flip)
					flip();
				
				// Don't go out screen
				if (getPosition().x < 800 - getCurrentAnimation().getKeyFrame().w / 2)
				{
					setPosition( (int) getPosition().x, (int) getPosition().y + (velocity.y * speedTimer.getElapsedTime()) ); 
					velocity.y += gravity * speedTimer.getElapsedTime();
				}
			}
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
		if (this.collidWith(k) && (this.getCurrentAnimation().getKeyFrame().collidWith(k.getCurrentAnimation().getKeyFrame())[0] == BodyPart.LEFTPUNCH || this.getCurrentAnimation().getKeyFrame().collidWith(k.getCurrentAnimation().getKeyFrame())[0] == BodyPart.RIGHTPUNCH) && k.getState() != States.hit && (this.getState() == States.forwardPunch || this.getState() == States.bottomPunch || this.getState() == States.topPunch))
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
				k.setHealth(k.getHealth() - 100);
				break;
			case RIGHTPUNCH:
				k.setHealth(k.getHealth() - 100);
				break;			
			}
			
			return true;
		}
		
		return false;
	}
	
	public void end(Game game)
	{
		if (this.equals(game.getWinner()))
			this.updateWins(1);
		else
			this.updateLooses(1);
		
		this.updateGames(1);
		this.updateElo(game.getEloChange(this));
	}
	
	public int getKCoef()
	{
		if (getGames() <= 30)
			return 40;
		else if (getElo() <= 2900)
			return 20;
		else
			return 10;
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

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
}