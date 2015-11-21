package com.genesys.kclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
	
	/**
	 * Make an animation by cutting the texture sheet by the frameSize dimensions. Also add hitboxes to the frames
	 * @param name
	 * @param fps
	 * @param sheet
	 * @param frameSize
	 * @param hitboxes
	 */
	public Animation(String name, int fps, final Texture sheet, final Rectangle frameSize, ArrayList<Hitbox> hitboxes)
	{
		this(name, fps, sheet, frameSize);
	
		this.hitboxes = hitboxes;
	}
	
	/**
	 * Make an animation by cutting the texture sheet by the frameSize dimensions. 
	 * @param name
	 * @param fps
	 * @param sheet
	 * @param frameSize
	 */
	public Animation(String name, int fps, final Texture sheet, final Rectangle frameSize)
	{
		this.name = name;
		this.fps = fps;
		nFrames = (int) (sheet.getWidth() / frameSize.width);
		
		frames = new ArrayList<TextureRegion>();
		
		for (int i = 0; i < nFrames; i++)
			frames.add(new TextureRegion(sheet, (int) (frameSize.x + frameSize.width * i), (int) frameSize.y, (int) frameSize.width, (int) frameSize.height));
	}

	/**
	 * Make an animation with the frames and hitboxes
	 * @param name
	 * @param fps
	 * @param frames
	 * @param hitboxes
	 */
	public Animation(String name, int fps, ArrayList<TextureRegion> frames, ArrayList<Hitbox> hitboxes)
	{
		this.name = name;
		this.setFps(fps);
		this.frames = frames;
		this.hitboxes = hitboxes;
	}
	
	/**
	 * Make an animation from a file .kfa
	 * @param name
	 * @param path
	 */
	public Animation(String name, String path)
	{
		FileHandle file = Gdx.files.internal(path);
		BufferedReader reader = new BufferedReader(file.reader());
		ArrayList<String> lines = new ArrayList<String>();

		String line;
		try
		{
			line = reader.readLine();
			
			// Read the file
			while (line != null)
			{
				lines.add(line);
				line = reader.readLine();
			}
			
			// Parse it
			Texture sheet = new Texture(Gdx.files.internal(lines.get(0)));
			Rectangle frameSize = new Rectangle(0, 0, Integer.parseInt(lines.get(1).split(",")[0]), Integer.parseInt(lines.get(1).split(",")[1]));
			nFrames = Integer.parseInt(lines.get(2));
			int nBoxPerHitbox = Integer.parseInt(lines.get(3));
			fps = Integer.parseInt(lines.get(4)); 
			
			// Cut sheet
			frames = new ArrayList<TextureRegion>();
			for (int i = 0; i < nFrames; i++)
				frames.add(new TextureRegion(sheet, (int) (frameSize.x + frameSize.width * i), (int) frameSize.y, (int) frameSize.width, (int) frameSize.height));	
			
			// Load hitboxes
			hitboxes = new ArrayList<Hitbox>();
			for (int i = 0; i < nFrames; i++)
			{
				Hitbox temp = new Hitbox();
				
				for (int j = 0; j < nBoxPerHitbox; j++)
				{
					int x = Integer.valueOf(lines.get(5 + (nBoxPerHitbox * i) + j).split(",")[0]);
					int y = Integer.valueOf(lines.get(5 + (nBoxPerHitbox * i) + j).split(",")[1]);
					int w = Integer.valueOf(lines.get(5 + (nBoxPerHitbox * i) + j).split(",")[2]);
					int h = Integer.valueOf(lines.get(5 + (nBoxPerHitbox * i) + j).split(",")[3]);
					System.err.println("Rect " + j + " of frame " + i + ": " + new Rectangle(x, y, w, h).toString());
					temp.addBox(new Rectangle(x, y, w, h));
				}
				
				hitboxes.add(temp);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
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
