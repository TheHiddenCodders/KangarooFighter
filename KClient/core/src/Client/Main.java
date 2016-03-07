package Client;

import Class.ConnectedStage;
import Class.Player;
import Packets.DisconnexionPacket;
import Packets.Packets;
import Packets.ServerInfoPacket;
import Stages.ServerAccesStage;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends ApplicationAdapter
{	
	/*
	 * Attributes
	 */
	
	private Stage stage;
	public Network network;
	public Skin skin;
	public Preferences prefs;
	public Player player;
	public ServerInfoPacket serverInfos;
	
	@Override
	public void create () 
	{
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		// Load fonts
		BitmapFont koreanFont = new BitmapFont(Gdx.files.internal("ui/korean.fnt"));
		BitmapFont korean32Font = new BitmapFont(Gdx.files.internal("ui/korean-32.fnt"));
		BitmapFont arialFont = new BitmapFont(Gdx.files.internal("ui/arial.fnt"));
		BitmapFont dotum32Font = new BitmapFont(Gdx.files.internal("ui/dotum-32.fnt"));
		
		// Add fonts 
		skin.add("korean", koreanFont, BitmapFont.class);
		skin.add("korean-32", korean32Font, BitmapFont.class);
		skin.add("arial", arialFont, BitmapFont.class);
		skin.add("dotum-32", dotum32Font, BitmapFont.class);
		
		// Get prefs
		prefs = Gdx.app.getPreferences("KangarooFighters");
		stage = new ServerAccesStage(this);
	}

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void pause()
	{
		/* If goes off app, send network deco
		if (!Gdx.app.getType().equals(ApplicationType.Desktop))
			if (network != null)
				network.send(-1);*/
		
		super.pause();
	}
	
	@Override
	public void dispose()
	{
		if (network != null)
			network.send((Packets) new DisconnexionPacket() );
		
		super.dispose();
	}	
	/**
	 * Set the new stage to draw
	 * @param stage
	 */
	public void setStage(ConnectedStage stage)
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
