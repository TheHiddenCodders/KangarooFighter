package com.genesys.kclient;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.genesys.client.Network;
import com.genesys.stages.EndGameStage;
import com.genesys.stages.GameStage;
import com.genesys.stages.HomeStage;
import com.genesys.stages.PreGameStage;
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
		BitmapFont korean32Font = new BitmapFont(Gdx.files.internal("ui/korean-32.fnt"));
		BitmapFont arialFont = new BitmapFont(Gdx.files.internal("ui/arial.fnt"));
		skin.add("korean", koreanFont, BitmapFont.class);
		skin.add("korean-32", korean32Font, BitmapFont.class);
		skin.add("arial", arialFont, BitmapFont.class);
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
		
		if (stage.getClass().isAssignableFrom(HomeStage.class) || stage.getClass().isAssignableFrom(PreGameStage.class) || stage.getClass().isAssignableFrom(EndGameStage.class))
			stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.7f)));
		else if (stage.getClass().isAssignableFrom(GameStage.class))
			stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(3)));
		
		Gdx.input.setInputProcessor(stage);
		this.stage = stage;
		
	}
}
