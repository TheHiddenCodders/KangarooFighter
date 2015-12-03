package Kangaroo;

import java.util.ArrayList;

public class States 
{
	public final static int idle = 0;
	public final static int hit = 1; // When kangaroo get hit
	public final static int leftPunch = 2;
	public final static int rightPunch = 3;
	public final static int movement = 0;
	
	//private ArrayList<Hitbox> hitboxes;
	private ArrayList<ServerAnimation> anims;
	private int currentState;

	public States()
	{
		setState(idle);
		
		anims = new ArrayList<ServerAnimation>();
		
		anims.add(new ServerAnimation("assets/anims/idle.hba"));
		anims.add(new ServerAnimation("assets/anims/hit.hba"));
		anims.add(new ServerAnimation("assets/anims/leftpunch.hba"));
		anims.add(new ServerAnimation("assets/anims/rightpunch.hba"));
		anims.get(0).setMode(ServerAnimation.foreverPlay);
		anims.get(1).setMode(ServerAnimation.onePlay);
		anims.get(2).setMode(ServerAnimation.onePlay);
		anims.get(3).setMode(ServerAnimation.onePlay);
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