package Kangaroo;


public class States 
{
	public final static int idle = 0;
	public final static int movement = 1;
	public final static int leftPunch = 2;
	public final static int rightPunch = 3;
	public final static int hit = 4; // When kangaroo get hit
	
	private int currentState;

	public States()
	{
		setState(idle);
	}

	public int getState() 
	{
		return currentState;
	}

	public void setState(int currentState) 
	{
		this.currentState = currentState;
	}
	
} 