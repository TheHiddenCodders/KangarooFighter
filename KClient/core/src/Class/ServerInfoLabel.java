package Class;

import Packets.ServerInfoPacket;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ServerInfoLabel extends ColoredLabel
{
	/*
	 * Attributes
	 */
	
	private String[] texts;
	
	/*
	 * Constructors
	 */
	
	public ServerInfoLabel(ServerInfoPacket packet, Skin skin, Color[] colors)
	{
		super("", skin, colors);
		
		texts = new String[4];
		
		fillTexts(packet);
	}
	
	/*
	 * Methods
	 */
	
	public void fillTexts(ServerInfoPacket packet)
	{
		texts[0] = "Il y a actuellement";
	}
	
	public void refresh(ServerInfoPacket packet)
	{
		
	}

}
