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
	private boolean dataReceived;
	
	/** This value need to be set to true when init data is received */
	private boolean initDataReceived;
	
	/** Put this boolean to true if data is needed to init */
	protected boolean needDataToInit = false;
	
	/*
	 * Constructors
	 */
	
	public ConnectedStage(Main main)
	{
		super();
		this.main = main;
		
		askInitData();
		
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
		if (dataReceived)
		{
			onDataReceived();
			dataReceived = false;
		}
		
		if (initDataReceived)
		{
			initDataNeededComponents();
			initDataReceived = false;
		}
		
		super.act(delta);
	};
	
	/**
	 * This method init the non data needed components of the stage
	 */
	public void init()
	{
		initComponents();
		addListeners();
		addAnimations();
		addActors();
	}
	
	/**
	 * This method init the data needed components of the stage
	 */
	public void initOnDataReceived()
	{
		initDataNeededComponents();
		addInitDataNeededListeners();
		addInitDataNeededAnimations();
		addInitDataNeededActors();
	}
	
	/**
	 * Call this method when an update need to be done after a data has been received
	 */
	protected void dataReceived()
	{
		dataReceived = true;
	}
	
	/**
	 * Call this method when all the init data required to init has been received
	 */
	protected void initDataReceived()
	{
		initDataReceived = true;
	}	
	
	/** This method will ask to the server the data that stage needs to init */
	protected abstract void askInitData();
	
	/** This method will tell the stage how to init its components */
	protected abstract void initComponents();
	
	/** This method will tell the stage how to init its data needed components */
	protected abstract void initDataNeededComponents();
	
	/** This method will add listeners onto actors */
	protected abstract void addListeners();
	
	/** This method will add listeners onto actors */
	protected abstract void addInitDataNeededListeners();
	
	/** This method will add animation onto stage and actors */
	protected abstract void addAnimations();
	
	/** This method will add animation onto stage and actors */
	protected abstract void addInitDataNeededAnimations();
	
	/** This method will add actors on stage */
	protected abstract void addActors();	
	
	/** This method will add actors on stage */
	protected abstract void addInitDataNeededActors();	
	
	/** This method will tell the stage what to do when server data are acquired */
	protected abstract void onDataReceived();	
	
	/** This method will set the data asked by the stage before.
	 *	The method is called by the network */
	public abstract void setData(Object data);
}
