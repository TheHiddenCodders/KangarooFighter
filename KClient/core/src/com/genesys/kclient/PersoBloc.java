package com.genesys.kclient;

import Packets.ClientDataPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PersoBloc extends Table 
{
	/*
	 * Attributes
	 */
	
	private Image background;
	private Image kangaroo;
	private Label name, elo, eloI, games, win, losses;
	private Image winIcon, loseIcon;
	
	/*
	 * Constructors
	 */
	
	public PersoBloc(ClientDataPacket data, Skin skin)
	{
		super();
		
		this.setSize(251, 374);
		background = new Image(new Texture(Gdx.files.internal("sprites/bloc perso.png")));
		this.addActor(background);
		
		kangaroo = new Image(new Texture(Gdx.files.internal("sprites/kangourou.png")));
		kangaroo.setPosition(this.getWidth() / 2 - kangaroo.getWidth() / 2, this.getHeight() - kangaroo.getHeight() - 50);
		kangaroo.getColor().a = 0.55f;
		this.addActor(kangaroo);
		
		name = new Label(data.name, skin);
		name.setStyle(new LabelStyle(skin.getFont("korean"), Color.TAN));
		name.setPosition(this.getWidth() / 2 - name.getWidth() / 2, this.getHeight() - name.getHeight() - 10);
		this.addActor(name);
		
		elo = new Label(data.elo + "", skin);
		elo.setStyle(new LabelStyle(skin.getFont("korean"), Color.TAN));
		elo.pack();
		elo.setPosition(this.getWidth() / 2 - elo.getWidth() / 2 - 15, kangaroo.getY() - elo.getHeight() - 10);
		this.addActor(elo);
		
		eloI = new Label(" elo" , skin);
		eloI.setStyle(new LabelStyle(skin.getFont("korean"), Color.WHITE));
		eloI.pack();
		eloI.setPosition(elo.getX() + elo.getWidth(), elo.getY());
		this.addActor(eloI);
		
		games = new Label(data.games + " parties" , skin);
		games.setStyle(new LabelStyle(skin.getFont("default-font"), Color.WHITE));
		games.setPosition(this.getWidth() / 2 - games.getWidth() / 2, elo.getY() - games.getHeight() - 2);
		this.addActor(games);
		
		win = new Label(data.wins + "" , skin);
		win.setStyle(new LabelStyle(skin.getFont("default-font"), Color.TAN));
		win.setPosition(this.getWidth() / 4 - win.getWidth() / 2, games.getY() - win.getHeight() - 10);
		this.addActor(win);
		
		winIcon = new Image(new Texture(Gdx.files.internal("sprites/games.png")));
		winIcon.setPosition(win.getX() + win.getWidth() + 5, win.getY());
		winIcon.scaleBy(0.2f);
		this.addActor(winIcon);
		
		loseIcon = new Image(new Texture(Gdx.files.internal("sprites/loses.png")));
		loseIcon.setPosition(this.getWidth() / 2 + this.getWidth() / 4 - loseIcon.getWidth() * 1.5f, games.getY() - 33);
		loseIcon.scaleBy(0.2f);
		this.addActor(loseIcon);
		
		losses = new Label(data.looses + "" , skin);
		losses.setStyle(new LabelStyle(skin.getFont("default-font"), new Color(200f/255f, 80f/255f, 80f/255f, 1)));
		losses.setPosition(loseIcon.getX() + loseIcon.getWidth() + 5, win.getY());
		this.addActor(losses);
		
	}
	
	public void flipKangaroo()
	{
		kangaroo.setDrawable(new Image(new Texture(Gdx.files.internal("sprites/kangourouflipped.png"))).getDrawable());
	}
	
	public void setBackground(Texture texture)
	{
		background.setDrawable(new Image(texture).getDrawable());
	}
}
