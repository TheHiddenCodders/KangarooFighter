package Class;

import Packets.PlayerPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class LadderBloc extends Bloc
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Label title;
	
	/*
	 * Constructors
	 */
	
	public LadderBloc(PlayerPacket[] ladderPlayers, HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(535, 235);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/ladder/background.png")));
		
		// Set display
		setDisplay(new LadderDisplay(ladderPlayers, skin));
		
		// Title
		title = new Label("Classement", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5); 
		addActor(title);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
