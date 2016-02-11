package Class;

import Stages.HomeStage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	protected Skin skin;
	private boolean animated = false;
	
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
		this.skin = homeStage.main.skin;
		
		// Add fade in so adding a bloc to a stage will make him fade in
		addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		
		// Make background
		background = new Image();
				
		// Add listener so click on a bloc will open is associated display
		addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (display != null)
				{
					homeStage.addActor(display);
					homeStage.hideBlocs();
				}
				super.clicked(event, x, y);
			}
		});
		
		// Add actors
		addActor(background);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta)
	{
		if (animated && getActions().size == 0)
			super.remove();
		
		super.act(delta);
	};
	
	/**
	 * Override remove method so bloc will fade out before beeing removed of stage
	 */
	@Override
	public boolean remove() 
	{
		addAction(Actions.fadeOut(0.5f));
		animated = true;
		
		return true;
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
	
	/**
	 * Getter for display
	 * @return display
	 */
	public Display getDisplay()
	{
		return display;
	}
	
	/** This method refresh the bloc */
	public abstract void refresh(Object data);
}
