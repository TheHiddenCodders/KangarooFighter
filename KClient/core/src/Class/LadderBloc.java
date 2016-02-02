package Class;

import Packets.PlayerPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class LadderBloc extends Bloc
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	
	
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
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
