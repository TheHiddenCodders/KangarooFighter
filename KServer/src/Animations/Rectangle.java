package Animations;

public class Rectangle 
{
	private float w, h;
	private float x, y;
	
	public Rectangle()
	{
	}
	
	public Rectangle(float x, float y, float w, float h)
	{
		this.w = w;
		this.h = h;
		this.x = x;
		this.y = y;
	}
	
	public Rectangle(Rectangle rectangle) 
	{
		this.w = rectangle.w;
		this.h = rectangle.h;
		this.x = rectangle.x;
		this.y = rectangle.y;
	}

	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void translate(float x, float y)
	{
		this.x += x;
		this.y += y;
	}
	
	public void setSize(float w, float h)
	{
		this.w = w;
		this.h = h;
	}
	
	public float getWidth()
	{
		return w;
	}
	
	public float getHeigth()
	{
		return h;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public boolean contains(Rectangle rect)
	{
		System.err.println(rect.x + ", " + rect.y + " - " + x + ", " + y);
		if (x < rect.x + rect.w &&
		   x + w > rect.x &&
		   y < rect.y + rect.h &&
		   h + y > rect.y) 
		{
			return true;
		}
		
		return false;
	}
}
