package BlocsDisplays;

import Class.Display;
import Packets.HomePacket;
import Packets.PlayerPacket;

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
	
	public LadderDisplay(PlayerPacket[] ladderPlayers, Skin skin) 
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
		if (data.getClass().isAssignableFrom(HomePacket.class))
		{
			HomePacket packet = (HomePacket) data;
			for (int i = 0; i < rank.length; i++)
			{
				rank[i].setText(String.valueOf(packet.ladderPlayers[i].pos));
				name[i].setText(packet.ladderPlayers[i].name);
				games[i].setText(String.valueOf(packet.ladderPlayers[i].games));
				wins[i].setText(String.valueOf(packet.ladderPlayers[i].wins));
				loses[i].setText(String.valueOf(packet.ladderPlayers[i].looses));
				elo[i].setText(String.valueOf(packet.ladderPlayers[i].elo));
			}
		}
	}

}
