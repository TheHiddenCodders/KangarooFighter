package com.genesys.kclient;

import Packets.FriendsDataPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FriendsBloc extends Table
{
	/*
	 * Attributes
	 */
	
	private Image background;
	private Label title;
	private Label[] name;
	private Image online[];
	
	/*
	 * Constructors
	 */
	
	public FriendsBloc(FriendsDataPacket friendsData, Skin skin)
	{
		super();	
		
		this.setSize(251, 174);
		
		refresh(friendsData, skin);
	}
	
	public void refresh(FriendsDataPacket friendsData, Skin skin)
	{
		this.clear();
		
		background = new Image(new Texture(Gdx.files.internal("sprites/bloc friend.png")));
		this.addActor(background);
		
		title = new Label("Amis", new LabelStyle(skin.getFont("korean"), Color.WHITE));
		title.setPosition(this.getWidth() / 2 - title.getWidth() / 2, this.getHeight() - title.getHeight() - 5);
		this.addActor(title);

		// If no friends
		if (friendsData.friendsName.isEmpty())
		{
			background.setDrawable(new Image(new Texture(Gdx.files.internal("sprites/bloc friend without friends.png"))).getDrawable());
		}
		else if (friendsData.friendsName.size() >= 5)
		{
			online = new Image[5];
			name = new Label[5];
		}
		else
		{
			online = new Image[friendsData.friendsName.size()];
			name = new Label[friendsData.friendsName.size()];
		}
		
		// Fill the labels with the players, starting by online ones
		int onlines = 0;
		for (int i = 0; i < friendsData.friendsName.size(); i++)
		{			
			// If online
			if (friendsData.friendsOnline.get(i))
			{
				// Parse line and make label			
				name[onlines] = new Label(friendsData.friendsName.get(i), skin);
				name[onlines].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
				name[onlines].setPosition(30, this.getHeight() - 40 - onlines * (name[onlines].getHeight() + 2) - name[onlines].getHeight());
				
				// Online image
				online[onlines] = new Image(new Texture(Gdx.files.internal("sprites/online.png")));
				
				online[onlines].setPosition(8, this.getHeight() - 40 - onlines * (name[onlines].getHeight() + 2) - name[onlines].getHeight() / 2 - 5);
				this.addActor(name[onlines]);	
				this.addActor(online[onlines]);
				
				onlines++;
			}
		}
			
		// Fill the labels with the offline players
		int offlines = 0;
		for (int i = 0; i < friendsData.friendsName.size(); i++)
		{	
			// If online
			if (!friendsData.friendsOnline.get(i))
			{
				// Parse line and make label			
				name[onlines + offlines] = new Label(friendsData.friendsName.get(i), skin);
				name[onlines + offlines].setColor(105f / 255f, 124f / 255f, 201f / 255f, 1);
				name[onlines + offlines].setPosition(30, this.getHeight() - 40 - (onlines + offlines) * (name[onlines + offlines].getHeight() + 2) - name[onlines + offlines].getHeight());
				
				// Online image
				online[onlines + offlines] = new Image(new Texture(Gdx.files.internal("sprites/offline.png")));
				
				online[onlines + offlines].setPosition(8, this.getHeight() - 40 - (onlines + offlines) * (name[onlines + offlines].getHeight() + 2) - name[onlines + offlines].getHeight() / 2 - 5);
				this.addActor(name[onlines + offlines]);	
				this.addActor(online[onlines + offlines]);
				
				offlines++;
			}
		}
		
	}
}
