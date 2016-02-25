package Class;

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
		// TODO: background = new Image(new Texture(Gdx.files.internal("sprites/gamestage/timer.png")));
		// TODO: addActor(background);
		
		// TODO: setSize(background.getWidth(), background.getHeight());
		
		time = new Label("", labelStyle);
		time.setPosition(100 / 2 - time.getWidth() / 2 , 20);
		addActor(time);
	}
	
	/*
	 * Methods
	 */
	
	public void refresh(float time)
	{
		this.time.setText(String.valueOf((int) (time / 1000)));
	}
}
