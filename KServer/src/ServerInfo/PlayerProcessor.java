package ServerInfo;

import java.io.File;
import java.util.ArrayList;

import Kangaroo.Player;
import Packets.PlayerPacket;

public class PlayerProcessor 
{
	/** players : all player who have an account on th game*/
	private ArrayList<Player> players;
	/** waitingClients : all clients connected to the server but not logged in */
	private ArrayList<String> waitingClients;
	/** connectedPlayer : all Player logged in */
	private ArrayList<Player> connectedPlayer;
	
	public PlayerProcessor(String filePath)
	{
		loadPlayerFromFile(filePath);
		waitingClients = new ArrayList<String>();
		connectedPlayer = new ArrayList<Player>();
	}
	
	/** Load player from the file stored on the server
	 * @return all player found in files
	 */
	private ArrayList<Player> loadPlayerFromFile(String filePath) 
	{
		
		ArrayList<File> playersFiles = new ArrayList<File>();
		// Get the directory containing player infos
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Players"));
		
		// Browse all file of the directory
		for (File file : directory.listFiles())
		{
			PlayerPacket playerData = new PlayerPacket();
			// TODO : Fill the PlayerPacket with data file
			// TODO : store player friends in the player object
			
			players.add(new Player(playerData));
			
			playersFiles.add(file);
		}
		return null;
	}
}
