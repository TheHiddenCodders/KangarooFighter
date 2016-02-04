package BlocsDisplays;

import Class.Display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class FriendsDisplay extends Display 
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Label title;
	private TextButton add;
	private TextField name;
	
	/*
	 * Constructors
	 */
	
	public FriendsDisplay(Skin skin) 
	{
		super(skin);
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/friends/background.png")));
		
		// Title
		title = new Label("Amis", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5);  
		addActor(title);
		
		// Add button
		add = new TextButton("Ajouter", skin);
		add.setPosition(5, 5);
		addActor(add);
		
		// Add name
		name = new TextField("", skin);
		name.setPosition(add.getX() + add.getWidth(), 5);
		name.setSize(getWidth() - 10 - add.getWidth(), add.getHeight());
		addActor(name);
	}

	@Override
	public void refresh() 
	{
		// TODO Auto-generated method stub

	}

}
