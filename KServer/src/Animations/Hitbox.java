package Animations;

import java.awt.Rectangle;
import java.util.ArrayList;

import enums.BodyPart;

public class Hitbox
{
	/*
	 * Attributes
	 */
	
	public ArrayList<Rectangle> rectangles;
	private Rectangle globalHitbox;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Make an empty Hitbox (no polygons in)
	 */
	public Hitbox()
	{
		rectangles = new ArrayList<Rectangle>();
		globalHitbox = new Rectangle();
	}
	
	/**
	 * Copy constructor
	 * @param hitbox
	 */
	public Hitbox(Hitbox hitbox)
	{
		this();
		
		globalHitbox.setLocation(hitbox.getGlobalHitbox().x, hitbox.getGlobalHitbox().y);
		
		for (int i = 0; i < hitbox.rectangles.size(); i++)
		{
			Rectangle temp = new Rectangle(hitbox.rectangles.get(i));
			addRectangle(temp);
		}
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Translate X all the polygons by value
	 * @param value
	 */
	public void translateX(int value)
	{
		// Translate the global hitbox
		globalHitbox.translate(value, 0);
			
		// Translate each rectangle
		for (Rectangle a : rectangles)
			a.translate(value, 0);
	}
	
	/**
	 * Translate Y all the polygons by value
	 * @param value
	 */
	public void translateY(int value)
	{
		// Translate the global hitbox
		globalHitbox.translate(0, value);

		// Translate each rectangle
		for (Rectangle a : rectangles)
			a.translate(0, value);
	}
	
	/**
	 * Check if Hitbox is colliding with another Hitbox
	 * The collid test will check for every polygons of the hitpolygons
	 * The colliding polygons are stored in the colliderIndex of each hitpolygons
	 * @param polygons2
	 * @return bodyPart[0] = bodyPart of this and bodyPart[1] = polygons2 bodyPart
	 */
	public BodyPart[] collidWith(Hitbox otherHitbox)
	{
		BodyPart[] parts = new BodyPart[2];
		
		// Browse all rectangles of the current hitbox
		for(int i = 0; i < rectangles.size(); i++)
		{
			parts[0] = BodyPart.values()[i];
			
			// Browse all rectangles of the hitbox in parameter
			for(int j = 0; j < otherHitbox.rectangles.size(); j++)
			{
				parts[1] = BodyPart.values()[j];
				
				// If the rectangles are colliding, then return the two parts
				if (rectangles.get(i).contains(otherHitbox.rectangles.get(j)))
					return parts;
			}
		}
		
		// If the two hitboxes aren't colliding
		return null;
	}
	
	/**
	 * Flip all the polygons
	 * @param fullWidth
	 */
	public void flip()
	{
		for (Rectangle rect : rectangles)
		{
			rect.setLocation(globalHitbox.width - rect.x - rect.width, rect.y);
			
		}
	}
	
	@Override
	public String toString() 
	{
		String temp = "";
		for (Rectangle rect : rectangles)
		{
			temp.concat(rect.toString());
			temp = temp.concat("\n");
		}
		return temp;
	}
	
	/*
	 * Getters & Setters
	 */
	
	private void addRectangle(Rectangle temp) 
	{
		rectangles.add(temp);
		
		// TODO : Recompute the global hitbox
	}
	
	public void setSize(int w, int h)
	{
		// TODO : check if the new size contain all rectangle
		globalHitbox.setSize(w, h);
	}
	
	public void setPosition(int posX, int posY)
	{
		int lastPosX = globalHitbox.getLocation().x, lastPosY = globalHitbox.getLocation().y;
		globalHitbox.setLocation(posY,  posY);
		
		for (Rectangle rect : rectangles)
		{
			rect.translate(lastPosX - posX, lastPosY - posY);
		}
	}
	
	public Rectangle getGlobalHitbox()
	{
		return globalHitbox;
	}
}
