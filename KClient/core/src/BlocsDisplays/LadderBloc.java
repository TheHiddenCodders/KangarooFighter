package BlocsDisplays;

import Class.Bloc;
import Packets.LadderPacket;
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
	private Label[] rank, name, elo;
	
	/*
	 * Constructors
	 */
	
	public LadderBloc(LadderPacket ladder, HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(535, 235);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/ladder/background.png")));
		
		// Set display
		setDisplay(new LadderDisplay(ladder, skin));
		
		// Title
		title = new Label("Classement", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5); 
		addActor(title);
		
		// Make labels tabs
		rank = new Label[5];
		name = new Label[5];
		elo = new Label[5];
		
		// Init them
		for (int i = 0; i < rank.length; i++)
		{
			rank[i] = new Label("", skin);
			name[i] = new Label("", skin);
			elo[i] = new Label("", skin);
			elo[i].setColor(Color.TAN);
			
			addActor(rank[i]);
			addActor(name[i]);
			addActor(elo[i]);
		}
		
		refresh(ladder);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh(Object data) 
	{
		if (data.getClass().isAssignableFrom(LadderPacket.class))
		{
			LadderPacket packet = (LadderPacket) data;
			for (int i = 0; i < rank.length; i++)
			{
				// Set texts
				rank[i].setText(String.valueOf(packet.ladderList.get(i).pos));
				name[i].setText(packet.ladderList.get(i).name);
				elo[i].setText(String.valueOf(packet.ladderList.get(i).elo));
				
				// Update actor size
				rank[i].pack();
				name[i].pack();
				elo[i].pack();
				
				// Set positions according to their size
				rank[i].setPosition(0 + 40 / 2 - rank[i].getWidth() / 2, getHeight() - 65 - i * 26.5f);
				name[i].setPosition(47, getHeight() - 65 - i * 26.5f);
				elo[i].setPosition(205 + 43 / 2 - elo[i].getWidth() / 2, getHeight() - 65 - i * 26.5f);
				
				// If it's the player
				if (packet.ladderList.get(i).name == homeStage.main.player.getName())
				{
					rank[i].setColor(Color.TAN);
					name[i].setColor(Color.TAN);
					elo[i].setColor(Color.TAN);
				}
				else
				{
					rank[i].setColor(Color.LIGHT_GRAY);
					name[i].setColor(Color.LIGHT_GRAY);
					elo[i].setColor(Color.TAN);
				}
				
				// Check if it's a friend
				for (int j = 0; j < homeStage.main.player.getFriends().size(); j++)
				{
					if (packet.ladderList.get(i).name == homeStage.main.player.getFriends().get(j).name)
					{
						rank[i].setColor(Color.BLUE);
						name[i].setColor(Color.BLUE);
						elo[i].setColor(Color.BLUE);
					}
				}
			}
		}
		
		display.refresh(data);
	}
}
