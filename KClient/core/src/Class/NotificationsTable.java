package Class;

import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class NotificationsTable extends Table 
{
	/*
	 * Attributes
	 */
	
	private HomeStage homeStage;
	private Label notif;
	private Image notifIco;
	
	/*
	 * Constructors
	 */
	
	public NotificationsTable(HomeStage homeStage)
	{
		super();
		
		this.homeStage = homeStage;
		notif = new Label(String.valueOf(homeStage.main.player.getNotifications().size()), homeStage.main.skin);
		notifIco = new Image(new Texture(Gdx.files.internal("sprites/homestage/notification.png")));
		
		notifIco.setPosition(getX() + notif.getWidth() + 5, getY() + 3);
		
		setWidth(notif.getWidth() + notifIco.getWidth());
		addActor(notif);
		addActor(notifIco);
	}
	
	/*
	 * Methods
	 */
	
	public void refresh()
	{
		System.out.println("Refresh notifications table. Notifications size : " + homeStage.main.player.getNotifications().size());
		if (homeStage.main.player.getNotifications().size() < 100)
			notif.setText(String.valueOf(homeStage.main.player.getNotifications().size()));
		else
			notif.setText("99+");
		
		notif.pack();
		//notifIco.setPosition(getX() + notif.getWidth() + 20, getY() + 3);
		setWidth(notif.getWidth() + notifIco.getWidth());
	}
	
	/*
	 * Getters - Setters
	 */
}
