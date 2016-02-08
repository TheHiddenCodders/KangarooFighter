package ServerInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Kangaroo.Player;
import Packets.ConnectionPacket;
import Packets.LoginPacket;
import Packets.PlayerPacket;

public class PlayerProcessor 
{
	/** players : all player who have an account on th game*/
	private ArrayList<Player> players;
	/** waitingClients : all clients connected to the server but not logged in */
	private ArrayList<String> waitingClients;
	/** connectedPlayer : all Player logged in */
	private ArrayList<Player> connectedPlayer;
	/** playerFilePath : the path of the directory storing all players infos */
	String playerFilePath;
	
	public PlayerProcessor(String filePath)
	{
		playerFilePath = filePath;
		
		waitingClients = new ArrayList<String>();
		connectedPlayer = new ArrayList<Player>();
		players = new ArrayList<Player>();
		
		loadPlayerFromFile();
	}
	
	/** Load player from the file stored on the server
	 * @return all player found in files
	 */
	private void loadPlayerFromFile() 
	{
		// Get the directory containing player infos
		// TODO : change /KangarooFighters/Players with playerFilePath
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players"));
		
		// Browse all file of the directory
		for (File file : directory.listFiles())
		{
			// Create a new PlayerPacket representing the player
			PlayerPacket playerData = new PlayerPacket();
			
			// Get specific file storing players infos
			File dataFile = new File(file.getPath() + "/data");
			File friendsFile = new File(file.getPath() + "/friends");
			File pwdFile = new File(file.getPath() + "/pwd");
			
			// Get the players data from file
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(dataFile));
				
				playerData.name = reader.readLine().split(":")[1];
				playerData.games = Integer.parseInt(reader.readLine().split(":")[1]);
				playerData.wins = Integer.parseInt(reader.readLine().split(":")[1]);
				playerData.looses = Integer.parseInt(reader.readLine().split(":")[1]);
				playerData.elo = Integer.parseInt(reader.readLine().split(":")[1]);
				playerData.streak = Integer.parseInt(reader.readLine().split(":")[1]);
				
				reader.close();
			} 
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			// TODO : store player friends in the player object using friendsFile
			
			// Create the player with those infos
			players.add(new Player(playerData));
			
			// Store the password of the player
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(pwdFile));
				
				players.get(players.size() - 1).setPassword(reader.readLine());
				
				reader.close();
			} 
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			System.out.println("Player stored : " + playerData.toString());
		}
	}
	
	/** Create a new registered player
	 * @param newPlayer : the player to register
	 */
	public void createPlayer(Player newPlayer)
	{
		
	}
	
	/**	Check if the client pseudo exists and check the password match with the pseudo
	 * 	@param packet the login packet
	 */
	public void attemptToLogin(LoginPacket packet)
	{
		
	}
	
	/**
	 * Check if the player is online
	 * @param name : the name of the player to check
	 * @return true if the player is connected, false otherwise
	 */
	public static boolean isPlayerOnline(String name)
	{
		return false;
	}
	
	/**	Check if the client pseudo exists and check the password match with the pseudo
	 * 	@param packet the login packet
	 */
	public void connexion(ConnectionPacket packet)
	{
		
	}
}
