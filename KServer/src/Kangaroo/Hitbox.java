package Kangaroo;

import java.util.ArrayList;



import Utils.Polygon;
import Utils.PolygonUtils;
import Utils.Rectangle;

public class Hitbox
{
	/*
	 * Attributes
	 */
	
	public ArrayList<Polygon> polygons;
	//public ArrayList<Rectangle> boxes;
	public int colliderIndex = -1;
	public float x = 0, y = 0;
	
	/*
	 * Constructors
	 */
	
	public Hitbox()
	{
		polygons = new ArrayList<Polygon>();
	}
	
	/*
	 * Methods
	 */
	
	public void translateX(float value)
	{
		x += value;
		for (Polygon a : polygons)
			a.translate(value, 0);
	}
	
	public void translateY(float value)
	{
		y += value;
		for (Polygon a : polygons)
			a.translate(0, value);
	}
	
	public boolean collidWith(Hitbox boxes2)
	{
		int indexA = 0;
		int indexB = 0;
		
		for(Polygon a : polygons)
		{
			indexA++;
			for(Polygon b : boxes2.polygons)
			{
				indexB++;
				if (a.overlap(b) || b.overlap(a))
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
		for (Polygon a : polygons)
			polyFlip(a, fullWidth);
	}
	
	public void addPoly(Polygon poly)
	{
		poly.translate(x, y);
		polygons.add(poly);
	}
	
	/**
	 * Flip a single polygon (ONLY TESTED ON RECTANGLES)
	 * @param poly to flip
	 */
	private void polyFlip(Polygon poly, float fullWidth)
	{		
		for (int i = 0; i < poly.getVertices().length; i+=2)
			poly.getVertices()[i] = fullWidth / 2 - poly.getVertices()[i] + fullWidth / 2;
		
		// Update transformed vertices according to untransformed vertice
		poly.translate(0, 0);
	}
	
	
	/*
	 * Getters & Setters
	 */
	
	public void addBox(Rectangle box)
	{
		Polygon temp = PolygonUtils.rectangleToPolygon(box);			
		addPoly(temp);
	}
	
	public void addPoly(float... vertices)
	{
		addPoly(new Polygon(vertices));
	}
	
	public void removePoly(Polygon poly)
	{
		polygons.remove(poly);
	}
}
