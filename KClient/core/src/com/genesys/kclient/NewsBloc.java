package com.genesys.kclient;

import Packets.NewsPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class NewsBloc extends ExpandableBloc
{
	/*
	 * Components
	 */
	
	private Image background;
	private Image banner;
	private Image edging;
	private Label name;
	
	/*
	 * Constructors
	 */
	
	public NewsBloc(NewsPacket newsData, Skin skin)
	{
		super();
		
		setSize(250, 152);
		background = new Image(new Texture(Gdx.files.internal("sprites/bloc news.png")));
		addActor(background);
		
		Pixmap bannerPixmap = new Pixmap(newsData.banner, 0, newsData.banner.length);
		banner = new Image(new Texture(bannerPixmap));
		bannerPixmap.dispose();
		banner.setPosition(2, 2);
		addActor(banner);
		
		edging = new Image(new Texture(Gdx.files.internal("sprites/edging.png")));
		edging.setPosition(2, 2);
		addActor(edging);
		
		name = new Label(newsData.name, skin);
		name.setPosition(5, 5);
		addActor(name);
		
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void expand() 
	{
		banner.addAction(Actions.fadeOut(0));
		edging.addAction(Actions.fadeOut(0));
		name.addAction(Actions.fadeOut(0));
		background.addAction(Actions.moveTo(0, -200, 1));
		background.addAction(Actions.sizeTo(763, 349, 1));
		addActor(close);
	}
	
	@Override
	public void retract() 
	{
		banner.addAction(Actions.fadeIn(1));
		edging.addAction(Actions.fadeIn(1));
		name.addAction(Actions.fadeIn(1));
		background.addAction(Actions.moveTo(0, 0, 1));
		background.addAction(Actions.sizeTo(250, 152, 1));
		removeActor(close);
	}
}
