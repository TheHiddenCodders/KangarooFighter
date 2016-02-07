package Kangaroo;

import java.io.File;
import java.util.ArrayList;

import Packets.NewsPacket;
import Server.BufferPacket;

public class News 
{
	/** ladder : A list containing players order by elo*/
	private ArrayList<NewsPacket> news;
	
	/** newsFile : all files containing a news*/
	ArrayList<File> newsFiles;
	
	/** saveBuffer : A buffer allowing communication with save thread */
	private BufferPacket saveBuffer;
	
	/** Constructor : load all news with the path to the save file.
	 * @param filePath : the path to the ladder file
	 */
	public News(String filePath)
	{
		// Create objects
		saveBuffer = new BufferPacket();
		news = new ArrayList<NewsPacket>();
		ArrayList<File> newsFiles = getNewsFiles();
		
		/*try
		{
			BufferedReader reader = new BufferedReader(new FileReader(ladderFile));
			
			// Forget the first line
			String line = reader.readLine();
			line = reader.readLine();
			
			
			// Read each line
			while (line != null)
			{
				// TODO : Add all PlayerPaket attribute in ladder save file
				
				// Create a player packet for each line
				ladder.add(createPacketFromLine(line));
				line = reader.readLine();
			}
			
			// Close the file
			reader.close();
			
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
	}
	
	/** Search files containing news 
	 * @return all the news in files
	 */
	private ArrayList<File> getNewsFiles()
	{
		newsFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/News"));

		for (File file : directory.listFiles())
			newsFiles.add(file);
		
		return newsFiles;
	}
}
