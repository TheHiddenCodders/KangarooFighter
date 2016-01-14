package com.genesys.kclient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class ExpandableBloc extends Table 
{
	/*
	 * Attributes
	 */
	
	protected Image background;
	protected Image close;
	private ClickListener clickListener;
	
	/*
	 * Constructor
	 */
	
	public ExpandableBloc()
	{
		super();
		
		close = new Image(new Texture(Gdx.files.internal("sprites/close.png")));
		close.setPosition(725, 110);
		close.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(2)));
		
		close.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				retract();
				super.clicked(event, x, y);
			}
		});
		
		this.addListener(clickListener = new ClickListener(){});
	}
	
	/*
	 * Methods
	 */
	
	public abstract void expand();
	public abstract void retract();
	
	/*
	 * Getters & Setters
	 */
	
	public boolean isPressed()
	{
		return clickListener.isVisualPressed();
	}
}
