package Class;

import Packets.FriendsPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


public class FriendsDisplay extends Display 
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
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
