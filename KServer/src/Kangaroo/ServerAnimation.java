package Kangaroo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ServerAnimation 
{
	private ArrayList<Hitbox> hitboxes;
	private Timer timer;
	private int nFrames, fps, currentFrame = 0;
	private boolean resume;
	
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
}
