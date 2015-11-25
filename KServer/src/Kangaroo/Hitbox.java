package Kangaroo;

import java.util.ArrayList;

import Utils.Rectangle;

public class Hitbox
{
	/*
	 * Attributes
	 */
	
	public ArrayList<Rectangle> boxes;
	public int colliderIndex = -1;
	public float x = 0, y = 0;
	
	/*
	 * Constructors
	 */
	
	public Hitbox()
	{
		boxes = new ArrayList<Rectangle>();
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
	
	public void flip(float fullWidth, float axeX)
	{	
		for (Rectangle a : boxes)
			a.x = (axeX + fullWidth / 2) - (a.x - (axeX + fullWidth / 2)) - a.width;
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
