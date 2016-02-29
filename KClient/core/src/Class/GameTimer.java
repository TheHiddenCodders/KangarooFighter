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
		this.time.setText(String.valueOf(time));
		this.time.pack();
		this.time.setPosition(getWidth() / 2 - this.time.getWidth() / 2 , getHeight() / 2 - this.time.getHeight() / 2);
	}
}
