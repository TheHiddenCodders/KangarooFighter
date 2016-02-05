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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import Kangaroo.Player;
import Packets.ClientDataPacket;
import Packets.FriendsPacket;
import Packets.NewsPacket;

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
		
		updateLadder();
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
	 * Get data of the client
	 * @param k the kangaroo
	 * @return his data
	 */
	public static ClientDataPacket getPlayerDataPacket(Player p)
	{
		ClientDataPacket packet = new ClientDataPacket();
		
		File dataFile = getPlayerDataFile(p);

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
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return packet;
	}
	
	/**
	 * This method will get a field of kangaroo data
	 * @param name the name of the kangaroo
	 * @param key the name of the field to get
	 * @return
	 */
	public static int getData(String name, String key)
	{
		File dataFile = getPlayerDataFile(name);
		
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
	 * This method will get a field of kangaroo data
	 * @param k the kangaroo
	 * @param key the name of the field to get
	 * @return
	 */
	public static int getData(Player p, String key)
	{		
		return getData(p.getName(), key);
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
	 * LADDER METHODS												*
	 ****************************************************************/
	
	/**
	 * This method update the ladder
	 */
	public static void updateLadder()
	{
		File ladderFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Ladder/elo"));
		ArrayList<File> ladder = new ArrayList<File>();
		
		try
		{			
			// Get date
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			
			// Find make the ladder
			for (File file : getPlayersFiles())
				ladder.add(file);

			// Sort by Elo
			Collections.sort(ladder, new EloComparator());
			
			// Write the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(ladderFile));
			
			writer.write("Classement mis à jour le: " + dateFormat.format(date) + ".");
			writer.newLine();
			
			for (int i = 0; i < ladder.size(); i++)
			{
				writer.write((i + 1) + " | " + ladder.get(i).getName() + " | " + ServerUtils.getData(ladder.get(i).getName(), "elo"));
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
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
	 * @param k
	 * @return the friend file of kangaroo k
	 */
	public static FriendsPacket getFriendsDataPacket(Player p)
	{
		return getFriendsPacket(p.getName());
	}
	
	/**
	 * @param name
	 * @return the friend file of kangaroo named name
	 */
	public static FriendsPacket getFriendsPacket(String name)
	{
		FriendsPacket packet = new FriendsPacket();
		packet.friendsName = new ArrayList<String>();
		packet.friendsOnline = new ArrayList<Boolean>();
		
		File friendsFile = getPlayerFriendsFile(name);
		
		// Get friends names
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(friendsFile));
			
			String line = reader.readLine();
			
			while (line != null)
			{				
				if (line != null)
					packet.friendsName.add(line);
				
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
		
		// Order packet alphabetically
		packet.friendsName.sort(String.CASE_INSENSITIVE_ORDER);
		
		// Fill packet
		for (int i = 0; i < packet.friendsName.size(); i++)
			packet.friendsOnline.add(ServerUtils.isPlayerOnline(packet.friendsName.get(i)));
	
		return packet;
	}
	
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
	 * This method reset player stats as he just sign out to the game
	 * @param k
	 */
	public static void resetPlayer(Player p)
	{
		resetPlayer(p.getName());
	}
	
	/**
	 * This method reset player stats as he just sign out to the game
	 * @param name
	 */
	public static void resetPlayer(String name)
	{
		setData(name, "games", 0);	
		setData(name, "wins", 0);	
		setData(name, "looses", 0);	
		setData(name, "elo", 1000);	
		setData(name, "streak", 0);	
		
		updateLadder();
	}
	
	/**
	 * This method reset all the players
	 */
	public static void resetPlayers()
	{
		ArrayList<File> players = getPlayersFiles();
		
		for (int i = 0; i < players.size(); i++)
			resetPlayer(players.get(i).getName());
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
	
	/**
	 * This method reset all the players and delete all the games contained in the games folder
	 */
	public static void reset()
	{
		resetGames();
		resetPlayers();
		resetPlayersFriends();
		updateLadder();
	}
	
}
