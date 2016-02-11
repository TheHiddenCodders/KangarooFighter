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
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background.png")));
		
		// Set display
		setDisplay(new FriendsDisplay(skin));
		
		// Title
		title = new Label("Amis", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5); 
		addActor(title);
		
		// Make tabs 
		name = new Label[5];
		status = new Image[5];
		
		for (int j = 0; j < name.length; j++)
		{
			name[j] = new Label("", skin);
			
			name[j].setPosition(38, getHeight() - 52 - j * 26.5f);
			
			name[j].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			
			status[j] = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/offline.png")));
			status[j].setVisible(false);
			
			addActor(name[j]);
			addActor(status[j]);
		}
		
		// Set background if no friends
		if (homeStage.main.player.getFriends().size() == 0)
			setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/background1.png")));
		
		// We don't need any object till friends are stored in player
		refresh(null);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh(Object data) 
	{
		if (homeStage.main.player.getFriends().size() > 0)
		{
			// Init them
			for (int i = 0; i < homeStage.main.player.getFriends().size(); i++)
			{
				name[i].setText(homeStage.main.player.getFriends().get(i).name);
				
				if (homeStage.main.player.getFriends().get(i).online)
					status[i].setDrawable(new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/online.png"))).getDrawable());
				else
					status[i].setDrawable(new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/offline.png"))).getDrawable());
			
				status[i].setVisible(true);
			}
		}
	}
}
