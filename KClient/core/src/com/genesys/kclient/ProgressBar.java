package com.genesys.kclient;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressBar extends Actor
{
	/*
	 * Attributes 
	 */
	
	private Texture barSheet;
	private boolean valueChanged;
	private float min, max, hitBarMax;
	private float value, hitBarValue;
	private float hitBarBaseWidth;
	private boolean flip = false;
	
	/*
	 * Constants
	 */
	
	private static final float hitBarDecrease = 0.3f; 
	
	/*
	 * Constructors
	 */
	
	/**
	 * Build a progress bar
	 * @param barSheet
	 * @param min
	 * @param max
	 * @param value
	 */
	public ProgressBar(Texture barSheet, float min, float max, float value)
	{
		super();
		
		// Store sheet
		this.barSheet = barSheet;
		
		// Store values
		this.min = min;
		this.max = max;
		this.value = value;
		
		// Set actor site
		this.setSize(368, 103);
	}
	
	@Override
	public void act(float delta)
	{
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
		// Frame
		batch.draw(barSheet, this.getX(), this.getY(), 0f, 0f, getWidth(), getHeight(), 1f, 1f, 0f, 0, 0, (int) getWidth(), (int) getHeight(), flip, false);
		
		// LifeBar
		if (!flip)
			batch.draw(barSheet, this.getX() + 71, this.getY() + 22, 0f, 0f, getProgressWidth(294, value, min, max), 30, 1f, 1f, 0f, 0, 154, (int) getProgressWidth(294, value, min, max), 30, flip, false);
		else
			batch.draw(barSheet, this.getX() + 3 + (294 - getProgressWidth(294, value, min, max)), this.getY() + 22, 0f, 0f, getProgressWidth(294, value, min, max), 30, 1f, 1f, 0f, 0, 154, (int) getProgressWidth(294, value, min, max), 30, flip, false);
		
		// HitBar
		if (valueChanged)
		{	
			if (!flip)
				batch.draw(barSheet, this.getX() + 71 + getProgressWidth(294, value, min, max), this.getY() + 22, 0f, 0f, getProgressWidth(hitBarBaseWidth, hitBarValue, value, hitBarMax), 30, 1f, 1f, 0f, (int) getProgressWidth(294, value, min, max), 257, (int) getProgressWidth(hitBarBaseWidth, hitBarValue, value, hitBarMax), 30, flip, false);
			else
				batch.draw(barSheet, this.getX() + 3 + (294 - getProgressWidth(294, value, min, max)) - getProgressWidth(hitBarBaseWidth, hitBarValue, value, hitBarMax), this.getY() + 22, 0f, 0f, getProgressWidth(hitBarBaseWidth, hitBarValue, value, hitBarMax), 30, 1f, 1f, 0f, (int) getProgressWidth(294, value, min, max), 257, (int) getProgressWidth(hitBarBaseWidth, hitBarValue, value, hitBarMax), 30, flip, false);
		}
		// Head
		if (!flip)
			batch.draw(barSheet, this.getX(), this.getY(), 0f, 0f, 77, 103, 1f, 1f, 0f, 0, 309, 77, 103, flip, false);
		else
			batch.draw(barSheet, this.getX() + getWidth() - 77, this.getY(), 0f, 0f, 77, 103, 1f, 1f, 0f, 0, 309, 77, 103, flip, false);
		
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
			hitBarBaseWidth = getProgressWidth(294, hitBarValue, min, max) - getProgressWidth(294, value, min, max);
			
			// Set the new value
			this.value = value;
			
			// Set value has changed
			valueChanged = true;
		}
	}
	
	public void flip()
	{
		flip = !flip;
	}
	
}
