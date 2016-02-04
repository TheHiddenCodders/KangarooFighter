package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedSprite extends Actor 
{
	/*
	 * Attributes
	 */
	
	private TextureRegion currentFrame;
	
	/*
	 * Constructors
	 */
	
	public AnimatedSprite() 
	{
		super();
		
		currentFrame = new TextureRegion(new Texture(Gdx.files.internal("sprites/gamestage/kangourou.png")));
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
		batch.draw(currentFrame, getX(), getY());
		super.draw(batch, parentAlpha);
	}
}
