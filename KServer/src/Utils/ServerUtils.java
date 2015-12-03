package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ServerUtils
{
	/**
	 * Get the list of the players directory
	 * @return
	 */
	public static ArrayList<File> getPlayersFiles()
	{
		ArrayList<File> playersFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players"));

		for (File file : directory.listFiles())
			playersFiles.add(file);
		
		return playersFiles;
	}
	
	/**
	 * Store a new player in the database
	 * @param pseudo
	 * @param pwd
	 */
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
			writer.write("[name]:" + pseudo + "\n");
			writer.write("[games]:0\n");
			writer.write("[wins]:0\n");
			writer.write("[looses]:0\n");
			writer.write("[elo]:0\n");
			writer.write("[streak]:0\n");
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
}
