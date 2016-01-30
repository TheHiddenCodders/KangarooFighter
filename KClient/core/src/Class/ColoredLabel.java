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
		
		parts = new ArrayList<Label>();
		
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
				parts.get(parts.size() - 1).setText(tempPart);
				tempPart = "";
				
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
				parts.get(parts.size() - 1).setText(tempPart);
				tempPart = "";
				
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
		
		// Make last part
		parts.get(parts.size() - 1).setText(tempPart);
		
		// Set pos first part
		parts.get(0).setPosition(getX(), getY());
		parts.get(0).pack();
		addActor(parts.get(0));
		
		// Concat them
		for (int i = 1; i < parts.size(); i++)
		{
			parts.get(i).pack();
			parts.get(i).setPosition(parts.get(i - 1).getX() + parts.get(i - 1).getWidth(), getY());
			System.err.println(parts.get(i).getX());
			addActor(parts.get(i));
		}		
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
}
