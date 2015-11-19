package Kangaroo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
	
	private Timer timer;
	private Kangaroo k;
	
	public States(Kangaroo k)
	{
		setState(idle);
		setPosition(new Vector2(0, 0));
		this.k = k;
		
		timer = new Timer();
	}
	


	public int getState() 
	{
		return currentState;
	}

	public void setState(int currentState) 
	{
		this.currentState = currentState;
		
		if (this.currentState == punch)
		{
			timer.schedule(new TimerTask(){

				@Override
				public void run() 
				{
					setState(idle);
					k.getClient().send(k.getUpdatePacket());
				}
				
			}, 1000);
		}
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
