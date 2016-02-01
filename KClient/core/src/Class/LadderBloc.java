package Class;

import Stages.HomeStage;

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
	
	public LadderBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(530, 235);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/ladder/background.png")));
		
		// Set display
		setDisplay(new LadderDisplay());
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
