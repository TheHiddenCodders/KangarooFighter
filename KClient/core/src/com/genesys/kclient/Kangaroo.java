package com.genesys.kclient;

import Packets.KangarooClientPacket;
import Packets.KangarooServerPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Kangaroo extends AnimatedSprite
{
	/*
	 * Controls
	 */
	
	private static final int leftPunchKey = Keys.A;
	private static final int rightPunchKey = Keys.Z;
	
	/*
	 * Attributes
	 */
	
	private String ip;
	private String name;
	private int health;
	private int damage;
	private int state;
	
	private boolean leftArrow = false;
	private boolean rightArrow = false;
	private boolean leftPunch = false;
	private boolean rightPunch = false;
	
	// Compare kangaroo update packet to this network image to know if server need to be updated
	public KangarooClientPacket networkImage;
	
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
	public Kangaroo(KangarooServerPacket p)
	{
		super();
		
		ip = p.ip;
		this.setPosition(p.x, p.y);
		name = p.name;
		health = p.health;
		damage = p.damage;
		state = p.state;
		
		networkImage = getClientPacket();
		
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
			leftArrow = true;
		else
			leftArrow = false;
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			rightArrow = true;
		else
			rightArrow = false;
		
		if (Gdx.input.isKeyPressed(leftPunchKey))
			leftPunch = true;
		else
			leftPunch = false;
		
		if (Gdx.input.isKeyPressed(rightPunchKey))
			rightPunch = true;
		else
			rightPunch = false;
	}
	
	/**
	 * Update kangaroo fields by packets.
	 * @param p the packet received
	 */
	public void updateFromPacket(KangarooServerPacket p)
	{
		health = p.health;
		
		if ((isFlip() && p.x > getX()) || (!isFlip() && p.x < getX()))
			flip();
		
		setPosition(p.x, p.y);
		setState(p.state);
	}
	
	/**
	 * Compare kangaroo and is network image to know if server needs to be updated
	 * @return true if they match, false if they don't
	 */
	public boolean needUpdate()
	{
		if (leftArrow != networkImage.leftArrowKey || rightArrow != networkImage.rightArrowKey || leftPunch != networkImage.leftPunchKey || rightPunch != networkImage.rightPunchKey)
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
		addAnimation(new Animation("MOVEMENT", "anims/idle.hba"));
		addAnimation(new Animation("HIT", "anims/hit.hba"));
		addAnimation(new Animation("PUNCH", "anims/idle.hba"));
		addAnimation(new Animation("GUARD", "anims/idle.hba"));
		addAnimation(new Animation("PFORWARD", "anims/leftpunch.hba"));
		addAnimation(new Animation("PTOP", "anims/leftpunch.hba"));
		addAnimation(new Animation("LPUNCH", "anims/leftpunch.hba"));
		addAnimation(new Animation("RPUNCH", "anims/rightpunch.hba"));
		addAnimation(new Animation("TRANSITORY", "anims/idle.hba"));
		anims.get(0).start();
	}
	
	/*
	 * Getters & Setters
	 */
	
	/**
	 * @return an updatekangaroopacket with this kangaroo datas
	 */
	public KangarooClientPacket getClientPacket()
	{
		KangarooClientPacket p = new KangarooClientPacket();
		p.leftArrowKey = leftArrow;
		p.rightArrowKey = rightArrow;
		p.leftPunchKey = leftPunch;
		p.rightPunchKey = rightPunch;
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
	
	public int getState() {
		return state;
	}

	public void setState(int state)
	{
		if (this.state != state)
		{
			this.state = state;
			setCurrentAnim(state);
		}
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
