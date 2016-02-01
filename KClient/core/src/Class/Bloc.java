package Class;

import Stages.HomeStage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Main class of blocs displayed on home stage
 * @author Nerisma
 *
 */
public abstract class Bloc extends Table
{
	/*
	 * Attributes
	 */
	
	protected HomeStage homeStage;
	
	/*
	 * Components
	 */
	
	protected Image background;
	protected Display display;
	
	/*
	 * Constructors
	 */
	
	public Bloc(final HomeStage homeStage)
	{
		super();
		
		// Store homeStage
		this.homeStage = homeStage;
		
		// Add fade in so adding a bloc to a stage will make him fade in
		addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		// Make background
		background = new Image();
				
		// Add listener so click on a bloc will open is associated display
		addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				homeStage.addActor(display);
				homeStage.hideBlocs();
				super.clicked(event, x, y);
			}
		});
		
		// Add actors
		addActor(background);
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Override remove method so bloc will fade out before beeing removed of stage
	 */
	@Override
	public boolean remove() 
	{
		addAction(Actions.fadeOut(1));
		return super.remove();
	}
	
	/**
	 * Easy background setter
	 * @param texture
	 */
	public void setBackground(Texture texture)
	{
		background.setDrawable(new Image(texture).getDrawable());
		background.pack();
		setSize(background.getWidth(), background.getHeight());
	}
	
	/**
	 * Setter for display
	 * @param display
	 */
	public void setDisplay(Display display)
	{
		this.display = display;
		this.display.setHomeStage(homeStage);
		this.display.setPosition(13, 45);
	}
	
	/** This method refresh the bloc */
	public abstract void refresh();
}
