package com.genesys.kclient;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PolygonUtils
{
	
	/**
	 * Transform a polygon to a rectangle 
	 * @param p the polygon to transform
	 * @param transformedVertices if true, rectangle will use polygon transformed vertices, if false, it will use normal ones
	 * @return a rectangle object
	 */
	public static Rectangle polygonToRectangle(Polygon p, boolean transformedVertices)
	{
		Rectangle temp = new Rectangle();
		
		if(transformedVertices)
		{
			temp.x = p.getTransformedVertices()[0];
			temp.y = p.getTransformedVertices()[1];
			temp.width = p.getTransformedVertices()[2] - temp.x;
			temp.height = p.getTransformedVertices()[5] - temp.y;
		}
		else
		{
			temp.x = p.getVertices()[0];
			temp.y = p.getVertices()[1];
			temp.width = p.getVertices()[2] - temp.x;
			temp.height = p.getVertices()[5] - temp.y;
		}
		return temp;
	}
	
	/**
	 * Transform a rectangle to a polygon
	 * @param r the rectangle to transform
	 * @return a polygon object
	 */
	public static Polygon rectangleToPolygon(Rectangle r)
	{
		Polygon temp = new Polygon(new float[] { r.x, r.y,	// bot left corner
				r.x + r.width, r.y,							// bot right corner
				r.x + r.width, r.y + r.height,				// top right corner
				r.x, r.y + r.height});						// top left corner
		
		return temp;
	}
	
	/**
	 * Set the polygon width
	 * ONLY WORKING ON RECTANGLES
	 * @param p
	 * @param width
	 */
	public static void setWidth(Polygon p, float width)
	{
		float x = p.getVertices()[0];

		//x += width;
		p.getVertices()[2] = x + width;
		p.getVertices()[4] = x + width;	
	}
	
	/**
	 * Set the polygon height
	 * ONLY WORKING ON RECTANGLES
	 * @param p
	 * @param height
	 */
	public static void setHeight(Polygon p, float height)
	{
		float y = p.getVertices()[1];

		//y += height;
		p.getVertices()[5] = y + height;
		p.getVertices()[7] = y + height;	
	}
	
	/**
	 * Resize the polygon at width and height
	 * ONLY WORKING ON RECTANGLES
	 * @param p the polygon to resize
	 * @param width the new width
	 * @param height the new height
	 */
	public static void setSize(Polygon p, float width, float height)
	{
		PolygonUtils.setWidth(p, width);
		PolygonUtils.setHeight(p, height);
	}
	
	/**
	 * Return the biggest distance between two X vertices
	 * @param p
	 * @return polygon width
	 */
	public static float getWidth(Polygon p)
	{
		float maxWidth = 0;
		
		for (int i = 0; i < p.getVertices().length; i+=2)
		{
			for (int j = 0; j < p.getVertices().length; j+=2)
			{
				maxWidth = Math.max(p.getVertices()[j] - p.getVertices()[i], maxWidth);
			}
		}
		
		return maxWidth;
	}
	
	/**
	 * Return the biggest distance between two Y vertices
	 * @param p
	 * @return polygon width
	 */
	public static float getHeight(Polygon p)
	{
		float maxHeight = 0;
		
		for (int i = 1; i < p.getVertices().length; i+=2)
		{
			for (int j = 1; j < p.getVertices().length; j+=2)
			{
				maxHeight = Math.max(p.getVertices()[j] - p.getVertices()[i], maxHeight);
			}
		}
		
		return maxHeight;
	}
	
	public static Vector2 getSize(Polygon p)
	{
		return new Vector2(getWidth(p), getHeight(p));
	}
	
	/**
	 * Set the polygon X position relatives to normal vertices (not transformed ones)
	 * @param p
	 * @param newX
	 */
	public static void setNormalX(Polygon p, float newX)
	{		
		float oldX = p.getVertices()[0];
		
		if (oldX == newX)
			return;
		
		p.getVertices()[0] = newX;
		
		for (int i = 2; i < p.getVertices().length; i+=2)
		{
			if (p.getVertices()[i] != oldX)
				p.getVertices()[i] += (newX - oldX);
			else
				p.getVertices()[i] = newX;
		}
	}
	
	/**
	 * Set the polygon Y position relatives to normal vertices (not transformed ones)
	 * @param p
	 * @param newY
	 */
	public static void setNormalY(Polygon p, float newY)
	{		
		float oldY = p.getVertices()[1];
		
		if (oldY == newY)
			return;
		
		p.getVertices()[1] = newY;
		
		for (int i = 3; i < p.getVertices().length; i+=2)
		{
			if (p.getVertices()[i] != oldY)
				p.getVertices()[i] += (newY - oldY);
			else
				p.getVertices()[i] = newY;
		}
	}
	
	/**
	 * Set the polygon X and Y position relatives to normal vertices (not transformed ones).
	 * See ({@link PolygonUtils.updateTransformedVertices})
	 * to update transformed ones from normal ones
	 * @param p
	 * @param newX
	 * @param newY
	 */
	public static void setNormalPosition(Polygon p, float newX, float newY)
	{
		PolygonUtils.setNormalX(p, newX);
		PolygonUtils.setNormalY(p, newY);
	}
	
	/**
	 * Return normal X
	 * @param p
	 * @return
	 */
	public static float getNormalX(Polygon p)
	{
		return p.getVertices()[0];
	}
	
	/**
	 * Return normal Y
	 * @param p
	 * @return
	 */
	public static float getNormalY(Polygon p)
	{
		return p.getVertices()[1];
	}
	
	/**
	 * Return normal pos
	 * @param p
	 * @return
	 */
	public static Vector2 getNormalPos(Polygon p)
	{
		return new Vector2(getNormalX(p), getNormalY(p));
	}
	
	/**
	 * Translate normal coords (not transformed ones)
	 * See ({@link PolygonUtils.updateTransformedVertices})
	 * to update transformed ones from normal ones
	 * @param p
	 * @param amount
	 */
	public static void normalTranslateX(Polygon p, float amount)
	{
		PolygonUtils.setNormalX(p, PolygonUtils.getNormalX(p) + amount);
	}
	
	/**
	 * Translate normal coords (not transformed ones)
	 * See ({@link PolygonUtils.updateTransformedVertices})
	 * to update transformed ones from normal ones
	 * @param p
	 * @param amount
	 */
	public static void normalTranslateY(Polygon p, float amount)
	{
		PolygonUtils.setNormalY(p, PolygonUtils.getNormalY(p) + amount);
	}
	
	/**
	 * Translate normal coords (not transformed ones)
	 * See ({@link PolygonUtils.updateTransformedVertices})
	 * to update transformed ones from normal ones
	 * @param p
	 * @param amount
	 */
	public static void normalTranslate(Polygon p, float amountX, float amountY)
	{
		PolygonUtils.normalTranslateX(p, amountX);
		PolygonUtils.normalTranslateY(p, amountY);
	}
	
	/**
	 * Set the origin to the center
	 * ONLY WORKING ON RECTANGLE
	 * @param p
	 */
	public static void setOriginCenter(Polygon p)
	{
		System.out.println(getCenter(p));
		p.setOrigin(getCenter(p).x, getCenter(p).y);
	}
	
	/**
	 * Get the center of the polygon
	 * ONLY TESTED ON RECTANGLES (but should work on any regular polygon=
	 * @param p
	 * @return a vector containing the mid position
	 */
	public static Vector2 getCenter(Polygon p)
	{
		float sumX = 0;
		float sumY = 0;
		
		for (int i = 0; i < p.getVertices().length; i+=2)
		{
			sumX += p.getVertices()[i];
			sumY += p.getVertices()[i+1];
		}
		
		sumX /= (p.getVertices().length / 2);
		sumY /= (p.getVertices().length / 2);
		
		return new Vector2(sumX, sumY);
	}
	
	/**
	 * Update transformed vertices
	 * @param p
	 */
	public static void updateTransformedVertices(Polygon p)
	{
		p.translate(0, 0);
	}
	
	/**
	 * Allow to reverse normal & transformed vertices
	 * @param p
	 * @param translateX
	 * @param translateY
	 */
	public static void setNormalToTransformedVertices(Polygon p, float translateX, float translateY)
	{
		PolygonUtils.setNormalPosition(p, p.getVertices()[0] - translateX, p.getVertices()[1] - translateY);
		p.translate(translateX, translateY);
	}
}

