package com.genesys.kclient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class HitBox
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
	
	public HitBox()
	{
		boxes = new ArrayList<Rectangle>();
		render = new ShapeRenderer();
		render.setAutoShapeType(true);
	}

	/*
	 * Methods
	 */
	
	public void translateX(float value)
	{
		x += value;
		for (Rectangle a : boxes)
			a.x = a.x + value;
	}
	
	public void translateY(float value)
	{
		y += value;
		for (Rectangle a : boxes)
			a.y = a.y + value;
	}
	
	public boolean collidWith(HitBox boxes2)
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
	
	public void drawDebug()
	{
		render.begin();
		render.setColor(Color.BLUE);
		
		for (Rectangle a : boxes)
			render.rect(a.x, a.y, a.width, a.height);
		
		render.end();
	}
	
	public void flip(float fullWidth, float axeX)
	{	
		System.out.println("Width : " + fullWidth);
		System.out.println("AxeX : " + axeX);
		for (Rectangle a : boxes)
		{
			System.out.println("Before : " + a.x);
			System.out.println("(" + axeX + " + " + fullWidth / 2 + ") - (" + a.x + " - (" + axeX + " + " + fullWidth / 2 + ")) - " + a.width);
			a.x = (axeX + fullWidth / 2) - (a.x - (axeX + fullWidth / 2)) - a.width;
			System.out.println("After : " + a.x);
		}
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
