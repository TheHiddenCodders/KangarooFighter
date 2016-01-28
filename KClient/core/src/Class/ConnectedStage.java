package Class;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.genesys.kclient.Main;

public abstract class ConnectedStage extends Stage
{
	/*
	 * Attributes
	 */
	
	protected Main main;
	
	/** This value need to be set to true when data that apply a change is received */
	protected boolean serverAnswered;
	
	/** This value permit to organize packet reception */
	protected int receiveState = 0;
	
	/*
	 * Constructors
	 */
	
	public ConnectedStage(Main main)
	{
		super();
		
		this.main = main;
		init();
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Update method
	 * @param delta (time between two frames)
	 */
	public void act(float delta)
	{
		if (serverAnswered)
		{
			onServerDataReceived();
			serverAnswered = false;
		}
		
		super.act(delta);
	};
	
	/**
	 * This method init the stage
	 */
	public void init()
	{
		askServerData();
		initComponents();
		addListeners();
		addActors();
	}
	
	/**
	 * Call this method in setServerData when a or some data that you need come to stage
	 */
	protected void serverAnswered()
	{
		serverAnswered = true;
	}
	
	/** This method will ask to the server the data that stage needs to init */
	protected abstract void askServerData();
	
	/** This method will tell the stage how to init its components */
	protected abstract void initComponents();
	
	/** This method will add listeners onto actors */
	protected abstract void addListeners();
	
	/** This method will add animation onto stage and actors */
	protected abstract void addAnimations();
	
	/** This method will add actors on stage */
	protected abstract void addActors();
	
	/** This method will tell the stage what to do when server data are acquire */
	protected abstract void onServerDataReceived();	
	
	/** This method will set the data asked by the stage before.
	 *	The method is called by the network */
	public abstract void setServerData(Object... data);
}
