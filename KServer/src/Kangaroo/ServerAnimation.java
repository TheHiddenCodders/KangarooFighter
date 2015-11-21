package Kangaroo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ServerAnimation 
{
	public static final int foreverPlay = 0;
	public static final int onePlay = 1;
	
	private ArrayList<Hitbox> hitboxes;
	private Timer timer;
	private int nFrames, fps, currentFrame = 0;
	private boolean resume;
	private int mode;
	private States callingState;
	
	public ServerAnimation()
	{
		mode = foreverPlay;
	}
	
	public ServerAnimation(String animationPath)
	{
		
	}
	
	public void changeFrame()
	{
		if (resume)
		{
			timer.schedule(new TimerTask()
			{
	
				@Override
				public void run() 
				{
					changeFrame();
				}
				
			}, 1/fps);
		
			// Change the current frame
			if (currentFrame < nFrames - 1)
			{
				currentFrame++;
			}
			else
			{
				if (mode == foreverPlay)
					currentFrame = 0;
				else
				{
					callingState.setState(States.idle);
					currentFrame = 0;
					resume = false;
				}
			}
		
		}
	}
	
	public void start(States state)
	{
		callingState = state;
		resume = true;
		
		timer.schedule(new TimerTask()
		{

			@Override
			public void run() 
			{
				changeFrame();
			}
			
		}, 0);
	}
	
	public void stop()
	{
		resume = false;
	}
	
	public Hitbox getCurrentFrame()
	{
		return hitboxes.get(currentFrame);
	}
	
	public void setHitboxes(ArrayList<Hitbox> hitboxes)
	{
		this.hitboxes = hitboxes;
	}
	
	public void setMode (int mode)
	{
		this.mode = mode;
	}
}
