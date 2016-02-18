package Class;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ColoredLabel extends Table
{
	/*
	 * Attributes
	 */
	
	private ArrayList<Label> parts;
	private String tempPart = "";
	private int cursor = 0; 
	private int state = -1; 
	private Skin skin;

	// -1 = begin
	// 0 = read a char 
	// 1 = read a <
	// 2 = read a c after 1
	// 3 = read a number after 2
	// 4 = read a > after 3
	// 5 = read a / after 1
	// 6 = read a > after 5
	
	/*
	 * Constructors
	 */
	
	public ColoredLabel(String sentence, Skin skin, Color... colors) 
	{
		super();
		
		this.skin = skin;
		
		parts = new ArrayList<Label>();
		
		parse(sentence, colors);
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		// Set pos first part
		parts.get(0).setPosition(getX(), getY());
		parts.get(0).pack();
		
		// Concat them
		for (int i = 1; i < parts.size(); i++)
		{
			parts.get(i).pack();
			parts.get(i).setPosition(parts.get(i - 1).getX() + parts.get(i - 1).getWidth(), getY());
		}	
		super.setPosition(x, y);
	}
	
	public void setText(String sentence, Color... colors)
	{
		for (Label part : parts)
			part.remove();
			
		parts.clear();
		
		parse(sentence, colors);
	}
	
	private void parse(String sentence, Color... colors)
	{
		//System.out.println("Parsing :" + sentence);
		tempPart = "";
		state = -1;
		cursor = 0;
		
		// While cursor has not pass through entire string
		while (cursor < sentence.length())
		{
			// Last read = nothing
			if (state == -1)
			{		
				// Make a new part any way
				parts.add(new Label("", skin));
				
				// If read a new tag char
				if (sentence.charAt(cursor) == '<')
				{
					state = 1;
				}
				else
				{
					tempPart += sentence.charAt(cursor);
					state = 0;
				}
			}
			
			// Last read = char
			else if (state == 0)
			{
				if (sentence.charAt(cursor) == '<')
				{
					state = 1;
				}
				else
				{
					tempPart += sentence.charAt(cursor);
					state = 0;
				}
			}
			
			// Last read = <
			else if (state == 1)
			{				
				if (sentence.charAt(cursor) == 'c')
				{					
					state = 2;
				}
				
				else if (sentence.charAt(cursor) == '/')
				{
					state = 5;
				}
			}
			
			// Last read = c
			else if (state == 2)
			{	
				// Close last part
				if (parts.size() != 0)
				{
					parts.get(parts.size() - 1).setText(tempPart);
					tempPart = "";
				}
				
				// Make new part
				parts.add(new Label("", skin));
				
				// Color part
				parts.get(parts.size() - 1).setColor(colors[Integer.valueOf("" + sentence.charAt(cursor))]);
				
				state = 3;
			}
			
			// Last read = number after c
			else if (state == 3)
			{
				if (sentence.charAt(cursor) == '>')
				{					
					state = 4;
				}
				else
				{
					System.err.println("Le char " + sentence.charAt(cursor) + " n'est pas autorisé ici, était attendu un '>'");
				}
			}
			
			// Last read = > after a number
			else if (state == 4)
			{
				if (sentence.charAt(cursor) == '<')
				{
					state = 1;
				}
				else
				{
					tempPart += sentence.charAt(cursor);
					state = 0;
				}
			}
			
			// Last read = / after a <
			else if (state == 5)
			{				
				state = 6;
			}
			
			// Last read = > after a /
			else if (state == 6)
			{			
				// Close last part
				if (parts.size() != 0)
				{
					parts.get(parts.size() - 1).setText(tempPart);
					tempPart = "";
				}
				
				// Make new part
				parts.add(new Label("", skin));
				
				if (sentence.charAt(cursor) == '<')
				{
					state = 1;
				}
				else
				{
					tempPart += sentence.charAt(cursor);
					state = 0;
				}
			}
			
			//System.err.println("Cursor | State = " + cursor + "(" + sentence.charAt(cursor) + ") | " + state + " [" + tempPart + "]");
			cursor++;
		}
		
		int width = 0;
		int height = 0;
		
		// Make last part
		if (parts.size() > 0)
		{
			parts.get(parts.size() - 1).setText(tempPart);
			
			// Set pos first part
			parts.get(0).setPosition(getX(), getY());
			parts.get(0).pack();
			addActor(parts.get(0));
			
			width = (int) parts.get(0).getWidth();
		}
		
		// Concat them
		for (int i = 1; i < parts.size(); i++)
		{
			parts.get(i).pack();
			parts.get(i).setPosition(parts.get(i - 1).getX() + parts.get(i - 1).getWidth(), getY());
			addActor(parts.get(i));
			
			width += parts.get(i).getWidth();
			height = (int) parts.get(i).getHeight();
		}
		
		setSize(width, height);
		//System.out.println("Coloredlabel width: " + getWidth());
	}	
	
	@Override
	public void setX(float x) 
	{			
		super.setX(x);
		
		parts.get(0).setX(0);
		
		for (int i = 1; i < parts.size(); i++)
		{
			//System.out.println(parts.get(i - 1).getX());
			parts.get(i).setX(parts.get(i - 1).getX() + parts.get(i - 1).getWidth());	
		}
	}
}
