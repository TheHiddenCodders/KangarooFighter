package ServerInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import Packets.NewsPacket;

public class News 
{
	/** ladder : A list containing players order by elo*/
	private ArrayList<NewsPacket> news;
	
	/** Constructor : load all news with the path to the save file.
	 * @param filePath : the path to the ladder file
	 */
	public News(String filePath)
	{
		// Load news from files
		loadNewsFromFiles(filePath);
	}
	
	/** Search files containing news 
	 * @return all the news in the news directory
	 */
	private void loadNewsFromFiles(String filePath)
	{
		ArrayList<File> newsContent;
		ArrayList<File> newsFiles = new ArrayList<File>();
		
		news = new ArrayList<NewsPacket>();
		
		// Get the directory containing news
		File directory = new File(new File("").getAbsolutePath().concat(filePath));
		
		// Browse all news in the directory
		for (File file : directory.listFiles())
			newsFiles.add(file);
		
		// Order news by last modification
		Collections.sort(newsFiles, new FileDateComparator());
		
		// Create packet with those files
		for (int i = 0; i < newsFiles.size(); i++)
		{
			// Create a news for each directory in "News" directory
			news.add(new NewsPacket());
			
			// The news is named the same as the directory
			news.get(i).name = newsFiles.get(i).getName();
			
			// Get the banner and the content of the news
			newsContent = getNewsFiles(news.get(i).name);
			
			// Try to fill the packet in
			try
			{
				news.get(i).banner = Files.readAllBytes(newsContent.get(0).toPath());
				news.get(i).news = Files.readAllBytes(newsContent.get(1).toPath());
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/** Return the two files in a specific news directory
	 * @param name : the name of the directory
	 * @return the news files of the single news named name
	 */
	private static ArrayList<File> getNewsFiles(String directoryName)
	{
		ArrayList<File> newsFiles = new ArrayList<File>();
		File directory = new File(new File("").getAbsolutePath().concat("/KangarooFighters/News/" + directoryName));

		for (File file : directory.listFiles())
			newsFiles.add(file);
		
		return newsFiles;
	}
	
	public NewsPacket[] getLastNews(int howManyNews, String ip)
	{
		NewsPacket[] result = new NewsPacket[howManyNews];
		for (int i = 0; i < howManyNews - 1; i++)
		{
			result[i] = new NewsPacket();
			result[i].setIp(ip);
			result[i].name = news.get(news.size() - 1 - i).name;
			result[i].banner = news.get(news.size() - 1 - i).banner;
			result[i].news = news.get(news.size() - 1 - i).news;
		}
		return result;
	}
}
