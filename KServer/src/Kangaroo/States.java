package Kangaroo;

import java.util.ArrayList;

import Utils.Vector2;

public class States 
{
	public final static int idle = 0;
	public final static int punch = 1;
	public final static int guard = 2;
	public final static int movement = 3;
	
	//private ArrayList<Hitbox> hitboxes;
	private int currentState;
	private Vector2 position;
	
	public States()
	{
		setState(idle);
		setPosition(new Vector2(0, 0));
	}

	public int getState() 
	{
		return currentState;
	}

	public void setState(int currentState) 
	{
		this.currentState = currentState;
	}

	public Vector2 getPosition() 
	{
		return position;
	}

	public void setPosition(Vector2 position) 
	{
		this.position = position;
	}
	
	
} 
