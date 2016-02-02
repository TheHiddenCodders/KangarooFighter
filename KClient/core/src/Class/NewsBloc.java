package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class NewsBloc extends Bloc
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
	
	public NewsBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set background (news position are setted by homestage
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/news/background.png")));
		
		// Set display
		setDisplay(new NewsDisplay(skin));
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
