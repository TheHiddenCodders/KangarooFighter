package Class;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedSprite extends Actor 
{
	/*
	 * Attributes
	 */
	
	private Animation[] animations;
	private int currentAnim = 0;
	
	/*
	 * Constructors
	 */
	
	public AnimatedSprite() 
	{
		super();
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta) 
	{
		
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		batch.draw(getCurrentAnimation().getKeyFrame(), getX(), getY());
		super.draw(batch, parentAlpha);
	}
	
	/*
	 * Getters - Setters
	 */
	
	public Animation getCurrentAnimation()
	{
		return animations[currentAnim];
	}
	
	public void setAnimations(Animation[] animations)
	{
		this.animations = animations;
	}
}
