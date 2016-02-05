package BlocsDisplays;

import Class.Bloc;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class FriendsBloc extends Bloc
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Label title;
	
	/*
	 * Constructors
	 */
	
	public FriendsBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(535, 50);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background.png")));
		
		// Set display
		setDisplay(new FriendsDisplay(skin));
		
		// Title
		title = new Label("Amis", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5); 
		addActor(title);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh(Object data) {
		// TODO Auto-generated method stub

	}
}
