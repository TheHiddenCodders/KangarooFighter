package BlocsDisplays;

import Class.Bloc;
import Packets.NewsPacket;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
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
		
		// Edge
		edge = new Image(new Texture("sprites/homestage/blocs/news/edge.png"));
		edge.setPosition(2, 2);
		addActor(edge);
		
		// Title
		title = new Label("No title", skin); // TODO: replace by news name
		title.setPosition(4, 4);
		addActor(title);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
