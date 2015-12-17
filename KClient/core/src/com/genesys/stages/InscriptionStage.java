package com.genesys.stages;

import Packets.ClientDataPacket;
import Packets.LadderDataPacket;
import Packets.LoginPacket;
import Packets.NewsPacket;
import Packets.SignOutPacket;
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

public class InscriptionStage extends Stage
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	private ClientDataPacket clientData;
	private LadderDataPacket ladderData;
	private NewsPacket lastNews, lastBeforeNews;
	
	// Components
	private Label infoName, infoPwd, infoPwdConfirm, other;
	private TextField name, pwd, pwdConfirm;
	private TextButton connectAndSignout;
	private Image background;
	private Table table;
	
	// Triggers
	private boolean signedOut;

	/*
	 * Constructors
	 */
	public InscriptionStage(Main main)
	{
		super();
		this.main = main;
		this.signedOut = false;
		this.table = new Table();
		
		// Make components
		background = new Image(new Texture(Gdx.files.internal("sprites/background.png")));
		infoName = new Label("Pseudonyme", main.skin);
		infoPwd = new Label("Mot de passe", main.skin);
		infoPwdConfirm = new Label("Confirmer mot de passe", main.skin);
		other = new Label("", main.skin);
		name = new TextField("", main.skin);
		pwd = new TextField("", main.skin);
		pwd.setPasswordMode(true);
		pwd.setPasswordCharacter('*');
		pwdConfirm = new TextField("", main.skin);
		pwdConfirm.setPasswordMode(true);
		pwdConfirm.setPasswordCharacter('*');
		connectAndSignout = new TextButton("S'inscrire et se connecter", main.skin);
		
		// Apply some colours to them
		other.setColor(1, 0.2f, 0.2f, 1);
		connectAndSignout.setColor(1, 0.5f, 0.5f, 1);
		
		// Organize display
		table.setFillParent(true);
		table.center();
		
		table.add(infoName).padRight(100);
		table.row();
		table.add(name).width(200);
		table.row();
		table.add(infoPwd).padRight(100);
		table.row();
		table.add(pwd).width(200);
		table.row();
		table.add(infoPwdConfirm).padRight(22);
		table.row();
		table.add(pwdConfirm).width(200);
		table.row();
		table.add(connectAndSignout).width(200).height(30).padTop(15);
		table.row();
		table.add(other);
		
		connectAndSignout.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				signOut(name.getText(), ConversionUtils.sha1(pwd.getText()), ConversionUtils.sha1(pwdConfirm.getText()));
				super.clicked(event, x, y);
			}
		});
		
		// Add them to the stage
		this.addActor(background);
		this.addActor(table);
	}
	
	@Override
	public void act(float delta)
	{			
		// If login then go to home stage
		if (signedOut && clientData != null && ladderData != null && lastNews != null && lastBeforeNews != null)
		{
			// If isn't already registered, make prefs
			if (!alreadyRegisteredOnPhone())
			{
				main.prefs.putString("[pseudo]", name.getText());
				main.prefs.putString("[pwd]", ConversionUtils.sha1(pwd.getText()));
				main.prefs.flush();
			}
			
			login(main.prefs.getString("[pseudo]"), main.prefs.getString("[pwd]"));
			main.setStage(new HomeStage(main, clientData, ladderData, lastNews, lastBeforeNews));
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
	
	private void signOut(String pseudo, String pwd, String pwdConfirm)
	{
		if (!pseudo.isEmpty() && !pwd.isEmpty() && !pwdConfirm.isEmpty() && pwd.equals(pwdConfirm))
		{
			// Make packet
			SignOutPacket signOutPacket = new SignOutPacket();
			signOutPacket.pseudo = pseudo;
			signOutPacket.pwd = pwd;
			
			// Send it
			main.network.send(signOutPacket);
		}
		else
		{
			other.setText("Les mots de passes ne correspondent pas");
		}
	}
	
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
	 * Check if the device is already registered
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
	
	public void signedOut()
	{
		signedOut = true;
	}
	
	public void notSignedOut(boolean pseudoExists)
	{
		signedOut = false;
		
		if (pseudoExists)
			other.setText("Ce pseudo est deja utilise");
	}
	
	public void setClientData(ClientDataPacket packet)
	{
		clientData = packet;
	}
	
	public void setLadderData(LadderDataPacket packet)
	{
		ladderData = packet;
	}
	
	public void setNewsData(NewsPacket lastNewsData, NewsPacket lastBeforeNewsData)
	{
		lastNews = lastNewsData;
		lastBeforeNews = lastBeforeNewsData;
	}
}
