package com.genesys.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.genesys.kclient.Main;

import Packets.LoginPacket;

public class InscriptionStage extends Stage 
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	
	// Components
	private Label info, other;
	private TextField name;
	private TextButton connect;
	
	// Triggers
	private boolean loggedIn;

	/*
	 * Constructors
	 */
	public InscriptionStage(Main main)
	{
		super();
		this.main = main;
		
		// Check if already registered, if it is, login with known pseudo
		if (alreadyRegistered())
			login(main.prefs.getString("[pseudo]"));
		
		else
		{
			// Make components
			info = new Label("Pseudonyme", main.skin);
			other = new Label("", main.skin);
			name = new TextField("", main.skin);
			connect = new TextButton("Connexion", main.skin);
			
			// Place them
			name.setPosition(Gdx.graphics.getWidth() / 2 - name.getWidth() / 2, Gdx.graphics.getHeight() / 2 - name.getHeight() / 2);
			info.setPosition(Gdx.graphics.getWidth() / 2 - info.getWidth() / 2, name.getY() + name.getHeight() + 2);
			connect.setWidth(name.getWidth());
			connect.setPosition(name.getX(), name.getY() - name.getHeight() - 2);
			other.setPosition(Gdx.graphics.getWidth() / 2 - info.getWidth() / 2, connect.getY() + connect.getHeight() - 2);
			other.setColor(1, 0.2f, 0.2f, 1);
			
			// Add them to the stage
			this.addActor(info);
			this.addActor(name);
			this.addActor(connect);
			this.addActor(other);
		}
	}
	
	@Override
	public void act(float delta)
	{			
		// If login then go to home stage
		if (loggedIn)
		{
			// If isn't already registered, make prefs
			if (!alreadyRegistered())
			{
				main.prefs.putString("[pseudo]", name.getText());
				main.prefs.flush();
			}
			
			main.setStage(new HomeStage(main));
		}
		else
		{
			login("debug");
		}
				
		// Input update
		if (Gdx.input.justTouched())
		{
			if (connect.isPressed())
				login(name.getText());
		}
				
		super.act(delta);
	}
	
	@Override
	public void draw()
	{
		super.draw();
	}
	
	/*
	 * Inherited methods
	 */	
	/**
	 * Send a login attempt to the server with pseudo
	 * @param pseudo
	 */
	private void login(String pseudo)
	{
		// Make a packet with the pseudo
		LoginPacket logPacket = new LoginPacket();
		logPacket.pseudo = pseudo;
		
		// Send it
		main.network.send(logPacket);
	}
	
	/**
	 * Check if the device is already registered
	 * @return
	 */
	private boolean alreadyRegistered()
	{	
		if (main.prefs.getString("[pseudo]", null) != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Setter for the server
	 */
	public void loggedIn()
	{
		loggedIn = true;
	}

}
