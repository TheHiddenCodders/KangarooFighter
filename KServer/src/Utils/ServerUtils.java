package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Kangaroo.Game;
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
	 * @return the data file of the player
	 */	
	public static File getPlayerDataFile(Kangaroo k)
	{
		File dataFile = null;
		for (File file : getPlayersFiles())
		{
			if (file.getName().equals(k.getName()))
			{
				dataFile = new File(file.getPath() + "/data");
			}
		}
		
		return dataFile;
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
	
	/**
	 * Get data of the client
	 * @param k the kangaroo
	 * @return his data
	 */
	public static ClientDataPacket getPlayerDataPacket(Kangaroo k)
	{
		ClientDataPacket packet = new ClientDataPacket();
		
		File dataFile = getPlayerDataFile(k);
		
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
		
		return packet;
	}
	
	/**
	 * This method will get a field of kangaroo data
	 * @param k the kangaroo
	 * @param key the name of the field to get
	 * @return
	 */
	public static int getData(Kangaroo k, String key)
	{
		File dataFile = getPlayerDataFile(k);
		
		try
		{
			// Read the file
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			
			reader.readLine(); // Jump the name line
			int games = Integer.parseInt(reader.readLine().split(":")[1]);
			int wins = Integer.parseInt(reader.readLine().split(":")[1]);
			int looses = Integer.parseInt(reader.readLine().split(":")[1]);
			int elo = Integer.parseInt(reader.readLine().split(":")[1]);
			int streak = Integer.parseInt(reader.readLine().split(":")[1]);
			
			reader.close();
			
			// Apply the value modification
			if (key.equals("games"))
				return games;
			else if (key.equals("wins"))
				return wins;
			else if (key.equals("looses"))
				return looses;
			else if (key.equals("elo"))
				return elo;
			else if (key.equals("streak"))
				return streak;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * This method will set a field of kangaroo data
	 * @param k the kangaroo
	 * @param key the name of the field to update
	 * @param value the transform to apply to the value
	 */
	public static void setData(Kangaroo k, String key, int value)
	{
		File dataFile = getPlayerDataFile(k);
		
		try
		{
			// Read the file
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			
			String name = reader.readLine().split(":")[1];
			int games = Integer.parseInt(reader.readLine().split(":")[1]);
			int wins = Integer.parseInt(reader.readLine().split(":")[1]);
			int looses = Integer.parseInt(reader.readLine().split(":")[1]);
			int elo = Integer.parseInt(reader.readLine().split(":")[1]);
			int streak = Integer.parseInt(reader.readLine().split(":")[1]);
			
			reader.close();
			
			// Apply the value modification
			if (key.equals("games"))
				games = value;
			else if (key.equals("wins"))
				wins = value;
			else if (key.equals("looses"))
				looses = value;
			else if (key.equals("elo"))
				elo = value;
			else if (key.equals("streak"))
				streak = value;
			
			// Write the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			writer.write("[name]:" + name);
			writer.newLine();
			writer.write("[games]:" + games);
			writer.newLine();
			writer.write("[wins]:" + wins);
			writer.newLine();
			writer.write("[looses]:" + looses);
			writer.newLine();
			writer.write("[elo]:" + elo);
			writer.newLine();
			writer.write("[streak]:" + streak);
			writer.newLine();
			writer.flush();
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * This method will update a field of kangaroo data
	 * @param k the kangaroo
	 * @param key the name of the field to update
	 * @param value the transform to apply to the value
	 */
	public static void updateData(Kangaroo k, String key, int value)
	{
		File dataFile = getPlayerDataFile(k);
		
		try
		{
			// Read the file
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			
			String name = reader.readLine().split(":")[1];
			int games = Integer.parseInt(reader.readLine().split(":")[1]);
			int wins = Integer.parseInt(reader.readLine().split(":")[1]);
			int looses = Integer.parseInt(reader.readLine().split(":")[1]);
			int elo = Integer.parseInt(reader.readLine().split(":")[1]);
			int streak = Integer.parseInt(reader.readLine().split(":")[1]);
			
			reader.close();
			
			// Apply the value modification
			if (key.equals("games"))
				games += value;
			else if (key.equals("wins"))
				wins += value;
			else if (key.equals("looses"))
				looses += value;
			else if (key.equals("elo"))
				elo += value;
			else if (key.equals("streak"))
				streak += value;
				
			// Write the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			writer.write("[name]:" + name);
			writer.newLine();
			writer.write("[games]:" + games);
			writer.newLine();
			writer.write("[wins]:" + wins);
			writer.newLine();
			writer.write("[looses]:" + looses);
			writer.newLine();
			writer.write("[elo]:" + elo);
			writer.newLine();
			writer.write("[streak]:" + streak);
			writer.newLine();
			writer.flush();
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void save(Game game)
	{
		File gameFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Games/"
				+ (getGamesFiles().size()+1)
				+ " - " + game.getWinner().getName()
				+ " - " + game.getLooser().getName()));
		try
		{
			gameFile.createNewFile();
			
			// Get date
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			
			// Write the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(gameFile));
			writer.write("Partie ayant eu lieue au: " + dateFormat.format(date) + ".");
			writer.newLine();
			writer.write("Opposant " + game.getK1().getName() + " (" + game.getBaseElo()[0] + " elo) à " + game.getK2().getName() + " (" + game.getBaseElo()[1] + "elo).");
			writer.newLine();
			writer.write("Victoire de " + game.getWinner().getName() + " (" + game.getWinner().getHealth() + " HP)");
			writer.newLine();
			writer.write("Défaite de " + game.getLooser().getName() + " (" + game.getLooser().getHealth() + "HP)");
			writer.newLine();
			writer.write("La partie aura durée " + game.getDuration() + "s");
			writer.flush();
			writer.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<File> getGamesFiles()
	{
		ArrayList<File> gamesFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Games"));

		for (File file : directory.listFiles())
			gamesFiles.add(file);
		
		return gamesFiles;
	}
}
