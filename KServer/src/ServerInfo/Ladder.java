package ServerInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Packets.LadderPacket;
import Packets.Packets;
import Packets.PlayerPacket;
import Server.BufferPacket;
import Utils.Timer;

public class Ladder 
{
	/** ladder : A list containing players order by elo*/
	private ArrayList<PlayerPacket> ladder;
	
	/** saveBuffer : A buffer allowing communication with save thread */
	private BufferPacket saveBuffer;
	
	/** Constructor : create the ladder with the path to the save file.
	 * @param filePath : the path to the ladder file
	 */
	public Ladder(String filePath)
	{
		// Create objects
		saveBuffer = new BufferPacket();
		ladder = new ArrayList<PlayerPacket>();
		
		// Open the ladder file
		File ladderFile = new File(new File("").getAbsolutePath().concat("/KangarooFighters/Ladder/elo"));
		
		try
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
		}
		
		// When ladder is initiated, launch a thread to save the ladder file each minute
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				Timer timer = new Timer();
				ArrayList<Packets> packetToSave;
				PlayerPacket savingPacket;
				
				timer.restart();
				
				
				while (true)
				{
					// Wait for a minute
					while (timer.getElapsedTime() <= 60.f);
					
					// Wait for new packet to save
					packetToSave = saveBuffer.readPackets();
					
					// Browse all packet to save
					for (int i = 0; i < packetToSave.size(); i++)
					{
						if (packetToSave.get(i).getClass().isAssignableFrom(PlayerPacket.class))
						{
							// Save the packets
							savingPacket = (PlayerPacket) packetToSave.get(i);
							
							// TODO : save data in file here
						}
					}
				}
			}
		});
		t.start();
	}
	
	public LadderPacket getLadderFromPosition(int position)
	{
		LadderPacket result = new LadderPacket();
		int begin, end;
		
		// - Find where the ladder start -
		begin = position - 3;

		// If the first block is before the list
		if (begin < 0) 
		{
			// Start from the first bloc
			begin = 0;
		}
		
		// Find where the ladder end
		end = begin + 9;
		
		// If the last block is after the list
		if (end > ladder.size())
		{
			// End the ladder to the last
			end = ladder.size();
		}
		
		// Fill the result in
		for (int i = 0; i < end-begin; i++)
			result.addPlayer(ladder.get(i + begin));
		
		return result;
	}
	
	/** Create a packet with a line of the ladder file
	 * @param line : a line from ladder file
	 * @return A new PlayerPacket containing the packet store in the ladder file
	 */
	private PlayerPacket createPacketFromLine(String line)
	{
		// Create the packet
		PlayerPacket packet = new PlayerPacket();
		
		// Fill it in
		packet.pos = Integer.parseInt(line.split(" | ")[0]);
		packet.name = line.split(" | ")[2];
		packet.elo = Integer.parseInt(line.split(" | ")[4]);
		
		// Return it
		return packet;
	}
	
}
