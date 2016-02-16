package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import Kangaroo.Player;
import Packets.NewsPacket;
import ServerInfo.FileDateComparator;

public class ServerUtils
{
	/** players : a reference to the server list*/
	public static ArrayList<Player> players;
	
	/****************************************************************
	 * PLAYERS METHODS												*
	 ****************************************************************/
	
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
	 * @param k the kangaroo 
	 * @return the data file of the player
	 */	
	public static File getPlayerDataFile(Player p)
	{		
		return getPlayerDataFile(p.getName());
	}
	
	/**
	 * @param name the name of the kangaroo 
	 * @return the data file of the player
	 */	
	public static File getPlayerDataFile(String name)
	{
		for (File file : getPlayersFiles())
		{
			if (file.getName().equals(name))
			{
				return new File(file.getPath() + "/data");
			}
		}
		
		return null;
	}
	
	/**
	 * @param k the kangaroo 
	 * @return the data file of the player
	 */	
	public static File getPlayerFriendsFile(Player p)
	{		
		return getPlayerFriendsFile(p.getName());
	}
	
	/**
	 * @param name the name of the kangaroo 
	 * @return the data file of the player
	 */	
	public static File getPlayerFriendsFile(String name)
	{
		File friendsFile = null;
		
		for (File file : getPlayersFiles())
		{
			if (file.getName().equals(name))
			{
				friendsFile = new File(file.getPath() + "/friends");
				break;
			}
		}
		
		return friendsFile;
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
		
		File friendsFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players/" + pseudo + "/friends"));
		File pwdFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players/" + pseudo + "/pwd"));
		
		try 
		{
			friendsFile.createNewFile();
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
	 * Check if the player is online
	 * @param kangaroos
	 * @return
	 */
	public static boolean isPlayerOnline(Player p)
	{
		return isPlayerOnline(p.getName());
	}
	
	/**
	 * Check if the player is online
	 * @param kangaroos
	 * @return
	 */
	public static boolean isPlayerOnline(String name)
	{
		for (Player p : players)
		{
			System.out.println(p.getName().equals(name));
			if (p.getName().equals(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * This method will set a field of kangaroo data
	 * @param k the kangaroo
	 * @param key the name of the field to update
	 * @param value the transform to apply to the value
	 */
	public static void setData(Player p, String key, int value)
	{
		setData(p.getName(), key, value);	
	}
	
	/**
	 * This method will set a field of kangaroo data
	 * @param k the kangaroo
	 * @param key the name of the field to update
	 * @param value the transform to apply to the value
	 */
	public static void setData(String kName, String key, int value)
	{
		File dataFile = getPlayerDataFile(kName);
		
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
	public static void updateData(Player p, String key, int value)
	{
		File dataFile = getPlayerDataFile(p);
		
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
	
	/****************************************************************
	 * GAMES METHODS												*
	 ****************************************************************/
	
	
	/**
	 * This method give access to the games directory.
	 * @return the files contains by the directory
	 */
	public static ArrayList<File> getGamesFiles()
	{
		ArrayList<File> gamesFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Games"));

		for (File file : directory.listFiles())
			gamesFiles.add(file);
		
		return gamesFiles;
	}
	
	/****************************************************************
	 * NEWS METHODS													*
	 ****************************************************************/
	
	/**
	 * @return all the news in files
	 */
	public static ArrayList<File> getNewsFiles()
	{
		ArrayList<File> newsFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/News"));

		for (File file : directory.listFiles())
			newsFiles.add(file);
		
		return newsFiles;
	}
	
	/**
	 * @param name
	 * @return the news files of the single news named name
	 */
	public static ArrayList<File> getSingleNewsFiles(String name)
	{
		ArrayList<File> newsFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/News/" + name));

		for (File file : directory.listFiles())
			newsFiles.add(file);
		
		return newsFiles;
	}
	
	/** 
	 * @param name
	 * @return the news packet of the news named name
	 */
	public static NewsPacket getNewsPacket(String name, String receiverIP)
	{
		NewsPacket packet = new NewsPacket();
		packet.name = name;
		
		ArrayList<File> newsFiles = getSingleNewsFiles(name);
	
		try
		{
			packet.setIp(receiverIP);
			packet.banner = Files.readAllBytes(newsFiles.get(0).toPath());
			packet.news = Files.readAllBytes(newsFiles.get(1).toPath());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return packet;		
	}
	
	/**
	 * @param name
	 * @return the news files
	 */
	public static File getLastNewsFiles()
	{
		ArrayList<File> directory = getNewsFiles();
		
		Collections.sort(directory, new FileDateComparator());
		
		return directory.get(0);
	}
	
	/**
	 * @param name
	 * @return the news files
	 */
	public static File getLastBeforeNewsFiles()
	{
		ArrayList<File> directory = getNewsFiles();
		
		Collections.sort(directory, new FileDateComparator());
		
		return directory.get(1);
	}
	
	/****************************************************************
	 * FRIENDS METHODS												*
	 ****************************************************************/
	
	/**
	 * Add the kangaroo friend to the friend list of k
	 * @param k
	 * @param friend
	 */
	public static void addFriend(Player k, Player friend)
	{
		addFriend(k.getName(), friend.getName());
	}
	
	/**
	 * Add the kangaroo friend to the friend list of k
	 * @param k
	 * @param friend
	 */
	public static void addFriend(String kName, String friendName)
	{
		File kFriendFile = ServerUtils.getPlayerFriendsFile(kName);
		File friendFriendFile = ServerUtils.getPlayerFriendsFile(friendName);
		
		// Write the name in the friends files
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(kFriendFile, true));
			
			writer.write(friendName + "\n");
			writer.flush();	
			writer.close();
			
			writer = new BufferedWriter(new FileWriter(friendFriendFile, true));
			
			writer.write(kName + "\n");
			
			writer.flush();	
			writer.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}	
	
	/****************************************************************
	 * RESETING METHODS												*
	 ****************************************************************/
	
	/**
	 * This method delete all the games contained in the games folder
	 */
	public static void resetGames()
	{
		ArrayList<File> games = getGamesFiles();
		
		for (int i = 0; i < games.size(); i++)
			games.get(i).delete();
	}
	
	/**
	 * This method will reset the friens of the kangaroo k
	 * @param k
	 */
	public static void resetPlayerFriends(Player p)
	{
		resetPlayerFriends(p.getName());
	}
	
	/**
	 * This method will reset the friens of the kangaroo k
	 * @param k
	 */
	public static void resetPlayerFriends(String name)
	{
		try 
		{
			new PrintWriter(getPlayerFriendsFile(name)).close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Reset all the friends of all the players
	 */
	public static void resetPlayersFriends()
	{
		ArrayList<File> players = getPlayersFiles();
		
		for (int i = 0; i < players.size(); i++)
			resetPlayerFriends(players.get(i).getName());
	}
	
}
