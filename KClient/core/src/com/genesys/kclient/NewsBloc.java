package com.genesys.kclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class NewsBloc extends Table
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
	
	public NewsBloc(Skin skin)
	{
		super();
		
		this.setSize(251, 114);
		background = new Image(new Texture(Gdx.files.internal("sprites/bloc news.png")));
		this.addActor(background);
		
		edging = new Image(new Texture(Gdx.files.internal("sprites/edging.png")));
		edging.setPosition(2, 2);
		this.addActor(edging);
		
		name = new Label("News name", skin);
		name.setPosition(5, 5);
		this.addActor(name);
	}	
}
