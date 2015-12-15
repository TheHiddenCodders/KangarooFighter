package com.genesys.stages;

import Packets.ClientDataPacket;
import Packets.LadderDataPacket;
import Packets.LoginPacket;
import Utils.ConversionUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.genesys.kclient.Main;

public class ConnexionStage extends Stage 
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	private ClientDataPacket clientData;
	private LadderDataPacket ladderData;
	
	// Components
	private Label infoName, infoPwd, other;
	private TextField name, pwd;
	private TextButton connect, signOut;
	private Image background;
	private Table table;
	
	// Triggers
	private boolean loggedIn;
	private boolean serverAnswered;

	/*
	 * Constructors
	 */
	public ConnexionStage(Main main)
	{
		super();
		this.main = main;
		this.loggedIn = false;
		this.serverAnswered = false;
		this.table = new Table();
		
		// Check if already registered, if it is, login with known pseudo and known password
		if (alreadyRegisteredOnPhone())
			login(main.prefs.getString("[pseudo]"), main.prefs.getString("[pwd]"));
		
		else
		{
			// Make components
			background = new Image(new Texture(Gdx.files.internal("sprites/background.png")));
			infoName = new Label("Pseudonyme", main.skin);
			infoPwd = new Label("Mot de passe", main.skin);
			other = new Label("", main.skin);
			name = new TextField("", main.skin);
			pwd = new TextField("", main.skin);
			pwd.setPasswordMode(true);
			pwd.setPasswordCharacter('*');
			connect = new TextButton("Connexion", main.skin);
			signOut = new TextButton("S'inscrire", main.skin);
			
			// Apply some colours to them
			other.setColor(1, 0.2f, 0.2f, 1);
			signOut.setColor(1f, 0.5f, 0.5f, 1);
			
			// Organize display
			table.setFillParent(true);
			table.center();
			
			table.add(infoName);
			table.row();
			table.add(name).width(200);
			table.row();
			table.add(infoPwd);
			table.row();
			table.add(pwd).width(200);
			table.row();
			table.add(other).bottom();
			
			connect.setWidth(95);
			connect.setHeight(30);
			connect.setPosition(this.getWidth() / 2 - connect.getWidth() / 2 - 53, this.getHeight() / 2 - connect.getHeight() - 75);
			signOut.setWidth(95);
			signOut.setHeight(30);
			signOut.setPosition(this.getWidth() / 2 - connect.getWidth() / 2 + 53, this.getHeight() / 2 - connect.getHeight() - 75);
			
			connect.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					login(name.getText(), ConversionUtils.sha1(pwd.getText()));
					super.clicked(event, x, y);
				}
			});
			
			signOut.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					ConnexionStage.this.main.setStage(new InscriptionStage(ConnexionStage.this.main));
					super.clicked(event, x, y);
				}
			});
			
			// Add them to the stage
			this.addActor(background);
			this.addActor(table);
			this.addActor(connect);
			this.addActor(signOut);
		}
	}
	
	@Override
	public void act(float delta)
	{				
		// If login then go to home stage
		if (loggedIn && clientData != null)
		{
			// If isn't already registered, make prefs
			if (!alreadyRegisteredOnPhone())
			{
				main.prefs.putString("[pseudo]", name.getText());
				main.prefs.putString("[pwd]", ConversionUtils.sha1(pwd.getText()));
				main.prefs.flush();
			}
			
			main.setStage(new HomeStage(main, clientData, ladderData));
		}
		else if (serverAnswered)
		{
			// Made a new request of login
			serverAnswered = false;
			
			if (!loggedIn)
				login("debug", ConversionUtils.sha1("debug"));
		}
				
		super.act(delta);
	}
	
	/*
	 * Inherited methods
	 */	
	
	/**
	 * Send a login attempt to the server with pseudo and pwd
	 * @param pseudo
	 * @param pwd
	 */
	private void login(String pseudo, String pwd)
	{
		if (!pseudo.isEmpty() && !pwd.isEmpty())
		{
			// Make a packet with the pseudo
			LoginPacket logPacket = new LoginPacket();
			logPacket.pseudo = pseudo;
			logPacket.pwd = pwd;
			
			// Send it
			main.network.send(logPacket);
		}
	}
	
	/**
	 * Check if user has already connect with his phone
	 * @return
	 */
	private boolean alreadyRegisteredOnPhone()
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
		serverAnswered = true;
	}
	
	public void notLoggedIn(boolean pwdMatch, boolean pseudoExists)
	{	
		if (pseudoExists)
		{			
			if (!pwdMatch)
				other.setText("Mauvais mot de passe");
		}
		else
		{
			if (other != null)
				other.setText("Ce pseudo n'existe pas");
		}

		loggedIn = false;
		serverAnswered = true;
	}

	public void setClientData(ClientDataPacket packet)
	{
		clientData = packet;
	}
	
	public void setLadderData(LadderDataPacket packet)
	{
		ladderData = packet;
	}
}
