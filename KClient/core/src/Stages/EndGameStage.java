package Stages;

import Class.ConnectedStage;
import Class.Game;
import Client.Main;
import Packets.RoundResultPacket;
import Utils.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class EndGameStage extends ConnectedStage 
{
	/*
	 * Attributes
	 */
	
	private Timer timer;
	private Game game;
	private String winner;
	
	/** When timer reach this value, pre-game stage is over */
	private static final float DELAY = 5f;
	
	/*
	 * Components
	 */
	
	private Image header;
	private Image background;
	private Image global, round1, round2, round3;

	/*
	 * Constructors
	 */

	public EndGameStage(Main main, Game game) 
	{
		super(main);		
		
		// Store game 
		this.game = game;
		
		// Make timer
		timer = new Timer();
		
		initDataReceived();
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
		background = new Image(new Texture(Gdx.files.internal("sprites/endgamestage/background.png")));		
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
		addActor(background);		
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
