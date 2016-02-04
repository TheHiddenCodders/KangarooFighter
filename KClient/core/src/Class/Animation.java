package Class;

import Utils.Timer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Animation
{
	/*
	 * Attributes
	 */
	private String name;
	private TextureRegion[] frames;
	private int currentFrame;
	private float fps;
	private Timer timer;
	private boolean resume = true;
	
	/*
	 * Constructors
	 */	
	
	public Animation() 
	{
		currentFrame = 0;
		fps = 1;
		timer = new Timer();
	}
	
	/**
	 * Cut the texture sheet proportionnaly to the dimensions of a frame
	 * @param sheet (to cut)
	 * @param dimensions (of a frame)
	 */
	public Animation(Texture sheet, Rectangle dimensions)
	{
		this();
		
		// Set number of frames
		frames = new TextureRegion[(int) (sheet.getWidth() / dimensions.width)];
		
		// Cut the sheet into frames
		for (int frame = 0; frame < frames.length; frame++)
		{
			frames[frame] =new TextureRegion(sheet, dimensions.x, dimensions.y, dimensions.width, dimensions.height);
		}
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
		// If animation not paused
		if (!resume)
		{
			if (timer.getElapsedTime() > 1f / fps)
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
		resume = !resume;
	}
	
	/**
	 * Stop the animation
	 */
	public void stop()
	{
		resume = true;
	}
	
	/**
	 * Go to next frame
	 */
	public void skipFrame()
	{
		if (currentFrame < frames.length - 1)
			currentFrame++;
		else
			currentFrame = 0;
		
		timer.restart();
	}
		
	/**
	 * Load the animation from a file
	 * @param path
	 */
	public void load(String path)
	{
		// TODO
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
		return frames[currentFrame];
	}
	
	public void setFrames(TextureRegion[] frames)
	{
		this.frames = frames;
	}
	
	public int getKeyFrameIndex()
	{
		return currentFrame;
	}
	
	public boolean isRunning()
	{
		return !resume;
	}
}
