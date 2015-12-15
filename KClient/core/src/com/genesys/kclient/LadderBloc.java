package com.genesys.kclient;

import Packets.ClientDataPacket;
import Packets.LadderDataPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LadderBloc extends Table
{
	/*
	 * Attributes
	 */
	
	private Image background;
	private Label title;
	private Label[] pos, name, elo;
	
	/*
	 * Constructors
	 */
	
	public LadderBloc(ClientDataPacket clientData, LadderDataPacket ladderData, Skin skin)
	{
		super();	
		
		this.setSize(251, 174);
		background = new Image(new Texture(Gdx.files.internal("sprites/bloc ladder.png")));
		this.addActor(background);
		
		title = new Label("Classement", new LabelStyle(skin.getFont("korean"), Color.TAN));
		title.setPosition(this.getWidth() / 2 - title.getWidth() / 2, this.getHeight() - title.getHeight() - 5);
		this.addActor(title);
		
		int beginningPos;
		
		// If not enough player
		if (ladderData.ladder.size() < 5)
		{
			beginningPos = 0;
			pos = new Label[ladderData.ladder.size()];
			name = new Label[ladderData.ladder.size()];
			elo = new Label[ladderData.ladder.size()];
		}
		else
		{
			if (ladderData.playerPos == 1 || ladderData.playerPos == 2)
				beginningPos = 0;
			else if (ladderData.playerPos == ladderData.ladder.size())
				beginningPos = ladderData.playerPos - 5;
			else if (ladderData.playerPos == ladderData.ladder.size() - 1)
				beginningPos = ladderData.playerPos - 4;
			else
				beginningPos = ladderData.playerPos - 2;
			
			pos = new Label[5];
			name = new Label[5];
			elo = new Label[5];
		}
		
		// Fill the labels with the players
		for (int i = 0; i < pos.length; i++)
		{
			// Parse line and make label
			pos[i] = new Label(ladderData.ladder.get(beginningPos + i).split(" | ")[0], skin);
			pos[i].setPosition(38 / 2 - pos[i].getWidth() / 2, this.getHeight() - 40 - i * (pos[i].getHeight() + 2) - pos[i].getHeight());
			this.addActor(pos[i]);
			
			name[i] = new Label(ladderData.ladder.get(beginningPos + i).split(" | ")[2], skin);
			name[i].setPosition(50, this.getHeight() - 40 - i * (name[i].getHeight() + 2) - name[i].getHeight());
			this.addActor(name[i]);
			
			elo[i] = new Label(ladderData.ladder.get(beginningPos + i).split(" | ")[4], skin);
			elo[i].setPosition(205, this.getHeight() - 40 - i * (elo[i].getHeight() + 2) - elo[i].getHeight());
			this.addActor(elo[i]);
			
			// If it's the player, color it in tan
			if (name[i].getText().toString().equals(clientData.name))
			{
				pos[i].setColor(Color.TAN);
				name[i].setColor(Color.TAN);
				elo[i].setColor(Color.TAN);
			}
		}
	}
}
