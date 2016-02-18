package Class;

import Packets.ServerInfoPacket;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ServerInfoLabel extends ColoredLabel
{
	/*
	 * Attributes
	 */
	
	private int currentText = 0;
	private String[] texts;
	private Color[] colors;
	
	/*
	 * Constructors
	 */
	
	public ServerInfoLabel(ServerInfoPacket packet, Skin skin, Color... colors)
	{
		super("", skin, colors);
		this.colors = colors;
		
		// Init tab
		texts = new String[4];
		
		// Fill texts with packet
		fillTexts(packet);
		
		// Set text
		setText(texts[currentText], colors);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void act(float delta)
	{
		if (getX() + getWidth() > 0)
		{
			moveBy(-1, 0);
		}
		else
		{
			currentText++;
			
			if (currentText == texts.length)
				currentText = 0;
			
			setText(texts[currentText], colors);
			setX(800);
		}
		super.act(delta);
	};
	
	private void fillTexts(ServerInfoPacket packet)
	{
		texts[0] = "Vous etes au total <c0>" + packet.nKangaroosRegistered + "</> kangourous a vous <c0>massacrer</> gratuitement. Pas mal !";
		texts[1] = "En ce moment meme <c0>" + packet.nKangaroosOnline + "</> kangourous <c0>en ligne</>. Peut mieux faire !";
		texts[2] = "Au total <c0>" + packet.nGamesPlayed + "</> kangourous sont <c0>morts</> au combat. Triste histoire..";
		texts[3] = "Actuellement <c0>" + packet.nGamesOnline + "</> combats <c0>font rage</>. Que demander de plus ? Ah oui, plus de sang.";		
	}
	
	public void refresh(ServerInfoPacket packet)
	{
		fillTexts(packet);
		setText(texts[currentText], colors);
		pack();
	}

}
