package BlocsDisplays;

import Class.Bloc;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
	private Label[] name;
	private Image[] status;
	
	/*
	 * Constructors
	 */
	
	public FriendsBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(535, 50);
		
		// Set display
		setDisplay(new FriendsDisplay(skin));
		
		// Title
		title = new Label("Amis", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5); 
		addActor(title);
		
		// Make tabs according to friends size and set background
		if (homeStage.main.player.getFriends().size() > 0)
		{
			setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background.png")));
			
			if (homeStage.main.player.getFriends().size() <= 5)
			{
				name = new Label[homeStage.main.player.getFriends().size()];
				status = new Image[homeStage.main.player.getFriends().size()];
			}
			else
			{
				name = new Label[5];
				status = new Image[5];
			}
		}
		// If no friends, display the no friends background
		else
		{
			setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background1.png")));
		}
		
		// Init them
		for (int i = 0; i < status.length; i++)
		{
			name[i] = new Label(homeStage.main.player.getFriends().get(i).friendsName, skin);
			
			if (homeStage.main.player.getFriends().get(i).friendsOnline)
				status[i] = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/online.png")));
			else
				status[i] = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/offline.png")));

			addActor(name[i]);
			addActor(status[i]);
		}
		
		refresh(homeStage.main.player.getFriends());
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh(Object data) {
		// TODO Auto-generated method stub

	}
}
