package Stages;

import Class.ConnectedStage;
import Client.Main;
import Packets.LoginPacket;
import Utils.ConversionUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConnexionStage extends ConnectedStage
{
	/*
	 * Attributes
	 */
	
	private LoginPacket loginPacket;
	private int debugCount = 0;
	
	/*
	 * Components
	 */
	
	private Label infoName, infoPwd, other;
	private TextField name, pwd;
	private TextButton connect, signOut;
	private Image background;
	private Table table;
	
	/*
	 * Constructors
	 */
	
	public ConnexionStage(Main main)
	{
		super(main);
		
		if (alreadyRegisteredOnPhone())
			login(main.prefs.getString("[pseudo]"), main.prefs.getString("[pwd]"));
	}

	@Override
	protected void askInitData() 
	{
		// No need to ask data on connexion stage		
	}

	@Override
	protected void initComponents()
	{
		// Make table
		table = new Table();
		
		// Background
		background = new Image(new Texture(Gdx.files.internal("sprites/background.png")));
		
		// Texts
		infoName = new Label("Pseudonyme", main.skin);
		infoPwd = new Label("Mot de passe", main.skin);
		other = new Label("", main.skin);
		
		// Textfields
		name = new TextField("", main.skin);
		pwd = new TextField("", main.skin);
		pwd.setPasswordMode(true);
		pwd.setPasswordCharacter('*');
		
		// Buttons
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

		// Set size
		connect.setWidth(95);
		connect.setHeight(30);
		connect.setPosition(this.getWidth() / 2 - connect.getWidth() / 2 - 53, this.getHeight() / 2 - connect.getHeight() - 75);
		signOut.setWidth(95);
		signOut.setHeight(30);
		signOut.setPosition(this.getWidth() / 2 - connect.getWidth() / 2 + 53, this.getHeight() / 2 - connect.getHeight() - 75);		
	}
	
	@Override
	protected void initDataNeededComponents()
	{
		// No data needed components		
	}

	@Override
	protected void addListeners()
	{
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
	}
	
	@Override
	protected void addInitDataNeededListeners() 
	{
		// No init data needed components		
	}

	@Override
	protected void addAnimations()
	{
		
	}
	
	@Override
	protected void addInitDataNeededAnimations()
	{
		// No init data needed components
	}
	
	@Override
	protected void addActors()
	{
		addActor(background);
		addActor(table);
		addActor(connect);
		addActor(signOut);
	}
	
	@Override
	protected void addInitDataNeededActors() 
	{
		// No init data needed components
	}

	@Override
	protected void onDataReceived()
	{
		// If not accepted
		if (!loginPacket.accepted)
		{
			if (loginPacket.pseudoExists && !loginPacket.pwdMatch)
				other.setText("Mauvais mot de passe");
			else if (!loginPacket.pseudoExists)
				other.setText("Ce pseudo n'existe pas");
			
			// This two lines allows infinite debug connexion
			login("debug" + debugCount, "debug");
			debugCount++;
		}
		
		// If accepted
		else
		{
			// If not registered on phone, register it
			if (!alreadyRegisteredOnPhone())
				registerOnPhone(name.getText(), pwd.getText());
			
			main.setStage(new HomeStage(main));
		}
	}

	@Override
	public void setData(Object data) 
	{
		// We can only treat one packet of type login
		if (data.getClass().isAssignableFrom(LoginPacket.class))
		{
			loginPacket = (LoginPacket) data;
			dataReceived();
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
		else if (pseudo.length() < 4 || pwd.length() < 4)
		{
			other.setText("pseudo / mot de passe : 4 caracteres minimum");
		}
		else
		{
			other.setText("Merci de remplir tous les champs");
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
	 * This method will register pseudo and crypted password onto phone
	 * @param pseudo
	 * @param pwd
	 */
	private void registerOnPhone(String pseudo, String pwd)
	{
		main.prefs.putString("[pseudo]", pseudo);
		main.prefs.putString("[pwd]", ConversionUtils.sha1(pwd));
		main.prefs.flush();
	}	
}
