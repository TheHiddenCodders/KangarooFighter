package Class;

import Packets.GameServerPacket;

public class Kangaroo extends AnimatedSprite
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public Kangaroo() 
	{
		super();
	}
	
	public Kangaroo(GameServerPacket gamePacket)
	{
		super();
		
		setPosition(gamePacket.x, gamePacket.y);
	}
}
