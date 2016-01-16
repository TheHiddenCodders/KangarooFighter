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
	
	/*
	 * Constructors
	 */
	
	public AnimatedSprite()
	{
		anims = new ArrayList<Animation>();
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
			if (anim.hitboxes != null)
			{
				for (Hitbox hb : anim.hitboxes)
				{
					if (hb.x != this.getX())
						hb.translateX(this.getX() - hb.x);
					
					if (hb.y != this.getY())
						hb.translateY(this.getY() - hb.y);
				}
			}
		}
		
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{		
		if (currentFrame != null)
			batch.draw(currentFrame, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());		
	
		super.draw(batch, parentAlpha);
	}
	
	/**
	 * Used for debug, draw the hitbox
	 * @param render 
	 */
	public void drawDebug(ShapeRenderer render)
	{
		getCurrentAnim().getKeyHitbox().drawDebug(render);
	}
	
	/**
	 * Flip all the sprites & hitboxes 
	 */
	public void flip()
	{			
		flipped = !flipped;
		
		for (Animation anim : anims)
			anim.flip();
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void addAnimation(Animation anim)
	{
		if (anim.hitboxes != null)
		{
			// Translate boxes to match actor position
			for (Hitbox hb : anim.hitboxes)
			{
				hb.translateX(this.getX());
				hb.translateY(this.getY());
			}
		}
		
		anims.add(anim);
		
		if (this.getWidth() != anims.get(currentAnim).getKeyFrame().getRegionWidth())
			this.setWidth(anims.get(currentAnim).getKeyFrame().getRegionWidth());
		
		if (this.getHeight() != anims.get(currentAnim).getKeyFrame().getRegionHeight())
			this.setHeight(anims.get(currentAnim).getKeyFrame().getRegionHeight());
	}
	
	public void setCurrentAnim(int index)
	{
		anims.get(currentAnim).stop();
		currentAnim = index;
		
		this.setSize(anims.get(currentAnim).getKeyFrame().getRegionWidth(), anims.get(currentAnim).getKeyFrame().getRegionHeight());

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
	
	public Animation getCurrentAnim()
	{
		return anims.get(currentAnim);
	}
	
	public boolean isFlip()
	{
		return flipped;
	}
}
