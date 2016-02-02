package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Main class of displays displayed by blocs
 * @author Nerisma
 *
 */
public abstract class Display extends Table
{
	/*
	 * Attributes
	 */
	
	private HomeStage homeStage;
	protected Skin skin;
	private boolean animated = false;
	
	/*
	 * Components
	 */
	
	protected Image background;
	protected Image closeButton;
	
	/*
	 * Constructors
	 */
	
	public Display(Skin skin)
	{
		super();
		this.skin = skin;
		
		// Add fade in so adding a display to a stage will make him fade in
		addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		
		// Make background
		background = new Image();
		
		// Make close button
		closeButton = new Image(new Texture(Gdx.files.internal("sprites/homestage/displays/closebutton.png")));
		
		// Add listener to close button
		closeButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				remove();
				super.clicked(event, x, y);
			}
		});
		
		// Add them
		addActor(background);
		addActor(closeButton);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta) 
	{
		if (getActions().size == 0 && animated)
			super.remove();
			
		super.act(delta);
	};
	
	/**
	 * Override remove method so bloc will fade out before beeing removed of stage
	 */
	@Override
	public boolean remove() 
	{
		addAction(Actions.fadeOut(1));
		homeStage.showBlocs();
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
		
		// Set close button position
		closeButton.setPosition(getWidth() - closeButton.getWidth() - 10, getHeight() - closeButton.getHeight() - 10);
	}
	
	/** 
	 * Setter for homeStage
	 * @param homeStage
	 */
	public void setHomeStage(HomeStage homeStage)
	{
		this.homeStage = homeStage;
		this.skin = homeStage.main.skin;
	}
	
	/** This method refresh the display */
	public abstract void refresh();
}
