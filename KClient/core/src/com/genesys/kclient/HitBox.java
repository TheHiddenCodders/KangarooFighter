package com.genesys.kclient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Hitbox
{
	/*
	 * Attributes
	 */
	
	public ArrayList<Polygon> polygons;
	public int colliderIndex = -1;
	public float x = 0, y = 0;
	public float w = 0, h = 0;
	
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
			Polygon temp = new Polygon();
			float[] vertices = new float[hitbox.polygons.get(i).getVertices().length];
			
			for (int j = 0; j < hitbox.polygons.get(i).getVertices().length; j++)
				vertices[j] = new Float(hitbox.polygons.get(i).getVertices()[j]);
			
			temp.setVertices(vertices);
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
	public void translateX(float value)
	{
		x += value;
		for (Polygon a : polygons)
			a.translate(value, 0);
	}
	
	/**
	 * Translate Y all the polygons by value
	 * @param value
	 */
	public void translateY(float value)
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
	 * @return true or false
	 */
	public boolean collidWith(Hitbox polygons2)
	{
		int indexA = 0;
		int indexB = 0;
		
		for(Polygon a : polygons)
		{
			indexA++;
			for(Polygon b : polygons2.polygons)
			{
				indexB++;
				if (Intersector.overlapConvexPolygons(a, b))
				{
					colliderIndex = indexA;
					polygons2.colliderIndex = indexB;
					return true;					
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Use for debug, draw the polygons
	 */
	public void drawDebug(ShapeRenderer render)
	{
		for (Polygon a : polygons)
			render.polygon(a.getTransformedVertices());
		
	//	for (Polygon a : polygons)
	//		render.polygon(a.getVertices());
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
	 * Add polygon.size * value to the polygon (ONLY WORK ON RECTANGLES)
	 * @param value the multiplicator
	 */
	public void sizeBy(float value)
	{
		for (Polygon a : polygons)
		{
			float x = a.getVertices()[0];
			float y = a.getVertices()[1];
			float width = a.getVertices()[2] - a.getVertices()[0]; // x2 - x1 = w
			float height = a.getVertices()[5] - a.getVertices()[1]; // y2 - y1 = h
			
			x *= value;
			y *= value;
			width *= value;
			height *= value;
			
			a.getVertices()[0] = x;
			a.getVertices()[1] = y;
			a.getVertices()[2] = x + width;
			a.getVertices()[3] = y;
			a.getVertices()[4] = x + width;
			a.getVertices()[5] = y + height;
			a.getVertices()[6] = x;
			a.getVertices()[7] = y + height;			
		}
	}
	
	/**
	 * Flip a single polygon (ONLY TESTED ON RECTANGLES)
	 * @param poly to flip
	 */
	private void polyFlip(Polygon poly)
	{	
		for (int i = 0; i < poly.getVertices().length; i+=2)
			poly.getVertices()[i] = (int) (w - poly.getVertices()[i]);
		
		poly.translate(0, 0);
	}
	
	@Override
	public String toString() 
	{
		String temp = "";
		for (Polygon poly : polygons)
		{
			for (float vertice : poly.getVertices())
			{
				temp = temp.concat(vertice + ",");
			}
			temp = temp.concat("\n");
		}
		return temp;
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void addBox(Rectangle box)
	{
		Polygon temp = PolygonUtils.rectangleToPolygon(box);			
		addPoly(temp);
	}
	
	public void addBoxWithoutTransform(Rectangle box)
	{
		Polygon temp = PolygonUtils.rectangleToPolygon(box);			
		addPolyWithoutTransform(temp);
	}
	
	public void addPoly(Polygon poly)
	{
		for (int i = 0; i < poly.getVertices().length; i++)
			w = Math.max(w, poly.getVertices()[i]);
		
		poly.translate(x, y);
		polygons.add(poly);
	}
	
	public void addPolyWithoutTransform(Polygon poly)
	{
		polygons.add(poly);
	}
	
	public void addPoly(float... vertices)
	{
		addPoly(new Polygon(vertices));
	}
	
	public void removePoly(Polygon poly)
	{
		polygons.remove(poly);
	}
	
	public void setSize(int w, int h)
	{
		this.w = w;
		this.h = h;
	}
}
