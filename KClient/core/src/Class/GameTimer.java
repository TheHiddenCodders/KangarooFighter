package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameTimer extends Table 
{
	/*
	 * Attributes
	 */
	
	
	
	/*
	 * Components
	 */
	
	private Image background;
	private Label time;
	
	/*
	 * Constructors
	 */
	
	public GameTimer(LabelStyle labelStyle) 
	{
		super();
		
		// Make components
		background = new Image(new Texture(Gdx.files.internal("sprites/gamestage/gametimer.png")));
		addActor(background);
		
		setSize(background.getWidth(), background.getHeight());
		
		time = new Label("", labelStyle);
		time.setPosition(getWidth() / 2 - time.getWidth() / 2 , getHeight() / 2 - time.getHeight() / 2);
		addActor(time);
	}
	
	/*
	 * Methods
	 */
	
	public void refresh(float time)
	{
		boolean needPack = false;
		
		// Check if pack is needed or not
		if (this.time.getText().toString().length() != String.valueOf(time).length())
			needPack = true;
			
		// Set new text
		this.time.setText(String.valueOf(time));	
		
		// If pack is need, pack and set the new position
		if (needPack)
		{
			this.time.pack();
			this.time.setPosition(getWidth() / 2 - this.time.getWidth() / 2 , getHeight() / 2 - this.time.getHeight() / 2);
		}
	}
}
