package com.genesys.kclient;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.genesys.enums.Direction;

import Packets.UpdateKangarooPacket;

public class Kangaroo extends AnimatedSprite
{
	/*
	 * Attributes
	 */
	private String ip;
	private String name;
	private int health;
	private int damage = 5;
	
	// Compare kangaroo update packet to this network image to know if server need to be updated
	public UpdateKangarooPacket networkImage;
	
	/*
	 * Constructors
	 */
	public Kangaroo()
	{
		super();
		
		initAnim();
	}
	
	/**
	 * Make a kangaroo from an update kangaroo packet.
	 * @param p
	 */
	public Kangaroo(UpdateKangarooPacket p)
	{
		super();
		
		ip = p.ip;
		this.setPosition(p.x, p.y);
		name = p.name;
		health = p.health;
		damage = p.damage;
		
		networkImage = getUpdatePacket();
		
		initAnim();
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
	}
	
	/*
	 * Methods
	 */
	
	public void update()
	{
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			walk(Direction.LEFT);
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			walk(Direction.RIGHT);
	}
	
	/**
	 * Make the kangaroo walk to the left or to the right
	 * @param direction
	 */
	public void walk(Direction direction)
	{
		if (direction == Direction.LEFT)
			moveBy(-1, 0);
		else if (direction == Direction.RIGHT)
			moveBy(1, 0);
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
			health = p.health;
			setPosition(p.x, p.y);
			
			networkImage = p;
		}
	}
	
	/**
	 * Copy the kangaroo into the network image of it
	 */
	public void setSameAsNetwork()
	{
		networkImage = getUpdatePacket();
	}
	
	/**
	 * Compare kangaroo and is network image to know if server needs to be updated
	 * @return true if they match, false if they don't
	 */
	public boolean isSameAsNetwork()
	{
		if (this.getX() == networkImage.x && this.getY() == networkImage.y && this.getHealth() == networkImage.health)
			return true;
		
		else
			return false;
	}

	/**
	 * Init the kangaroo animation
	 */
	public void initAnim()
	{
		// Add idle animation (they are all the same, but it's an example)
		// FRAME 1
		Hitbox hitBox0 = new Hitbox();
		hitBox0.addBox(new Rectangle(130, 202 - 64, 52, 64)); // HEAD
		hitBox0.addBox(new Rectangle(172, 202 - 100 - 34, 40, 34)); // LEFT PUNCH
		hitBox0.addBox(new Rectangle(164, 202 - 108 - 34, 40, 34)); // RIGHT PUNCH
		hitBox0.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		// FRAME 2
		Hitbox hitBox1 = new Hitbox();
		hitBox1.addBox(new Rectangle(132, 202 - 64, 52, 64)); // HEAD
		hitBox1.addBox(new Rectangle(168, 202 - 100 - 34, 40, 34)); // LEFT PUNCH
		hitBox1.addBox(new Rectangle(168, 202 - 108 - 34, 40, 34)); // RIGHT PUNCH
		hitBox1.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		// FRAME 3
		Hitbox hitBox2 = new Hitbox();
		hitBox2.addBox(new Rectangle(134, 202 - 64, 52, 64)); // HEAD
		hitBox2.addBox(new Rectangle(164, 202 - 100 - 34, 40, 34)); // LEFT PUNCH
		hitBox2.addBox(new Rectangle(172, 202 - 108 - 34, 40, 34)); // RIGHT PUNCH
		hitBox2.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		// FRAME 4
		Hitbox hitBox3 = new Hitbox();
		hitBox3.addBox(new Rectangle(136, 202 - 64, 52, 64)); // HEAD
		hitBox3.addBox(new Rectangle(164, 202 - 104 - 34, 40, 34)); // LEFT PUNCH
		hitBox3.addBox(new Rectangle(172, 202 - 104 - 34, 40, 34)); // RIGHT PUNCH
		hitBox3.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		// FRAME 5
		Hitbox hitBox4 = new Hitbox();
		hitBox4.addBox(new Rectangle(136, 202 - 64, 52, 64)); // HEAD
		hitBox4.addBox(new Rectangle(164, 202 - 108 - 34, 40, 34)); // LEFT PUNCH
		hitBox4.addBox(new Rectangle(172, 202 - 100 - 34, 40, 34)); // RIGHT PUNCH
		hitBox4.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		// FRAME 6
		Hitbox hitBox5 = new Hitbox();
		hitBox5.addBox(new Rectangle(134, 202 - 64, 52, 64)); // HEAD
		hitBox5.addBox(new Rectangle(168, 202 - 108 - 34, 40, 34)); // LEFT PUNCH
		hitBox5.addBox(new Rectangle(168, 202 - 100 - 34, 40, 34)); // RIGHT PUNCH
		hitBox5.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		// FRAME 7
		Hitbox hitBox6 = new Hitbox();
		hitBox6.addBox(new Rectangle(132, 202 - 64, 52, 64)); // HEAD
		hitBox6.addBox(new Rectangle(172, 202 - 108 - 34, 40, 34)); // LEFT PUNCH
		hitBox6.addBox(new Rectangle(164, 202 - 100 - 34, 40, 34)); // RIGHT PUNCH
		hitBox6.addBox(new Rectangle(82, 0, 81, 140)); // BODY		
		// FRAME 8
		Hitbox hitBox7 = new Hitbox();
		hitBox7.addBox(new Rectangle(130, 202 - 64, 52, 64)); // HEAD
		hitBox7.addBox(new Rectangle(172, 202 - 104 - 34, 40, 34)); // LEFT PUNCH
		hitBox7.addBox(new Rectangle(164, 202 - 104 - 34, 40, 34)); // RIGHT PUNCH
		hitBox7.addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// Put them together
		ArrayList<Hitbox> idleHitboxes = new ArrayList<Hitbox>();
		idleHitboxes.add(hitBox0);
		idleHitboxes.add(hitBox1);
		idleHitboxes.add(hitBox2);
		idleHitboxes.add(hitBox3);
		idleHitboxes.add(hitBox4);
		idleHitboxes.add(hitBox5);
		idleHitboxes.add(hitBox6);
		idleHitboxes.add(hitBox7);
		
		// Add the animation & hitboxes
		Animation idle = new Animation("IDLE", 8, new Texture(Gdx.files.internal("sprites/kangourousheet.png")), new Rectangle(0, 0, 213, 202), idleHitboxes);
		addAnim(idle);
		anims.get(0).start();
	}
	
	/*
	 * Getters & Setters
	 */
	
	/**
	 * @return an updatekangaroopacket with this kangaroo datas
	 */
	public UpdateKangarooPacket getUpdatePacket()
	{
		UpdateKangarooPacket p = new UpdateKangarooPacket();
		p.ip = ip;
		p.name = name;
		p.x = this.getX();
		p.y = this.getY();
		p.health = health;
		p.damage = damage;
		return p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public String getIp()
	{
		return ip;
	}
}
