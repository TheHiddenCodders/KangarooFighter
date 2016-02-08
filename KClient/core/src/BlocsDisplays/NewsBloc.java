package BlocsDisplays;

import Class.Bloc;
import Packets.NewsPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class NewsBloc extends Bloc
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Image edge;
	private Label title;
	private Image banner;
	
	/*
	 * Constructors
	 */
	
	public NewsBloc(NewsPacket newsPacket, HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set background (news position are setted by homestage
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/news/background.png")));
		
		// Set display
		setDisplay(new NewsDisplay(newsPacket, skin));
		
		// Banner
		Pixmap pixBanner = new Pixmap(newsPacket.banner, 0, newsPacket.banner.length);
		banner = new Image(new Texture(pixBanner));
		banner.setPosition(2, 2);
		pixBanner.dispose();
		addActor(banner);
		
		// Edge
		edge = new Image(new Texture("sprites/homestage/blocs/news/edge.png"));
		edge.setPosition(2, 2);
		addActor(edge);
				
		// Title
		title = new Label(newsPacket.name, skin);
		title.setPosition(4, 4);
		addActor(title);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh(Object data) {

	}
}
