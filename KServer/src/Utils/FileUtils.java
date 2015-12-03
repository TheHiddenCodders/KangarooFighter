package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils
{	
	/**
	 * Read a file and store all lines in an array
	 * @param file to read
	 * @return an array of string containing the lines
	 */
	public static ArrayList<String> readString(File file)
	{
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader;
		
		try 
		{
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			
			// Read the file
			while (line != null)
			{
				lines.add(line);
				line = reader.readLine();
			}
			
			reader.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}		
		
		return lines;
	}
}
