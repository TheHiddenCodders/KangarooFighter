package BlocsDisplays;

import Class.Display;
import Packets.FriendRequestPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class FriendsDisplay extends Display 
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Label title;
	private TextButton add;
	private TextField nameField;
	
	private Label[] rank, name, games, wins, loses, elo;
	private Image[] status;
	/*
	 * Constructors
	 */
	
	public FriendsDisplay(Skin skin) 
	{
		super(skin);
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/friends/background.png")));
		
		// Title
		title = new Label("Amis", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(getWidth() / 2 - title.getWidth() / 2, getHeight() - title.getHeight() - 5);  
		addActor(title);
		
		// Add button
		add = new TextButton("Ajouter", skin);
		add.setPosition(5, 5);
		addActor(add);
		
		// Add name
		nameField = new TextField("", skin);
		nameField.setPosition(add.getX() + add.getWidth(), 5);
		nameField.setSize(getWidth() - 10 - add.getWidth(), add.getHeight());
		addActor(nameField);
		
		// Make tabs
		rank = new Label[9];
		name = new Label[9];
		games = new Label[9];
		wins = new Label[9];
		loses = new Label[9];
		elo = new Label[9];
		status = new Image[9];
		
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
			status[i] = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/offline.png")));
			
			rank[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			name[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			games[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			wins[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			loses[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
			elo[i].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);			
			
			status[i].setVisible(false);
			
			addActor(rank[i]);
			addActor(name[i]);
			addActor(games[i]);
			addActor(wins[i]);
			addActor(loses[i]);
			addActor(elo[i]);
			addActor(status[i]);
		}
	}

	@Override
	public void refresh(Object data) 
	{		
		System.err.println("FriendsDisplay.refresh()");
		if (homeStage != null)
		{
			// Init labels
			for (int i = 0; i < homeStage.main.player.getFriends().size(); i++)
			{
				// Set texts
				rank[i].setText(String.valueOf(homeStage.main.player.getFriends().get(i).pos));
				name[i].setText(homeStage.main.player.getFriends().get(i).name);
				games[i].setText(String.valueOf(homeStage.main.player.getFriends().get(i).games));
				wins[i].setText(String.valueOf(homeStage.main.player.getFriends().get(i).wins));
				loses[i].setText(String.valueOf(homeStage.main.player.getFriends().get(i).looses));
				elo[i].setText(String.valueOf(homeStage.main.player.getFriends().get(i).elo));
				
				// Set status
				if (homeStage.main.player.getFriends().get(i).online)
					status[i].setDrawable(new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/online.png"))).getDrawable());
				else
					status[i].setDrawable(new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/friends/offline.png"))).getDrawable());
				
				// Set status visible
				status[i].setVisible(true);
				
				// Update actor size
				rank[i].pack();
				name[i].pack();
				games[i].pack();
				wins[i].pack();
				loses[i].pack();
				elo[i].pack();
				
				// Set their position according to their size
				rank[i].setPosition(40 + 58 / 2 - rank[i].getWidth() / 2, getHeight() - 132 - i * 26.2f);
				name[i].setPosition(107, getHeight() - 132 - i * 26.2f);
				games[i].setPosition(396 + 92 / 2 - games[i].getWidth() / 2, getHeight() - 132 - i * 26.2f);
				wins[i].setPosition(490 + 92 / 2 - wins[i].getWidth() / 2, getHeight() - 132 - i * 26.2f);
				loses[i].setPosition(582 + 92 / 2 - loses[i].getWidth() / 2, getHeight() - 132 - i * 26.2f);
				elo[i].setPosition(676 + 92 / 2 - elo[i].getWidth() / 2, getHeight() - 132 - i * 26.2f);
				status[i].setPosition(14, getHeight() - 126 - i * 26.2f);
			}
		}
		
		// Search listener
		add.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				FriendRequestPacket packet = new FriendRequestPacket();
				
				if (!nameField.getText().isEmpty())
					packet.name = nameField.getText();				
				
				homeStage.main.network.send(packet);
				super.clicked(event, x, y);
			}
		});
	}

}
