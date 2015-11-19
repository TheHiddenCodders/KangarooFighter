package com.genesys.kclient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Animation
{
	/*
	 * Attributes
	 */
	private String name;
	private int nFrames, fps, currentFrame = 0;
	protected ArrayList<TextureRegion> frames;
	protected ArrayList<Hitbox> hitboxes;
	private float timer = 0;
	private boolean resume = true;
	
	/*
	 * Constructors
	 */	
	
	public Animation(String name, int fps, final Texture sheet, final Rectangle frameSize, ArrayList<Hitbox> hitboxes)
	{
		this.name = name;
		this.fps = fps;
		nFrames = (int) (sheet.getWidth() / frameSize.width);
		frames = new ArrayList<TextureRegion>();
		
		for (int i = 0; i < nFrames; i++)
			frames.add(new TextureRegion(sheet, (int) (frameSize.x + frameSize.width * i), (int) frameSize.y, (int) frameSize.width, (int) frameSize.height));
	
		this.hitboxes = hitboxes;
	}
	
	public Animation(String name, int fps, final Texture sheet, final Rectangle frameSize)
	{
		this.name = name;
		this.fps = fps;
		nFrames = (int) (sheet.getWidth() / frameSize.width);
		
		frames = new ArrayList<TextureRegion>();
		
		for (int i = 0; i < nFrames; i++)
			frames.add(new TextureRegion(sheet, (int) (frameSize.x + frameSize.width * i), (int) frameSize.y, (int) frameSize.width, (int) frameSize.height));
	}

	public Animation(String name, int fps, ArrayList<TextureRegion> frames, ArrayList<Hitbox> hitboxes)
	{
		this.name = name;
		this.setFps(fps);
		this.frames = frames;
		this.hitboxes = hitboxes;
	}
	
	/*
	 * Methods
	 */
	public void update(float delta)
	{
		timer = timer + delta;
		
		if (!resume)
		{
			if (timer > 1f / (float) fps)
			{
				if (currentFrame < nFrames - 1)
					currentFrame++;
				else
					currentFrame = 0;
				
				timer = 0;
			}
		}
	}
	
	public void start()
	{
		resume = false;
	}
	
	public void stop()
	{
		resume = true;
		timer = 0;
	}

	public void setFramesAndHitboxes(ArrayList<TextureRegion> frames, ArrayList<Hitbox> hitboxes)
	{
		setFrames(frames);
		setHitboxes(hitboxes);
	}
	
	/*
	 * Getters & Setters
	 */
	
	public String getName()
	{
		return name;
	}
	
	public void setFps(int fps)
	{
		this.fps = fps;
	}
	
	public TextureRegion getKeyFrame()
	{
		return frames.get(currentFrame);
	}
	
	public void setFrames(ArrayList<TextureRegion> frames)
	{
		this.frames = frames;
	}
	
	public void setHitboxes(ArrayList<Hitbox> hitboxes)
	{
		this.hitboxes = hitboxes;
	}
	
	public int getKeyFrameIndex()
	{
		return currentFrame;
	}
	
	public Hitbox getKeyHitbox()
	{
		return hitboxes.get(currentFrame);
	}
}
