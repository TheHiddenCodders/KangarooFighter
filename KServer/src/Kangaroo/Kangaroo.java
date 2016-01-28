package Kangaroo;

import java.util.ArrayList;

import Packets.ClientDataPacket;
import Packets.FriendsDataPacket;
import Packets.KangarooClientPacket;
import Packets.KangarooServerPacket;
import Packets.LadderDataPacket;
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
	private Player player;
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
	public Kangaroo(Player player, Direction position)
	{
		this.speedTimer = new Timer();
		this.player = player;
		
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
	 * @return an updatekangaroopacket with this kangaroo data
	 */
	public KangarooServerPacket getUpdatePacket()
	{
		KangarooServerPacket p = new KangarooServerPacket();
		p.ip = player.getIp();
		p.x = position.x;
		p.y = position.y;
		p.health = health;
		p.damage = damage;
		p.state = state.ordinal();
		p.flip = flip;
		return p;
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
		//if (animations.get(currentAnimation).getMode() == ServerAnimation.foreverPlay)
		animations.get(currentAnimation).stop();
		
		currentAnimation = state.ordinal();
	
		animations.get(state.ordinal()).start();
	}
	
	/**
	 * Set player position according to direction
	 * @param direction
	 */
	private void move(Direction direction, Game game)
	{
		Kangaroo opponent = game.getKangarooFromOpponentIp(this.getClient().getIp());
		
		if (speedTimer.getElapsedTime() > 0.01)
		{
			if (direction == Direction.LEFT)
			{
				if (!flip)
					flip();
				
				// Don't go out screen
				if (getPosition().x > 0 - getCurrentAnimation().getKeyFrame().w / 2 && !this.collidWith(opponent))
					setPosition( (int) getPosition().x - (speed * speedTimer.getElapsedTime()), (int) getPosition().y ); 
			}
			else if (direction == Direction.RIGHT)
			{
				if (flip)
					flip();

				//Don't go out screen
				if (getPosition().x < 800 - getCurrentAnimation().getKeyFrame().w / 2  && !this.collidWith(opponent))
					setPosition( (int) getPosition().x + (speed * speedTimer.getElapsedTime()), (int) getPosition().y );
			}
			
			speedTimer.restart();
		}
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
	
	public void end(Game game)
	{
		if (this.equals(game.getWinner()))
			this.updateWins(1);
		else
			this.updateLooses(1);
		
		this.updateGames(1);
		this.updateElo(game.getEloChange(this));
		
		// Reset the kangaroo
		currentAnimation = 0;
		setState(States.idle);
		
		if (flip)
			flip();
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

	public void setState(States state)
	{
		// Cancel previous offset
		setPosition(getPosition().x + getAnimation(this.state.ordinal()).getOffset().x, getPosition().y);
		
		this.state = state;
		
		// Add the offset of this new animation
		setPosition(getPosition().x - getAnimation(state.ordinal()).getOffset().x, getPosition().y);
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
		this.networkImage.flip = this.flip;
		this.networkImage.x = this.getPosition().x;
		this.networkImage.y = this.getPosition().y;
	}
	
	public FriendsDataPacket getFriendsDataPacket()
	{
		return ServerUtils.getFriendsDataPacket(this);
	}
	
	public LadderDataPacket getLadderDataPacket()
	{
		return ServerUtils.getLadderDataPacket(this);
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

	public boolean isDead()
	{
		return (health <= 0);
	}
	
	public boolean isTouched() {
		return touched;
	}

	public boolean isFlipped() {
		return flip;
	}
	
	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
}