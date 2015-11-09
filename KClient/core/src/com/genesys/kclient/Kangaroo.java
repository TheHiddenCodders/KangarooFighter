package com.genesys.kclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import Packets.UpdateKangarooPacket;

public class Kangaroo extends Actor
{
	/*
	 * Attributes
	 */
	private Sprite sprite;
	private String name;
	private int health;
	
	/*
	 * Constructors
	 */
	public Kangaroo()
	{
		super();
		
		sprite = new Sprite(new Texture(Gdx.files.internal("sprites/kangaroo.png")));
	}
	
	/**
	 * Simple constructor, build a kangaroo with a sprite (but no health).
	 * @param name
	 */
	public Kangaroo(String name)
	{
		super();
		
		sprite = new Sprite(new Texture(Gdx.files.internal("sprites/kangaroo.png")));
		this.name = name;
	}
	
	/**
	 * Make a kangaroo from an update kangaroo packet.
	 * @param p
	 */
	public Kangaroo(UpdateKangarooPacket p)
	{
		super();
		
		sprite = new Sprite(new Texture(Gdx.files.internal("sprites/kangaroo.png")));
		sprite.setPosition(p.x, p.y);
		name = p.name;
		health = p.health;
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
		}
	}
	
	/*
	 * Getters & Setters
	 */
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
