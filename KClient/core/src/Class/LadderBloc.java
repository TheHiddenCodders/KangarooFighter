package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class LadderBloc extends Bloc
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
	
	public LadderBloc() 
	{
		super();
		
		// Set position and background
		setPosition(530, 235);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/ladder/background.png")));
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
