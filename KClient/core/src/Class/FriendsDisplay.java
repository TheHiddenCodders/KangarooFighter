package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class FriendsDisplay extends Display 
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	
	
	/*
	 * Constructors
	 */
	
	public FriendsDisplay() 
	{
		super();
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/friends/background.png")));
	}

	@Override
	public void refresh() 
	{
		// TODO Auto-generated method stub

	}

}
