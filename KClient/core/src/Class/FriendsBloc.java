package Class;

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
	
	public FriendsBloc() 
	{
		super();
		
		setPosition(0, 0);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background.png")));
		
		System.err.println(getChildren().size);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
