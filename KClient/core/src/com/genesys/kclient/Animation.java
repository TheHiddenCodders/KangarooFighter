package com.genesys.kclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Animation
{
	/*
	 * Attributes
	 */
	private String name;
	private int nFrames, fps = 1, currentFrame = 0;
	protected ArrayList<TextureRegion> frames;
	protected ArrayList<Hitbox> hitboxes;
	private float timer = 0;
	private boolean resume = true;
	
	/*
	 * Constructors
	 */	
	
	/**
	 * Make an animation by cutting the texture sheet by the frame dimensions. Also add hitboxes to the frames
	 * @param name
	 * @param fps
	 * @param sheet
	 * @param frame
	 * @param hitboxes
	 */
	public Animation(String name, int fps, final Texture sheet, final Rectangle frame, ArrayList<Hitbox> hitboxes)
	{
		this(name, fps, sheet, frame);
	
		this.hitboxes = hitboxes;
	}
	
	/**
	 * Make an animation by cutting the texture sheet by the frame dimensions. 
	 * @param name
	 * @param fps
	 * @param sheet
	 * @param frame
	 */
	public Animation(String name, int fps, final Texture sheet, final Rectangle frame)
	{
		this.name = name;
		this.fps = fps;
		nFrames = (int) (sheet.getWidth() / frame.width);
		
		frames = new ArrayList<TextureRegion>();
		hitboxes = new ArrayList<Hitbox>();
		
		for (int i = 0; i < nFrames; i++)
		{
			frames.add(new TextureRegion(sheet, (int) (frame.x + frame.width * i), (int) frame.y, (int) frame.width, (int) frame.height));
			hitboxes.add(new Hitbox());
		}
		
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
		this.fps = fps;
		this.frames = frames;
		this.hitboxes = hitboxes;
	}
	
	/**
	 * Make an animation from a file .hba
	 * @param name
	 * @param path
	 */
	public Animation(String name, String path)
	{
		this.name = name;
		
		load(path);		
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Update time and handle frame skipping
	 * @param delta
	 */
	public void update(float delta)
	{
		timer = timer + delta;
		
		if (!resume)
		{
			if (timer > 1f / (float) fps)
			{
				skipFrame();
			}
		}
	}
	
	/**
	 * Start or resume the animation
	 */
	public void start()
	{
		resume = false;
	}
	
	/**
	 * Resume the animation
	 */
	public void resume()
	{
		resume = true;
		timer = 0;
	}
	
	/**
	 * Stop the animation
	 */
	public void stop()
	{
		resume = true;
		timer = 0;
		currentFrame = 0;
	}
	
	/**
	 * Go to next frame
	 */
	public void skipFrame()
	{
		if (currentFrame < nFrames - 1)
			currentFrame++;
		else
			currentFrame = 0;
		
		timer = 0;
	}
	
	/**
	 * Go to previous frame
	 */
	public void unSkipFrame()
	{
		if (currentFrame > 0)
			currentFrame--;
		else
			currentFrame = nFrames - 1;
		
		timer = 0;
	}
	
	/**
	 * Save the animation in a file
	 * @param path save destination (absolute path)
	 * @param sheetPath path of the sheet texture
	 */
	public void save(String path, FileHandle sheetPath)
	{
		FileHandle file = Gdx.files.absolute(path);
		
		String writeString = sheetPath.path() + "\n"
				+ frames.get(0).getRegionWidth() + "," + frames.get(0).getRegionHeight() + "\n"
				+ fps + "\n"
				+ hitboxes.get(0).polygons.size() + "\n";
		
		for (Hitbox hb : hitboxes)
		{
			for (Polygon poly : hb.polygons)
			{
				for (float vertice : poly.getVertices())
				{
					writeString = writeString.concat(vertice + ",");
				}
				writeString = writeString.concat("\n");
			}
		}
		
		file.writeString(writeString, false);
	}
	
	/**
	 * Load the animation from a file
	 * @param path
	 */
	public void load(String path)
	{
		FileHandle file = Gdx.files.internal(path);
		BufferedReader reader = new BufferedReader(file.reader());
		ArrayList<String> lines = new ArrayList<String>();
		System.err.println("Anim: " + path );
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
			Rectangle frame = new Rectangle(0, 0, Integer.parseInt(lines.get(1).split(",")[0]), Integer.parseInt(lines.get(1).split(",")[1]));
			fps = Integer.parseInt(lines.get(2)); 
			int nBoxPerHitbox = Integer.parseInt(lines.get(3));
			int nHitboxes = (lines.size() - 4) / nBoxPerHitbox;
			
			// Cut sheet
			nFrames = (int) (sheet.getWidth() / frame.width);
			frames = new ArrayList<TextureRegion>();
			for (int i = 0; i < nFrames; i++)
			{
				System.err.println("Frame " + i + ": " + "[" + (frame.x + frame.width * i) + "]");
				frames.add(new TextureRegion(sheet, (int) (frame.x + frame.width * i), (int) frame.y, (int) frame.width, (int) frame.height));	
			}
			
			// Load hitboxes
			hitboxes = new ArrayList<Hitbox>();
			for (int i = 0; i < nHitboxes; i++)
			{
				Hitbox temp = new Hitbox();
				
				for (int j = 0; j < nBoxPerHitbox; j++)
				{
					float[] vertices = new float[lines.get(4 + (nBoxPerHitbox * i) + j).split(",").length];
					
					for (int k = 0; k < vertices.length; k++)
					{
						vertices[k] = Float.valueOf(lines.get(4 + (nBoxPerHitbox * i) + j).split(",")[k]);
					}
					temp.addPoly(vertices);
				}
				
				temp.setSize((int) frame.width, (int) frame.height);
				hitboxes.add(temp);
			}
			
			for (int i = nHitboxes - 1; i < nFrames; i++)
				hitboxes.add(new Hitbox());
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
			
	}
	
	public void flip()
	{
		for (Hitbox hb : hitboxes)
			hb.flip();
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
	
	public void setFramesAndHitboxes(ArrayList<TextureRegion> frames, ArrayList<Hitbox> hitboxes)
	{
		setFrames(frames);
		setHitboxes(hitboxes);
		nFrames = frames.size();
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
	
	public boolean isRunning()
	{
		return !resume;
	}
}
