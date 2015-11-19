package com.genesys.kclient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedSprite extends Actor 
{
	/*
	 * Attributes
	 */
	
	private int currentAnim = 0;
	protected ArrayList<Animation> anims;
	protected TextureRegion currentFrame;
	protected boolean flipped = false;
	
	static final boolean debug = true;
	ShapeRenderer renderer;
	
	/*
	 * Constructors
	 */
	
	public AnimatedSprite()
	{
		anims = new ArrayList<Animation>();
		
		if (debug)
			renderer = new ShapeRenderer();
	}

	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta)
	{
		anims.get(currentAnim).update(delta);
		currentFrame = anims.get(currentAnim).getKeyFrame();
		
		if (!currentFrame.isFlipX() && flipped)
			currentFrame.flip(true, false);
		else if (currentFrame.isFlipX() && !flipped)
			currentFrame.flip(true, false);
		
		for (Animation anim : anims)
		{
			for (Hitbox hb : anim.hitboxes)
			{
				if (hb.x != this.getX())
					hb.translateX(this.getX() - hb.x);
			}
		}
		
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{		
		if (currentFrame != null)
			batch.draw(currentFrame, this.getX(), this.getY());		
		
		if (debug)
		{		
			batch.end();
			anims.get(currentAnim).getKeyHitbox().drawDebug();
			batch.begin();
		}
		super.draw(batch, parentAlpha);
	}
	
	public void flip()
	{			
		flipped = !flipped;
		
		for (Hitbox hb : anims.get(currentAnim).hitboxes)
			hb.flip(anims.get(currentAnim).getKeyFrame().getRegionWidth(), this.getX());
	}
	
	public void addAnim(Animation anim)
	{
		// Translate boxes to match actor position
		for (Hitbox hb : anim.hitboxes)
		{
			hb.translateX(this.getX());
			hb.translateY(this.getY());
		}
		
		anims.add(anim);
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void setCurrentAnim(int index)
	{
		anims.get(currentAnim).stop();
		currentAnim = index;
		anims.get(currentAnim).start();
	}
	
	public void setCurrentAnim(String name)
	{
		for (int i = 0; i < anims.size(); i++)
		{
			if (anims.get(i).getName().equals(name))
				setCurrentAnim(i);
		}
	}
}
