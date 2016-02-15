package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public abstract class NotificationBloc extends Table 
{
	/*
	 * Attributes
	 */
	
	
	
	
	/*
	 * Components
	 */
	
	private Image background;
	private Image icone;
	private ColoredLabel msg;
	private TextButton yes, no;
	
	/*
	 * Constructors
	 */
	
	public NotificationBloc(HomeStage homeStage)
	{
		super();
		
		// Add background;
		background = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/notifications/background.png")));
		addActor(background);
		
		// Set size
		setSize(background.getWidth(), background.getHeight());
		
		// Make labels
		msg = new ColoredLabel("<c0> </>", homeStage.main.skin, Color.WHITE);
		msg.setY(20);
		addActor(msg);
		
		// Make buttons
		yes = new TextButton("Accepter", homeStage.main.skin);
		yes.setPosition(80, 5); 
		no = new TextButton("Refuser", homeStage.main.skin);
		no.setColor(Color.RED);
		no.setPosition(295, 5); 
		addActor(yes);
		addActor(no);
	}
	
	/*
	 * Methods
	 */
	
	public void setIcone(Texture icone)
	{
		this.icone = new Image(icone);
		this.icone.setPosition(5 + 70 / 2 - icone.getWidth() / 2, 5 + 70 / 2 - icone.getHeight() / 2);
		addActor(this.icone);
	}
	
	public void setMsg(String msg, Color... colors)
	{
		this.msg.setText(msg, colors);
		this.msg.setX(getWidth() / 2 - this.msg.getWidth() / 2 + 30);
		System.err.println(msg + " | " + this.msg.getX() + ":" + this.msg.getY());
	}
	
	@Override
	public void setPosition(float x, float y) 
	{
		//this.msg.setY(y);
		super.setPosition(x, y);
	}
}
