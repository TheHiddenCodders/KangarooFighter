package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class LadderDisplay extends Display 
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
	
	public LadderDisplay() 
	{
		super();
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/ladder/background.png")));
	}

	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub

	}

}
