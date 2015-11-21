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
	
	public ServerAnimation()
	{
		
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
		}
		
		// Change the cuurent frame
		if (currentFrame < nFrames - 1)
		{
			currentFrame++;
		}
		else
		{
			currentFrame = 0;
		}
	}
	
	public void start()
	{
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
}
