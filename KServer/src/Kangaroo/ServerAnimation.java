package Kangaroo;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Utils.Rectangle;
import Utils.Timer;

public class ServerAnimation 
{
	public static final int foreverPlay = 0;
	public static final int onePlay = 1;
	
	private ArrayList<Hitbox> hitboxes;
	private Timer timer;
	private int nFrames, fps, currentFrame = 0;
	private boolean resume;
	private boolean over = false;
	private int mode;
	
	public ServerAnimation(String animationPath)
	{
		load(animationPath);
		timer = new Timer();
	}
	
	public void update()
	{	
		if (resume)
		{		
			if (timer.getElapsedTime() > 1f / fps)
			{
				// Change the current frame
				if (currentFrame < nFrames - 1)
				{
					currentFrame++;
				}
				else
				{
					if (mode == foreverPlay)
					{
						currentFrame = 0;
					}
					else
					{
						stop();
					}
				}
				
				timer.restart();
			}
		}
	}
	
	public void start(States state)
	{
		System.err.println("Launched animation has " + nFrames + " frames");
		resume = true;
		over = false;
		timer.restart();
	}
	
	public void stop()
	{
		currentFrame = 0;
		resume = false;
		over = true;	
	}
	
	/**
	 * Load the animation from a file
	 * @param path
	 */
	public void load(String path)
	{
		File file = new File(path);
		ArrayList<String> lines = new ArrayList<String>();

		String line;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			
			// Read the file
			while (line != null)
			{
				lines.add(line);
				line = reader.readLine();
			}
			
			reader.close();
			
			// Parse it
			BufferedImage sheet = ImageIO.read(new File("assets/" + lines.get(0)));
			Rectangle frame = new Rectangle(0, 0, Integer.parseInt(lines.get(1).split(",")[0]), Integer.parseInt(lines.get(1).split(",")[1]));
			fps = Integer.parseInt(lines.get(2)); 
			int nBoxPerHitbox = Integer.parseInt(lines.get(3));
			int nHitboxes = (lines.size() - 4) / nBoxPerHitbox;
			
			// Cut sheet
			nFrames = (int) (sheet.getWidth() / frame.width);
			
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
				
				hitboxes.add(temp);
			}
			
			// If no boxes, add empty hitboxes
			for (int i = nHitboxes; i < nFrames; i++)
				hitboxes.add(new Hitbox());
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}		
		
		System.err.println("Loaded animation has " + nFrames + " frames");
	}
	
	/*
	 * Getters - Setters
	 */
	
	public boolean isOver()
	{
		return over;
	}
	
	public float getTotalDuration()
	{
		return hitboxes.size() / fps;
	}
	
	public Hitbox getKeyFrame()
	{
		return hitboxes.get(currentFrame);
	}
	
	public int getNFrames()
	{
		return nFrames;
	}
	
	public void setHitboxes(ArrayList<Hitbox> hitboxes)
	{
		this.hitboxes = hitboxes;
	}
	
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	public int getMode()
	{
		return mode;
	}
}
