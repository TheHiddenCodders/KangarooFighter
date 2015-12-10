package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Kangaroo.Kangaroo;
import Packets.ClientDataPacket;

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
			writer.write("[elo]:1000\n");
			writer.write("[streak]:0\n");
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
	
	public static ClientDataPacket getPlayerDatas(Kangaroo k)
	{
		ClientDataPacket packet = new ClientDataPacket();
		
		for (File file : getPlayersFiles())
		{
			if (file.getName().equals(k.getName()))
			{
				File dataFile = new File(file.getPath() + "/data");
				
				try
				{
					BufferedReader reader = new BufferedReader(new FileReader(dataFile));
					
					packet.name = reader.readLine().split(":")[1];
					packet.games = Integer.parseInt(reader.readLine().split(":")[1]);
					packet.wins = Integer.parseInt(reader.readLine().split(":")[1]);
					packet.looses = Integer.parseInt(reader.readLine().split(":")[1]);
					packet.elo = Integer.parseInt(reader.readLine().split(":")[1]);
					packet.streak = Integer.parseInt(reader.readLine().split(":")[1]);
					
					reader.close();
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return packet;
	}
}
