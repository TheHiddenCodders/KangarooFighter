package com.genesys.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.genesys.kclient.Main;

public class PreGameStage extends Stage 
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	
	/*
	 *  Components
	 */

	/*
	 * Constructors
	 */
	public PreGameStage(Main main)
	{
		super();
		this.main = main;
	}
	
	@Override
	public void act(float delta)
	{								
		super.act(delta);
	}
}
