package com.genesys.kclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.genesys.enums.Direction;

import Packets.UpdateKangarooPacket;

public class Kangaroo extends Actor
{
	/*
	 * Attributes
	 */
	private String ip;
	private Sprite sprite;
	private String name;
	private int health;

	private Texture kangaroo, flippedKangaroo;
	
	// Compare kangaroo update packet to this network image to know if server need to be updated
	public UpdateKangarooPacket networkImage;
	
	/*
	 * Constructors
	 */
	/**
	 * unused
	 */
	public Kangaroo()
	{
		super();
		
		kangaroo = new Texture(Gdx.files.internal("sprites/kangourou.png"));
		flippedKangaroo = new Texture(Gdx.files.internal("sprites/kangourouflipped.png"));
		sprite = new Sprite(kangaroo);
	}
	
	/**
	 * Make a kangaroo from an update kangaroo packet.
	 * @param p
	 */
	public Kangaroo(UpdateKangarooPacket p)
	{
		super();
		
		kangaroo = new Texture(Gdx.files.internal("sprites/kangourou.png"));
		flippedKangaroo = new Texture(Gdx.files.internal("sprites/kangourouflipped.png"));
		
		ip = p.ip;
		sprite = new Sprite(kangaroo);
		sprite.setPosition(p.x, p.y);
		name = p.name;
		health = p.health;
		
		networkImage = getUpdatePacket();
	}
	
	@Override
	public void act(float delta)
	{
		
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		sprite.draw(batch);
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
			sprite.translateX(-1);
		else if (direction == Direction.RIGHT)
			sprite.translateX(1);
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
			health =p.health;
			sprite.setPosition(p.x, p.y);
			
			networkImage = p;
		}
	}
	
	/**
	 * Flip the kangaroo texture
	 */
	public void flip()
	{
		if (sprite.getTexture().equals(kangaroo))
			sprite.setTexture(flippedKangaroo);
		else if (sprite.getTexture().equals(flippedKangaroo))
			sprite.setTexture(kangaroo);			
	}
	
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
		if (this.sprite.getX() == networkImage.x && this.sprite.getY() == networkImage.y && this.getHealth() == networkImage.health)
			return true;
		
		else
			return false;
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
		p.x = sprite.getX();
		p.y = sprite.getY();
		p.health = health;
		return p;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
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
