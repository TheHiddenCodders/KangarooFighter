package Stages;

import Class.ConnectedStage;
import Client.Main;
import Utils.Timer;

public class EndGameStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private Timer timer;
	
	/** When timer reach this value, pre-game stage is over */
	private static final float DELAY = 1f;
	
	/*
	 * Components
	 */
	
	

	/*
	 * Constructors
	 */

	public EndGameStage(Main main) 
	{
		super(main);
		
		// Make timer
		timer = new Timer();
	}

	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta)
	{
		if (timer.getElapsedTime() > DELAY)
			main.setStage(new HomeStage(main));
			
		super.act(delta);
	};
	
	@Override
	protected void askInitData() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initComponents() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDataNeededComponents() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addListeners() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addInitDataNeededListeners() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAnimations() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addInitDataNeededAnimations() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addActors() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addInitDataNeededActors() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDataReceived() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setData(Object data)
	{
		// TODO Auto-generated method stub
		
	}

}
