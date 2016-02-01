package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class PersoBloc extends Bloc
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
	
	public PersoBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(270, 42.5f);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/perso/background.png")));
		
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
