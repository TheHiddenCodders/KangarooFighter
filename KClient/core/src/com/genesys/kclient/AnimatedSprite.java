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
	protected ArrayList<ArrayList<HitBox>> boxes;
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
		boxes = new ArrayList<ArrayList<HitBox>>();
		
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
		
		for (ArrayList<HitBox> hbs : boxes)
		{
			for (HitBox hb : hbs)
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
			for (HitBox boxes : boxes.get(currentAnim))
				boxes.drawDebug();
			batch.begin();
		}
		super.draw(batch, parentAlpha);
	}

	public void addAHB(Animation anim, ArrayList<HitBox> boxs)
	{
		anims.add(anim);
		
		for (HitBox hb : boxs)
		{
			hb.translateX(this.getX());
			hb.translateY(this.getY());
		}
			
		boxes.add(boxs);
	}
	
	public void flip()
	{			
		flipped = !flipped;
		
		for (HitBox hb : boxes.get(currentAnim))
			hb.flip(anims.get(currentAnim).getKeyFrame().getRegionWidth(), this.getX());
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
