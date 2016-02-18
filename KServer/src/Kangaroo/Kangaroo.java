package Kangaroo;

import java.util.ArrayList;

import Animations.ServerAnimation;
import Packets.KangarooPacket;
import enums.States;

/**
 * The Kangaroo class manage one client. A Kangaroo is create when a client is connecting on the server. 
 * If a client send a request, there is a Kangaroo with this IP.
 * 
 * @author Kurond
 *
 */
public class Kangaroo 
{
	public KangarooPacket kStats;

	private ArrayList<ServerAnimation> animations;
	private int currentAnimation = 0;
	
	private int	win = 0;
	//private Timer speedTimer;

	
	/**
	 * Create the kangaroo with the client.
	 * 
	 * @param cp
	 */
	public Kangaroo(int posX, int posY)
	{
		kStats.state = States.idle.ordinal();
		
		kStats.x = posX;
		kStats.y = posY;
		kStats.speed = 10;
		
		kStats.health = 100;
		kStats.damage = 5;
		
		initAnim();
	}
	
	/**
	 * Init the name of the kangaroo.
	 * 
	 * Load animations
	 */
	private void initAnim()
	{
		animations = new ArrayList<ServerAnimation>();
		
		animations.add(new ServerAnimation("assets/anims/idle.hba"));
		animations.add(new ServerAnimation("assets/anims/walk.hba")); // Movement
		animations.add(new ServerAnimation("assets/anims/hit.hba"));
		animations.add(new ServerAnimation("assets/anims/guard.hba")); // Guard 
		animations.add(new ServerAnimation("assets/anims/leftpunch.hba"));
		animations.add(new ServerAnimation("assets/anims/rightpunch.hba"));
		//animations.add(new ServerAnimation("assets/anims/idleGuard.hba"));
		
		animations.get(States.idle.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.movement.ordinal()).setMode(ServerAnimation.foreverPlay);
		animations.get(States.hit.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.guard.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.leftPunch.ordinal()).setMode(ServerAnimation.onePlay);
		animations.get(States.rightPunch.ordinal()).setMode(ServerAnimation.onePlay);
		//animations.get(States.idleGuard.ordinal()).setMode(ServerAnimation.foreverPlay);
	}
	
	public void flip()
	{
		kStats.flip = !kStats.flip;
		
		// TODO : flip anim
		
		/*for (int i = 0; i < animations.size(); i++)
			animations.get(i).flip();*/
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Launch specified animation
	 * @param index of the animation to launch
	 */
	public void launchAnimation(int index)
	{
		if (animations.get(currentAnimation).getMode() == ServerAnimation.foreverPlay)
			animations.get(currentAnimation).stop();
		
		currentAnimation = index;
		
		animations.get(index).start();
	}
	
	/**
	 * Launch specified animation
	 * @param state of the animation to launch
	 */
	public void launchAnimation(States state)
	{
		//if (animations.get(currentAnimation).getMode() == ServerAnimation.foreverPlay)
		animations.get(currentAnimation).stop();
		
		currentAnimation = state.ordinal();
	
		animations.get(state.ordinal()).start();
	}

	public int getWins() 
	{
		return win;
	}
}