package com.genesys.kclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
	
	/*
	 * Constructors
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
}
