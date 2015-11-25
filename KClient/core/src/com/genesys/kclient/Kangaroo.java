package com.genesys.kclient;

import Packets.UpdateKangarooPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.genesys.enums.Direction;

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
	//private boolean guard;
	
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
		addAnimation(new Animation("IDLE", "anims/idle.hba"));
		addAnimation(new Animation("HIT", "anims/hit.hba"));
		addAnimation(new Animation("PLEFT", "anims/leftpunch.hba"));
		addAnimation(new Animation("PRIGHT", "anims/rightpunch.hba"));
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
