package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils
{
	public static ArrayList<File> getPlayersFiles()
	{
		ArrayList<File> playersFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players"));

		for (File file : directory.listFiles())
			playersFiles.add(file);
		
		return playersFiles;
	}
	
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
	
	public static void newPlayer(String pseudo, String pwd)
	{
		File playerDirectory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players/" + pseudo));
		playerDirectory.mkdirs();
		
		File friendsDirectory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players/" + pseudo + "/friends"));
		friendsDirectory.mkdirs();
		
		File pwdFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players/" + pseudo + "/pwd"));
		
		try 
		{
			pwdFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(pwdFile));
			writer.write(pwd);
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		File dataFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players/" + pseudo + "/data"));
		
		try 
		{
			dataFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			writer.write("[games]:0");
			writer.write("[wins]:0");
			writer.write("[looses]:0");
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
}
