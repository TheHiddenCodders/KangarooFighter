package BlocsDisplays;

import Class.Display;
import Packets.LadderPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class LadderDisplay extends Display 
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Label title;
	private TextButton search;
	private TextField nameField;
	
	private Label[] rank, name, games, wins, loses, elo;
	/*
	 * Constructors
	 */
	
	public LadderDisplay(LadderPacket ladder, Skin skin) 
	{
		super(skin);
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/ladder/background.png")));
		
		// Title
		title = new Label("Classement", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5); 
		addActor(title);
		
		// Add button
		search = new TextButton("Rechercher", skin);
		search.setPosition(5, 5);
		addActor(search);
		
		// Add name
		nameField = new TextField("", skin);
		nameField.setPosition(search.getX() + search.getWidth(), 5);
		nameField.setSize(getWidth() - 10 - search.getWidth(), search.getHeight());
		addActor(nameField);
		
		// Make labels tabs
		rank = new Label[9];
		name = new Label[9];
		games = new Label[9];
		wins = new Label[9];
		loses = new Label[9];
		elo = new Label[9];
		
		// Init them
		for (int i = 0; i < rank.length; i++)
		{
			rank[i] = new Label("", skin);
			name[i] = new Label("", skin);
			games[i] = new Label("", skin);
			wins[i] = new Label("", skin);
			loses[i] = new Label("", skin);
			elo[i] = new Label("", skin);
			elo[i].setColor(Color.TAN);
			
			addActor(rank[i]);
			addActor(name[i]);
			addActor(games[i]);
			addActor(wins[i]);
			addActor(loses[i]);
			addActor(elo[i]);
		}
	}

	@Override
	public void refresh(Object data)
	{
		// TODO: Change with ladder data packet
		if (data.getClass().isAssignableFrom(LadderPacket.class))
		{
			LadderPacket packet = (LadderPacket) data;
			for (int i = 0; i < rank.length; i++)
			{
				rank[i].setText(String.valueOf(packet.ladderList.get(i).pos));
				name[i].setText(packet.ladderList.get(i).name);
				games[i].setText(String.valueOf(packet.ladderList.get(i).games));
				wins[i].setText(String.valueOf(packet.ladderList.get(i).wins));
				loses[i].setText(String.valueOf(packet.ladderList.get(i).looses));
				elo[i].setText(String.valueOf(packet.ladderList.get(i).elo));
				
				rank[i].setPosition(0 + 58 / 2 - rank[i].getWidth() / 2, getHeight() - 120 - i * 26.2f);
				name[i].setPosition(23 + 92 / 2 - name[i].getWidth() / 2, getHeight() - 120 - i * 26.2f);
				games[i].setPosition(390 + 92 / 2 - games[i].getWidth() / 2, getHeight() - 120 - i * 26.2f);
				wins[i].setPosition(484 + 92 / 2 - wins[i].getWidth() / 2, getHeight() - 120 - i * 26.2f);
				loses[i].setPosition(576 + 92 / 2 - loses[i].getWidth() / 2, getHeight() - 120 - i * 26.2f);
				elo[i].setPosition(660 + 92 / 2 - elo[i].getWidth() / 2, getHeight() - 120 - i * 26.2f);
			}
		}
	}

}
