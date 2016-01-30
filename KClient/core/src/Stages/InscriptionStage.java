package Stages;

import Class.ConnectedStage;
import Packets.Packets;
import Packets.SignOutPacket;
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
import com.genesys.kclient.Main;

public class InscriptionStage extends ConnectedStage
{
	/*
	 * Attributes
	 */
	
	private SignOutPacket signOutPacket;
	
	/*
	 * Components
	 */
	
	private Label infoName, infoPwd, infoPwdConfirm, other;
	private TextField name, pwd, pwdConfirm;
	private TextButton connectAndSignout;
	private Image background;
	private Table table;
	
	/*
	 * Constructors
	 */
	
	public InscriptionStage(Main main)
	{
		super(main);
		
	}

	@Override
	protected void askServerData()
	{
		// No need to ask data on inscription stage		
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
		infoPwdConfirm = new Label("Confirmer mot de passe", main.skin);
		other = new Label("", main.skin);
		
		// TextFields
		name = new TextField("", main.skin);
		pwd = new TextField("", main.skin);
		pwd.setPasswordMode(true);
		pwd.setPasswordCharacter('*');
		pwdConfirm = new TextField("", main.skin);
		pwdConfirm.setPasswordMode(true);
		pwdConfirm.setPasswordCharacter('*');
		
		// Buttons
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
	}

	@Override
	protected void addListeners()
	{
		connectAndSignout.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				signOut(name.getText(), pwd.getText(), pwdConfirm.getText());
				super.clicked(event, x, y);
			}
		});		
	}

	@Override
	protected void addAnimations()
	{
		
	}
	
	@Override
	protected void addActors()
	{
		addActor(background);
		addActor(table);		
	}

	@Override
	protected void onServerDataReceived()
	{
		System.err.println("kk");
		// If not accepted
		if (signOutPacket.accepted)
		{
			if (signOutPacket.pseudoExists)
				other.setText("Ce pseudo deja utilise");
		}
		
		// If accepted
		else
		{
			// If not registered on phone register it
			if (!alreadyRegisteredOnPhone())
				registerOnPhone(name.getText(), pwd.getText());
			
			main.setStage(new HomeStage(main));
		}
	}

	@Override
	public void setServerData(Object... data)
	{
		// We can treat only one packet of type login, or signout
		if (data[0].getClass().isAssignableFrom(SignOutPacket.class))
		{
			signOutPacket = (SignOutPacket) data[0];
			serverAnswered();
		}
	}
	
	/**
	 * Send a signout packet
	 * @param pseudo
	 * @param pwd
	 * @param pwdConfirm
	 */
	private void signOut(String pseudo, String pwd, String pwdConfirm)
	{
		if (!pseudo.isEmpty() && !pwd.isEmpty() && !pwdConfirm.isEmpty() && pwd.equals(pwdConfirm))
		{
			// Make packet
			SignOutPacket signOutPacket = new SignOutPacket();
			signOutPacket.pseudo = pseudo;
			signOutPacket.pwd = ConversionUtils.sha1(pwd);
			
			// Send it
			main.network.send((Packets) signOutPacket);
		}
		else if (!pseudo.isEmpty() || !pwd.isEmpty() || !pwdConfirm.isEmpty())
		{
			other.setText("Merci de remplir tous les champs");
		}
		else if (pseudo.length() < 4 || pwd.length() < 4)
		{
			other.setText("pseudo / mot de passe : 4 caracteres minimum");
		}
		else
		{
			other.setText("Les mots de passes ne orrespondent pas");
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
