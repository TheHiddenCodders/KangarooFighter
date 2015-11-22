package com.genesys.kclient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Hitbox
{
	/*
	 * Attributes
	 */
	
	public ArrayList<Rectangle> boxes;
	public int colliderIndex = -1;
	public float x = 0, y = 0;
	ShapeRenderer render;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Make an empty Hitbox (no boxes in)
	 */
	public Hitbox()
	{
		boxes = new ArrayList<Rectangle>();
		render = new ShapeRenderer();
		render.setAutoShapeType(true);
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Translate X all the boxes by value
	 * @param value
	 */
	public void translateX(float value)
	{
		x += value;
		for (Rectangle a : boxes)
			a.x = a.x + value;
	}
	
	/**
	 * Translate Y all the boxes by value
	 * @param value
	 */
	public void translateY(float value)
	{
		y += value;
		for (Rectangle a : boxes)
			a.y = a.y + value;
	}
	
	/**
	 * Check if Hitbox is colliding with another Hitbox
	 * The collid test will check for every boxes of the hitboxes
	 * The colliding boxes are stored in the colliderIndex of each hitboxes
	 * @param boxes2
	 * @return true or false
	 */
	public boolean collidWith(Hitbox boxes2)
	{
		int indexA = 0;
		int indexB = 0;
		
		for(Rectangle a : boxes)
		{
			indexA++;
			for(Rectangle b : boxes2.boxes)
			{
				indexB++;
				if (a.overlaps(b) || b.overlaps(a))
				{
					colliderIndex = indexA;
					boxes2.colliderIndex = indexB;
					return true;					
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Use for debug, draw the boxes
	 */
	public void drawDebug()
	{
		render.begin();
		render.setColor(Color.BLUE);
		
		for (Rectangle a : boxes)
			render.rect(a.x, a.y, a.width, a.height);
		
		render.end();
	}
	
	/**
	 * Flip all the boxes by the axe containerX + fullWidth / 2
	 * @param fullWidth
	 * @param containerX
	 */
	public void flip(float fullWidth, float containerX)
	{	
		for (Rectangle a : boxes)
			a.x = (containerX + fullWidth / 2) - (a.x - (containerX + fullWidth / 2)) - a.width;
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void addBox(Rectangle box)
	{
		boxes.add(box);
	}
	
	public void removeBox(Rectangle box)
	{
		boxes.remove(box);
	}
}
