package Class;

import Packets.KangarooPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Kangaroo extends AnimatedSprite
{
	/*
	 * Attributes
	 */
	
	private int health;
	private int damage;
	private int state;
	private boolean flip;
	
	/*
	 * Constructors
	 */
	
	public Kangaroo() 
	{
		super();
		
		// Make animations
		Animation[] animations = new Animation[1];
		animations[0] = new Animation("IDLE", new Texture(Gdx.files.internal("anims/idle.png")), new Rectangle(0, 0, 221, 203));
		animations[0].setFps(10);
		
		// Set animations
		setAnimations(animations);
	}
	
	public Kangaroo(KangarooPacket player)
	{
		this();
		
		setPosition(player.x, player.y);
		setHealth(player.health);
		setDamage(player.damage);
		setState(player.state);
		setFlip(player.flip);			
	}

	/*
	 * Methods
	 */
	
	public void update(KangarooPacket packet)
	{
		setPosition(packet.x, packet.y);
		setHealth(packet.health);
		setDamage(packet.damage);
		setState(packet.state);
		setFlip(packet.flip);	
	}
	
	/*
	 * Getters - Setters
	 */
	
	public int getHealth()
	{
		return health;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public int getDamage() 
	{
		return damage;
	}

	public void setDamage(int damage) 
	{
		this.damage = damage;
	}

	public int getState() 
	{
		return state;
	}

	public void setState(int state) 
	{
		this.state = state;
	}

	public boolean isFlip() 
	{
		return flip;
	}

	public void setFlip(boolean flip)
	{
		this.flip = flip;
		
		if (flip)
			flip();
	}
}
