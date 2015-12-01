package com.genesys.kclient;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.genesys.client.Network;
import com.genesys.stages.ServerAccesStage;

public class Main extends ApplicationAdapter
{	
	/*
	 * Attributes
	 */
	private Stage stage;
	public Network network;
	public Skin skin;
	public Preferences prefs;
	
	@Override
	public void create () 
	{
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		BitmapFont koreanFont = new BitmapFont(Gdx.files.internal("ui/korean.fnt"));
		skin.add("korean-font", koreanFont, BitmapFont.class);
		prefs = Gdx.app.getPreferences("data");
		setStage(new ServerAccesStage(this));
	}

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void pause()
	{
		if (!Gdx.app.getType().equals(ApplicationType.Desktop))
			if (network != null)
				network.send(-1);
		
		super.pause();
	}
	
	@Override
	public void dispose()
	{
		if (network != null)
			network.send(-1);
		
		super.dispose();
	}	
	/**
	 * Set the new stage to draw
	 * @param stage
	 */
	public void setStage(Stage stage)
	{
		if (this.stage != null)
			System.out.println("Going to " + stage.getClass().getSimpleName() + " from " + this.stage.getClass().getSimpleName());
		else
			System.out.println("Going to " + stage.getClass().getSimpleName());
		
		if (network != null)
		{
			System.out.println("Network updated");
			network.setStage(stage);
		}
		else
			System.out.println("Network not updated");
		
		Gdx.input.setInputProcessor(stage);
		this.stage = stage;
		
	}
}
