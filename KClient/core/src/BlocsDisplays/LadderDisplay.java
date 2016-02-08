package BlocsDisplays;

import Class.Display;
import Packets.LadderPacket;
import Packets.SearchLadderPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LadderDisplay extends Display 
{
	/*
	 * Attributes
	 */

	
	
	/*
	 * Components
	 */
	
	private Label title;
	private TextButton search, prev, next, me;
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
		
		// Add search button
		search = new TextButton("Rechercher", skin);
		search.setPosition(5, 5);
		addActor(search);
		
		// Add name
		nameField = new TextField("", skin);
		nameField.setPosition(search.getX() + search.getWidth(), 5);
		nameField.setSize(getWidth() - 190 - search.getWidth(), search.getHeight());
		addActor(nameField);
		
		// Add browsing buttons
		prev = new TextButton("", skin);
		prev.setWidth(60);
		addActor(prev);
		next = new TextButton("", skin);
		next.setWidth(60);
		addActor(next);
		me = new TextButton("Moi", skin);
		me.setWidth(60);
		me.setColor(Color.TAN);
		addActor(me);
		
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
		if (data.getClass().isAssignableFrom(LadderPacket.class))
		{
			LadderPacket packet = (LadderPacket) data;
			
			// Set min and max rank
			final int rankMin = packet.ladderList.get(0).pos;
			final int rankMax = rankMin + 9;
			
			// Init labels
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
				
				// If it's the player
				if (packet.ladderList.get(i).name == homeStage.main.player.getName())
				{
					rank[i].setColor(Color.TAN);
					name[i].setColor(Color.TAN);
					games[i].setColor(Color.TAN);
					wins[i].setColor(Color.TAN);
					loses[i].setColor(Color.TAN);
					elo[i].setColor(Color.TAN);
				}
				else
				{
					rank[i].setColor(Color.LIGHT_GRAY);
					name[i].setColor(Color.LIGHT_GRAY);
					games[i].setColor(Color.LIGHT_GRAY);
					wins[i].setColor(Color.LIGHT_GRAY);
					loses[i].setColor(Color.LIGHT_GRAY);
					elo[i].setColor(Color.TAN);
				}
				
				// Check if it's a friend
				for (int j = 0; j < homeStage.main.player.getFriends().length; j++)
				{
					if (packet.ladderList.get(i).name == homeStage.main.player.getFriends()[j].name)
					{
						rank[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
						name[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
						games[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
						wins[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
						loses[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
						elo[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
					}
				}
			}
			
			// Init browsing buttons
			if (packet.ladderList.get(0).pos > 9)
			{
				prev.setText((rankMin - 9) + " - " + (rankMax + 9));
			}
			else
			{
				prev.setText("-");
				prev.setColor(Color.GRAY);
				prev.setTouchable(Touchable.disabled);
			}
			
			// Set parameters of buttons
			next.setText((rankMin + 9) + " - " + (rankMax + 9));
			next.setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			prev.setWidth(60);
			next.setWidth(60);
			prev.setPosition(nameField.getX() + nameField.getWidth(), 5);
			next.setPosition(prev.getX() + prev.getWidth(), 5);
			me.setPosition(next.getX() + next.getWidth(), 5);			
			
			// Add listeners
			prev.addListener(new ClickListener() 
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					SearchLadderPacket packet = new SearchLadderPacket();
					packet.pos = rankMin - 9;
					homeStage.main.network.send(packet);
					super.clicked(event, x, y);
				}
			});
			
			next.addListener(new ClickListener() 
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					SearchLadderPacket packet = new SearchLadderPacket();
					packet.pos = rankMax + 1;
					homeStage.main.network.send(packet);
					super.clicked(event, x, y);
				}
			});
			
			me.addListener(new ClickListener() 
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					SearchLadderPacket packet = new SearchLadderPacket();
					packet.name = homeStage.main.player.getName();
					homeStage.main.network.send(packet);
					super.clicked(event, x, y);
				}
			});
		}
	}

}
