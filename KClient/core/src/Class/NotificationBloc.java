package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
	private TextButton yes, no, ok;
	
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
		ok = new TextButton("Ok", homeStage.main.skin);
		ok.setPosition(getWidth() - ok.getWidth() - 5, 5);
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
	
	public void setAnswerStyle(String style)
	{
		if (style.equals("YesNo"))
		{
			addActor(yes);
			addActor(no);
		}
		else if (style.equals("ok"))
		{
			addActor(ok);
		}
	}
	
	public void addYesListener(ClickListener listener)
	{
		yes.addListener(listener);
	}
	
	public void addNoListener(ClickListener listener)
	{
		no.addListener(listener);
	}
	
	public void addOkListener(ClickListener listener)
	{
		no.addListener(listener);
	}
	
}
