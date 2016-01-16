package com.genesys.stages;

import Packets.GamePacket;
import Utils.Timer;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.genesys.kclient.Main;

public class PreGameStage extends Stage 
{
	private static final float timeBeforeGame = 0.5f; // In s
	
	/*
	 * Attributes
	 */
	
	/** Used as a wire between stage to access client for example */
	public Main main;
	private Timer timer;
	private GamePacket gamePacket;
	
	/*
	 *  Components
	 */

	/*
	 * Constructors
	 */
	
	public PreGameStage(Main main, GamePacket gamePacket)
	{
		super();
		this.main = main;
		
		this.gamePacket = gamePacket;
		timer = new Timer();
	}
	
	@Override
	public void act(float delta)
	{					
		timer.update();
		
		if (timer.getElapsedTime() > timeBeforeGame)
			main.setStage(new GameStage(main, gamePacket));
		
		super.act(delta);
	}
}
