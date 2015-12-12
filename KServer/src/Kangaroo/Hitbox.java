package Kangaroo;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import enums.BodyPart;

public class Hitbox
{
	/*
	 * Attributes
	 */
	
	public ArrayList<Polygon> polygons;
	public int colliderIndex = -1;
	public float x = 0, y = 0;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Make an empty Hitbox (no polygons in)
	 */
	public Hitbox()
	{
		polygons = new ArrayList<Polygon>();
	}
	
	/**
	 * Copy constructor
	 * @param hitbox
	 */
	public Hitbox(Hitbox hitbox)
	{
		this();
		x = hitbox.x;
		y = hitbox.y;
		colliderIndex = hitbox.colliderIndex;
		
		for (int i = 0; i < hitbox.polygons.size(); i++)
		{
			Polygon temp = new Polygon(hitbox.polygons.get(i).xpoints, hitbox.polygons.get(i).ypoints, hitbox.polygons.get(i).npoints);
			addPoly(temp);
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
		x += value;
		for (Polygon a : polygons)
			a.translate(value, 0);
	}
	
	/**
	 * Translate Y all the polygons by value
	 * @param value
	 */
	public void translateY(int value)
	{
		y += value;
		for (Polygon a : polygons)
			a.translate(0, value);
	}
	
	/**
	 * Check if Hitbox is colliding with another Hitbox
	 * The collid test will check for every polygons of the hitpolygons
	 * The colliding polygons are stored in the colliderIndex of each hitpolygons
	 * @param polygons2
	 * @return bodyPart[0] = bodyPart of this and bodyPart[1] = polygons2 bodyPart
	 */
	public BodyPart[] collidWith(Hitbox polygons2)
	{
		int indexA = 0;
		int indexB = 0;

		for(Polygon a : polygons)
		{
			for(int i = 0; i < polygons2.polygons.size(); i++)
			{
				if (polygonIntersectPolygon(a, polygons2.polygons.get(i)))
				{
					BodyPart[] parts = new BodyPart[2];
					parts[0] = BodyPart.values()[indexA];
					parts[1] = BodyPart.values()[i];
					colliderIndex = indexA;
					polygons2.colliderIndex = indexB;
					System.err.println(parts[0] + " hit " + parts[1]);
					return parts;
				}
			}
			indexA++;
		}
		
		return null;
	}
	
	/**
	 * Determines if the two polygons supplied intersect each other, by checking if either polygon has points which are contained in the other. 
	 * (It doesn't detect body-only intersections, but is sufficient in most cases.) 
	*/ 
	public static boolean polygonIntersectPolygon(Polygon p1, Polygon p2)
	{
		Point p; 
		for(int i = 0; i < p2.npoints;i++)
		{
			p = new Point(p2.xpoints[i],p2.ypoints[i]); if(p1.contains(p)) return true; 
		} 
		
		for(int i = 0; i < p1.npoints;i++) 
		{
			p = new Point(p1.xpoints[i],p1.ypoints[i]); if(p2.contains(p)) return true;
		}
		return false;
	}
	
	/**
	 * Flip all the polygons
	 * @param fullWidth
	 */
	public void flip()
	{			
		for (Polygon a : polygons)
			polyFlip(a);
	}
	
	/**
	 * Flip a single polygon (ONLY TESTED ON RECTANGLES)
	 * @param poly to flip
	 */
	private void polyFlip(Polygon poly)
	{		
		// Find max x value of the array
		int maxX = 0;
		for (int i = 0; i < poly.npoints; i++)
			maxX = (int) Math.max(poly.xpoints[i] - x, maxX);

		// Reverse 
		for (int i = 0; i < poly.npoints; i++)
			poly.xpoints[i] = (int) (maxX - poly.xpoints[i] + 2 * x);
	}
	
	@Override
	public String toString() 
	{
		String temp = "";
		for (Polygon poly : polygons)
		{
			temp.concat(poly.toString());
			temp = temp.concat("\n");
		}
		return temp;
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void addBox(Rectangle box)
	{
		Polygon temp = new Polygon();
		temp.addPoint(box.x, box.y);
		temp.addPoint(box.x + box.width, box.y);
		temp.addPoint(box.x + box.width, box.y + box.height);
		temp.addPoint(box.x, box.y + box.height);
		addPoly(temp);
	}
	
	public void addPoly(Polygon poly)
	{
		poly.translate((int) x, (int) y);
		polygons.add(poly);
	}
	
	public void addPolyWithoutTransform(Polygon poly)
	{
		polygons.add(poly);
	}
	
	public void removePoly(Polygon poly)
	{
		polygons.remove(poly);
	}
}
