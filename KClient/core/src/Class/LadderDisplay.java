package Class;

import Packets.PlayerPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LadderDisplay extends Display 
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
	
	public LadderDisplay(PlayerPacket[] ladderPlayers, Skin skin) 
	{
		super(skin);
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/ladder/background.png")));
	}

	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub

	}

}
