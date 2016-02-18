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
	
	private KangarooPacket packet;
	
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
		
		this.packet = player;
		
		update(player);			
	}

	/*
	 * Methods
	 */
	
	/**
	 * This method update kangaroo fields with packet's ones
	 * @param packet
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
		return packet.health;
	}

	public void setHealth(int health)
	{
		packet.health = health;
	}

	public int getDamage() 
	{
		return packet.damage;
	}

	public void setDamage(int damage) 
	{
		packet.damage = damage;
	}

	public int getState() 
	{
		return packet.state;
	}

	public void setState(int state) 
	{
		packet.state = state;
	}

	public boolean isFlip() 
	{
		return packet.flip;
	}

	public void setFlip(boolean flip)
	{
		packet.flip = flip;
		
		if (flip)
			flip();
	}
}
