package ServerInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Kangaroo.Player;
import Packets.ConnexionPacket;
import Packets.DisconnexionPacket;
import Packets.LoginPacket;
import Packets.PlayerPacket;

public class PlayerProcessor 
{
	/** players : all player who have an account on th game*/
	private ArrayList<Player> players;
	/** waitingClients : all clients connected to the server but not logged in */
	private ArrayList<String> waitingClients;
	/** connectedPlayers : all Player logged in */
	private ArrayList<Player> connectedPlayers;
	/** playerFilePath : the path of the directory storing all players infos */
	String playerFilePath;
	
	public PlayerProcessor(String filePath)
	{
		playerFilePath = filePath;
		
		waitingClients = new ArrayList<String>();
		connectedPlayers = new ArrayList<Player>();
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
	 * 	@return the modified packet
	 */
	public LoginPacket attemptToLogin(LoginPacket packet)
	{
		// If the player is already connected
		if (isPlayerConnected(packet.pseudo) != null)
		{
			// TODO : disconnect the last cp and connect this new client
			packet.accepted = false;
			return packet;
		}
		
		// If this client is not connected, get the Player associate to this pseudo
		Player player = isPlayerExist(packet.pseudo);
		
		// If the pseudo exist
		if (player != null)
		{
			packet.pseudoExists = true;
			
			if (!player.getPassword().equals(packet.pwd))
			{
				// If the password doesn't match
				packet.pwdMatch = false;
				return packet;
			}
		}
		else
		{
			// If the pseudo doesn't exist
			packet.pseudoExists = false;
			return packet;
		}	
		
		// If the login succeed
		packet.pwdMatch = true;
		packet.accepted = true;	
		player.setIp(packet.getIp());
		
		// Remove this client from the waiting list
		waitingClients.remove(packet.getIp());
		
		// Add him to the connected players list
		connectedPlayers.add(player);
		
		return packet;
	}
	
	/** return a connected Player object matching with the pseudo in parameter
	 * @param pseudo : the player pseudo
	 * @return the player if it is connected, null otherwise
	 */
	public Player isPlayerConnected(String pseudo)
	{
		// Browse the list of connected players
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			// If the player is found
			if (connectedPlayers.get(i).getName().equals(pseudo))
				return connectedPlayers.get(i);
		}
		
		// If no player matching with the pseudo
		return null;
	}
	
	/** Check if a player name exist on this server
	 * @param name : the player's name to check
	 * @return return the player itself if exist, null otherwise.
	 */
	private Player isPlayerExist(String name)
	{
		// Browse the list of connected players
		for (int i = 0; i < players.size(); i++)
		{
			// If the player is found
			if (players.get(i).getName().equals(name))
				return players.get(i);
		}
		
		// If no player matching with the pseudo
		return null;
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
	
	/**	Add the ip of the client in the waitingClient list
	 * 	@param packet the ConnexionPacket received from the server class
	 */
	public void connexion(ConnexionPacket packet)
	{
		// Add the ip of the client in the waiting list
		waitingClients.add(new String(packet.getIp()));
	}
	
	/**	Check if this client is connect or/and login and remove the client from the appropriate list
	 * 	@param packet the DeconnexionPacket received from the server class
	 */
	public void deconnexion(DisconnexionPacket packet)
	{
		// TODO : remove player 
	}

	/**
	 * Get a player from the ip of a client
	 * @param ip : the ip associate to the player
	 * @return the associated player if connected, null otherwise
	 */
	public Player getPlayerFromIp(String ip)
	{
		// Browse the list of connected players
		for (Player player : connectedPlayers)
		{
			// If the player is found
			if (player.getIp().equals(ip))
				return player;
		}
		
		// If no player matching with the ip
		return null;
	}
}
