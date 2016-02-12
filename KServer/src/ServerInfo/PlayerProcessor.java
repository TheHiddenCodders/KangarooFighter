package ServerInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import Kangaroo.Player;
import Packets.ConnexionPacket;
import Packets.DisconnexionPacket;
import Packets.FriendsPacket;
import Packets.LadderPacket;
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
		
		// Load players from their files
		loadPlayerFromFile(filePath);
		
		// Create a thread that save data
		TimerTask saveFile = new TimerTask(){

			@Override
			public void run() 
			{
				// TODO : Save file here
			}
		};
		
		Timer timer = new Timer();
		
		// Save files each 30 minutes
		timer.schedule(saveFile, 0, 1800000);
	}
	
	/** Load player from the file stored on the server
	 * @return all player found in files
	 */
	private void loadPlayerFromFile(String filePath) 
	{
		// Get the directory containing player infos
		File directory = new File(new File("").getAbsolutePath().concat(filePath));
		
		// Browse all file of the directory
		for (File file : directory.listFiles())
		{
			// Create a new PlayerPacket representing the player
			PlayerPacket playerData = new PlayerPacket();
			
			// Get specific file storing players infos
			File dataFile = new File(file.getPath() + "/data");
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
		
		// Re browse player to init friends
		for (File file : directory.listFiles())
		{
			File friendsFile = new File(file.getPath() + "/friends");
			
			// Get player's friends
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(friendsFile));
				String readingLine;
				
				// Brows the friend file
				while ( (readingLine = reader.readLine()) != null)
				{
					// Add the name in the file into the player's friends
					isPlayerExist(file.getName()).getPacket().addFriend(new FriendsPacket(isPlayerExist(readingLine).getPacket()));
				}
				
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
		
		// Order the players by their elo
		Collections.sort(players, new EloComparator());
		
		// Affect position to each player
		for (int i = 0; i < players.size(); i++)
		{
			players.get(i).getPacket().pos = i + 1;
		}
	}
	
	/** Create a new registered player
	 * @param newPlayer : the player to register
	 */
	public void createPlayer(Player newPlayer)
	{
		// TODO : send a ServerInfoPacket
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
		
		// Browse all player friends
		for (int i = 0; i < player.getPacket().friends.size(); i++)
		{
			// Try to find player in his friend's, friend list
			Player friendPlayer = isPlayerExist(player.getPacket().friends.get(i).name);
			
			// Put me online in my friends friends packets
			for (int j = 0; j < friendPlayer.getPacket().friends.size(); j++)
			{
				if (friendPlayer.getPacket().friends.get(j).name.equals(player.getName()))
				{
					friendPlayer.getPacket().friends.get(j).online = true;
					break;
				}
			}
		}
		
		// Put me online in my own packet
		player.getPacket().online = true;
		
		// Remove this client from the waiting list
		waitingClients.remove(packet.getIp());
		
		// Add him to the connected players list
		connectedPlayers.add(player);
		
		// TODO : send a ServerInfoPacket
		
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
	public Player isPlayerExist(String name)
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
		Player player = isPlayerConnected(getPlayerFromIp(packet.getIp()).getName());
		
		// If the player is login
		if (player != null)
			connectedPlayers.remove(player);
		
		// If the player is not login, then he is in waiting list
		waitingClients.remove(packet.getIp());
		
		// TODO : notified friend this client is deco
		// TODO : send a ServerInfoPacket
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
	
	public ArrayList<Player> getConnectedPlayers()
	{
		return connectedPlayers;
	}
	
	public LadderPacket getLadderFromPlayer(Player player)
	{
		int position = 0;
		
		// Get player position
		for (int i = 0; i < players.size(); i++)
		{
			if (players.get(i).getName().equals(player.getName()))
			{
				position = i + 1;
				break;
			}
		}
		
		return getLadderFromPosition(position);
	}
	
	public LadderPacket getLadderFromPosition(int position)
	{
		LadderPacket result = new LadderPacket();
		int begin, end;
		
		// The 1st player is accessible with player.get(0)
		position--;
		
		// Try to get the 4 players above
		begin = position - 4;

		// If there is less than 4 players above
		if (begin < 0) 
		{
			// Then start from the first player of the ladder
			begin = 0;
		}
		
		// Try to get 9 players in total
		end = begin + 9;
		
		// If there is less than 9 players below the beginning
		if (end > players.size())
		{
			// Then end to the last player
			end = players.size();
			
			// NERISMA ADD
			// Kurond.getFriends(Nerisma).giveCookie
			begin = end - 9;
		}
		
		// Fill the result in
		for (int i = 0; i < end-begin; i++)
		{
			result.addPlayer(players.get(i + begin).getPacket());
		}
		
		return result;
	}
	
	// TODO : Create a method modifying players and reorder them using EloComparator, make for to affect position
}
