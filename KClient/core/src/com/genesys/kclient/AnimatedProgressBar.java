package com.genesys.kclient;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedProgressBar extends Actor
{
	/*
	 * Attributes
	 */
	private Animation normalAnimation;
	private TextureRegion background;
	private TextureRegion currentFrame;
	private TextureRegion hitBar;
	private boolean valueChanged;
	private int borderSize;
	private float min, max, hitBarMax;
	private float value, hitBarValue;
	private float hitBarBaseWidth;
	private float stateTime = 0;
	private float frameDuration = 0.05f;
	
	
	private static final float hitBarDecrease = 0.2f; 
	/*
	 * Constructors
	 */
	/**
	 * Build an animated progress bar
	 * @param barSheet the sprite sheet containing in this order : background, then frames
	 * @param frames the number of frames of the animation (without background)
	 * @param min
	 * @param max
	 * @param value
	 */
	public AnimatedProgressBar(Texture barSheet, int borderSize, int frames, float min, float max, float value)
	{
		super();
		
		background = new TextureRegion(barSheet, 0, 0, barSheet.getWidth() / frames + (2 * borderSize), barSheet.getHeight() / 3);
		hitBar = new TextureRegion(barSheet, 0, background.getRegionHeight() * 2, barSheet.getWidth() / (frames), barSheet.getHeight() / 3);
		normalAnimation = new Animation(frameDuration, getAnimationFrames(barSheet, frames));
		normalAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		this.borderSize = borderSize;
		this.min = min;
		this.max = max;
		this.value = value;
		currentFrame = normalAnimation.getKeyFrame(0);
		
		this.setSize(background.getRegionWidth(), background.getRegionHeight());
	}
	
	@Override
	public void act(float delta)
	{
		stateTime += delta;
		currentFrame = normalAnimation.getKeyFrame(stateTime);
		
		if (valueChanged)
		{
			if (hitBarValue > value)
				hitBarValue -= hitBarDecrease;	
			else
				valueChanged = false;
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(background, this.getX(), this.getY());
		batch.draw(currentFrame, this.getX() + borderSize, this.getY(), getProgressWidth(currentFrame.getRegionWidth(), value, min, max), currentFrame.getRegionHeight());		
		
		if (valueChanged)
			batch.draw(hitBar, this.getX() + borderSize + getProgressWidth(currentFrame.getRegionWidth(), value, min, max), this.getY(), getProgressWidth(hitBarBaseWidth, hitBarValue, value, hitBarMax), hitBar.getRegionHeight());
			
		super.draw(batch, parentAlpha);
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Calculate the width of the progress
	 * @return the animation frame * percentage
	 */
	private float getProgressWidth(float barWidth, float value, float min, float max)
	{
		float percentage = (value - min) / (max - min);
		return (barWidth * percentage);
	}
	
	/**
	 * Cut the sprite sheet
	 * @param sheet the spritesheet to cut
	 * @param frames the number of frame of the animation
	 * @return
	 */
	private TextureRegion[] getAnimationFrames(Texture sheet, int frames)
	{
		TextureRegion[] keyFrames = new TextureRegion[frames];
		int frameWidth = sheet.getWidth() / frames;
		int frameHeight = sheet.getHeight() / 3;
		
		for (int i = 0; i < frames; i++)
			keyFrames[i] = new TextureRegion(sheet, frameWidth * i, frameHeight, frameWidth, frameHeight);
		
		return keyFrames;
	}
	
	/*
	 * Getters & setters
	 */
	public void setValue(float value)
	{
		if (this.value != value)
		{	
			// Init the hitBar values
			hitBarValue = this.value;
			hitBarMax = hitBarValue;
			hitBarBaseWidth = getProgressWidth(currentFrame.getRegionWidth(), hitBarValue, min, max) - getProgressWidth(currentFrame.getRegionWidth(), value, min, max);
			
			// Set the new value
			this.value = value;
			
			// Set value has changed
			valueChanged = true;
		}
	}
}
