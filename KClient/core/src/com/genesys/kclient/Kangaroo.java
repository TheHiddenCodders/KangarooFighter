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
	private final static int punchKey = Keys.SPACE;
	
	
	/*
	 * Attributes
	 */
	private String ip;
	private String name;
	private int health;
	private int damage = 5;
	private boolean punch;
	private boolean guard;
	
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
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			walk(Direction.RIGHT);
		
		if (Gdx.input.isKeyPressed(punchKey))
			punch = true;
		else 
			punch = false;
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
		if (this.getX() == networkImage.x && this.getY() == networkImage.y && this.punch == networkImage.punch)
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
		// FRAME 0
		HitBox hitBox0 = new HitBox();
		hitBox0.addBox(new Rectangle(131, 139, 51, 63)); // HEAD
		hitBox0.addBox(new Rectangle(174, 69, 39, 33)); // LEFT PUNCH
		hitBox0.addBox(new Rectangle(165, 60, 39, 33)); // RIGHT PUNCH
		hitBox0.addBox(new Rectangle(83, 0, 80, 139)); // BODY
		// FRAME 1
		HitBox hitBox1 = new HitBox();
		hitBox1.addBox(new Rectangle(131, 139, 51, 63)); // HEAD
		hitBox1.addBox(new Rectangle(174, 69, 39, 33)); // LEFT PUNCH
		hitBox1.addBox(new Rectangle(165, 60, 39, 33)); // RIGHT PUNCH
		hitBox1.addBox(new Rectangle(83, 0, 80, 139)); // BODY
		// FRAME 2
		HitBox hitBox2 = new HitBox();
		hitBox2.addBox(new Rectangle(131, 139, 51, 63)); // HEAD
		hitBox2.addBox(new Rectangle(174, 69, 39, 33)); // LEFT PUNCH
		hitBox2.addBox(new Rectangle(165, 60, 39, 33)); // RIGHT PUNCH
		hitBox2.addBox(new Rectangle(83, 0, 80, 139)); // BODY
		// FRAME 3
		HitBox hitBox3 = new HitBox();
		hitBox3.addBox(new Rectangle(131, 139, 51, 63)); // HEAD
		hitBox3.addBox(new Rectangle(174, 69, 39, 33)); // LEFT PUNCH
		hitBox3.addBox(new Rectangle(165, 60, 39, 33)); // RIGHT PUNCH
		hitBox3.addBox(new Rectangle(83, 0, 80, 139)); // BODY
		// Put them together
		ArrayList<HitBox> hitBoxes = new ArrayList<HitBox>();
		hitBoxes.add(hitBox0);
		hitBoxes.add(hitBox1);
		hitBoxes.add(hitBox2);
		hitBoxes.add(hitBox3);
		// Add to the sprite
		addAHB(new Animation("IDLE", 1, new Texture(Gdx.files.internal("sprites/kangourousheet.png")), new Rectangle(0, 0, 213, 202)), hitBoxes);
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
		p.punch = punch;
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
