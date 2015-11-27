package Kangaroo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Utils.Rectangle;

public class States 
{
	public final static int idle = 0;
	public final static int punch = 1;
	public final static int guard = 2;
	public final static int movement = 3;
	
	//private ArrayList<Hitbox> hitboxes;
	private ArrayList<ServerAnimation> anims;
	private int currentState;
	
	private Timer timer;
	//private Kangaroo k;
	
	public States(/*Kangaroo k*/)
	{
		
		//this.k = k;
		setState(idle);
		
		timer = new Timer();
		anims = new ArrayList<ServerAnimation>();
		
		
		/*
		 * TODO : Change with a file reading
		 */
		// IDLE - use for punchin (TEST)
		ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
		
		// FRAME 1
		hitboxes.add( new Hitbox() );
		hitboxes.get(0).addBox(new Rectangle(130, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(0).addBox(new Rectangle(172, 202 - 100 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(0).addBox(new Rectangle(164, 202 - 108 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(0).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// FRAME 2
		hitboxes.add( new Hitbox() );
		hitboxes.get(1).addBox(new Rectangle(132, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(1).addBox(new Rectangle(168, 202 - 100 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(1).addBox(new Rectangle(168, 202 - 108 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(1).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// FRAME 3
		hitboxes.add( new Hitbox() );
		hitboxes.get(2).addBox(new Rectangle(134, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(2).addBox(new Rectangle(164, 202 - 100 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(2).addBox(new Rectangle(172, 202 - 108 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(2).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// FRAME 4
		hitboxes.add( new Hitbox() );
		hitboxes.get(3).addBox(new Rectangle(136, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(3).addBox(new Rectangle(164, 202 - 104 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(3).addBox(new Rectangle(172, 202 - 104 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(3).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// FRAME 5
		hitboxes.add( new Hitbox() );
		hitboxes.get(4).addBox(new Rectangle(136, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(4).addBox(new Rectangle(164, 202 - 108 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(4).addBox(new Rectangle(172, 202 - 100 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(4).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// FRAME 6
		hitboxes.add( new Hitbox() );
		hitboxes.get(5).addBox(new Rectangle(134, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(5).addBox(new Rectangle(168, 202 - 108 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(5).addBox(new Rectangle(168, 202 - 100 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(5).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// FRAME 7
		hitboxes.add( new Hitbox() );
		hitboxes.get(6).addBox(new Rectangle(132, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(6).addBox(new Rectangle(172, 202 - 108 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(6).addBox(new Rectangle(164, 202 - 100 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(6).addBox(new Rectangle(82, 0, 81, 140)); // BODY		
		
		// FRAME 8
		hitboxes.add( new Hitbox() );
		hitboxes.get(7).addBox(new Rectangle(130, 202 - 64, 52, 64)); // HEAD
		hitboxes.get(7).addBox(new Rectangle(172, 202 - 104 - 34, 40, 34)); // LEFT PUNCH
		hitboxes.get(7).addBox(new Rectangle(164, 202 - 104 - 34, 40, 34)); // RIGHT PUNCH
		hitboxes.get(7).addBox(new Rectangle(82, 0, 81, 140)); // BODY
		
		// create a void anim for idle
		anims.add( new ServerAnimation() );
		// add the idle anim to the punch for testing
		anims.add ( new ServerAnimation() );
		anims.get(1).setHitboxes(hitboxes);
		anims.get(1).setMode(ServerAnimation.onePlay);
	}
	


	public int getState() 
	{
		return currentState;
	}

	public boolean setState(int currentState) 
	{
		this.currentState = currentState;
		
		if (this.currentState == punch)
		{
			timer.schedule(new TimerTask(){

				@Override
				public void run() 
				{
					// Launch punch anim
					anims.get(1).start(States.this);
					
					//setState(idle);
					//k.getClient().send(k.getUpdatePacket());
				}
				
			}, 0);
			
			return true;
		}
		else if (currentState == idle)
		{
			//k.getClient().send(k.getUpdatePacket());
			return true;
		}
		
		return false;
	}
	
} 