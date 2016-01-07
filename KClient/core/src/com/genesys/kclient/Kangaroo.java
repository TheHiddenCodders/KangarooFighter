package com.genesys.kclient;

import Packets.KangarooClientPacket;
import Packets.KangarooServerPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.genesys.enums.States;

public class Kangaroo extends AnimatedSprite
{
	/*
	 * Controls
	 */
	
	private static int AKey = Keys.A;
	private static int ZKey = Keys.Z;
	private static int EKey = Keys.E;
	private static int RKey = Keys.R;
	private static int leftKey = Keys.LEFT;
	private static int rightKey = Keys.RIGHT;
	private static int upKey = Keys.UP;
	private static int downKey = Keys.DOWN;
	
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
	private boolean topArrow = false;
	private boolean bottomArrow = false;
	private boolean leftPunch = false;
	private boolean rightPunch = false;
	private boolean guard = false;
	
	private boolean ctrlPlusD = false;
	
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
		this();
		
		ip = p.ip;
		this.setPosition(p.x, p.y);
		name = p.name;
		health = p.health;
		damage = p.damage;
		state = p.state;
		
		if (p.flip != flipped)
			this.flip();
		
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
		if (Gdx.input.isKeyPressed(leftKey))
			leftArrow = true;
		else
			leftArrow = false;
		
		if (Gdx.input.isKeyPressed(rightKey))
			rightArrow = true;
		else
			rightArrow = false;
		
		if (Gdx.input.isKeyPressed(upKey))
			topArrow = true;
		else
			topArrow = false;
		
		if (Gdx.input.isKeyPressed(downKey))
			bottomArrow = true;
		else
			bottomArrow = false;
		
		if (Gdx.input.isKeyPressed(AKey))
			leftPunch = true;
		else
			leftPunch = false;
		
		if (Gdx.input.isKeyPressed(ZKey))
			rightPunch = true;
		else
			rightPunch= false;
		
		if (Gdx.input.isKeyPressed(RKey))
			guard = true;
		else
			guard = false;
		
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.D))
			ctrlPlusD = true;
		else
			ctrlPlusD = false;
	}
	
	/**
	 * Update kangaroo fields by packets.
	 * @param p the packet received
	 */
	public void updateFromPacket(KangarooServerPacket p)
	{
		health = p.health;
		
		if (p.flip != isFlip())
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
		if (leftArrow != networkImage.leftArrow || rightArrow != networkImage.rightArrow ||
			topArrow != networkImage.topArrow || bottomArrow != networkImage.bottomArrow ||
			leftPunch != networkImage.leftPunch || rightPunch != networkImage.rightPunch || 
			guard != networkImage.guard || ctrlPlusD != networkImage.ctrlPlusD)
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
		addAnimation(new Animation("GUARD", "anims/idle.hba"));
		addAnimation(new Animation("PFORWARD", "anims/leftpunch.hba"));
		addAnimation(new Animation("PUPPER", "anims/leftpunch.hba"));
		addAnimation(new Animation("PTOP", "anims/leftpunch.hba"));
		addAnimation(new Animation("LPUNCH", "anims/leftpunch.hba"));
		addAnimation(new Animation("RPUNCH", "anims/rightpunch.hba"));
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
		p.leftArrow = leftArrow;
		p.rightArrow = rightArrow;
		p.topArrow = topArrow;
		p.bottomArrow = bottomArrow;
		p.leftPunch = leftPunch;
		p.rightPunch = rightPunch;
		p.guard = guard;
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
		if (this.state != state && state != States.transitoryState.ordinal())
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
