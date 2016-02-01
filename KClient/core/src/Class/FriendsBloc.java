package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class FriendsBloc extends Bloc
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
	
	public FriendsBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(530, 50);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background.png")));
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
