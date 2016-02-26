package BlocsDisplays;

import Class.Display;
import Packets.NewsPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class NewsDisplay extends Display 
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Image news;
	
	/*
	 * Constructors
	 */
	
	public NewsDisplay(NewsPacket newsPacket, Skin skin) 
	{
		super(skin);
		
		// Set position and background
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/displays/news/background.png")));
		
		// Make image from news packet
		Pixmap temp = new Pixmap(newsPacket.news, 0, newsPacket.news.length);
		news = new Image(new Texture(temp));
		temp.dispose();
		news.setPosition(2, 2);
		addActor(news);
	}

	@Override
	public void refresh(Object data)
	{
		// TODO Auto-generated method stub

	}

}
