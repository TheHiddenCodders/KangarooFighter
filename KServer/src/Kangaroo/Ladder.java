package Kangaroo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Packets.Packets;
import Packets.PlayerPacket;
import Server.BufferPacket;
import Utils.Timer;

public class Ladder 
{
	
	private ArrayList<PlayerPacket> ladder;
	private BufferPacket saveBuffer;
	
	public Ladder(String filePath)
	{
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
			
			
			// Read the file
			while (line != null)
			{
				// TODO : Add all PlayerPaket attribute in ladder save file
				// Create a player packet for each line
				ladder.add(createPacketFromLine(line));
				line = reader.readLine();
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
		
		// When ladder is initiated, lauch a thread to save file each minute
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
							savingPacket = (PlayerPacket) packetToSave.get(i);
							
							// TODO : save data in file here
						}
					}
				}
			}
		});
		t.start();
	}
	
	public PlayerPacket[] getLadderFromPosition(int position)
	{
		int begin, end;
		PlayerPacket[] result;
		
		// Find where the ladder start
		if (position - 5 < 0)
			begin = 0;
		else
			begin = position - 5;
		
		// Find where the ladder end
		if (position + 5 > ladder.size())
			end = ladder.size();
		else
			end = position + 5;
			
		// Create the object
		result = new PlayerPacket[end - begin];
		
		// Fill the result in
		for (int i = 0; i < end-begin; i++)
		{
			result[i] = ladder.get(i + begin);
		}
		
		return result;
	}
	
	private PlayerPacket createPacketFromLine(String line)
	{
		PlayerPacket packet = new PlayerPacket();
		
		packet.pos = Integer.parseInt(line.split(" | ")[0]);
		packet.name = line.split(" | ")[2];
		packet.elo = Integer.parseInt(line.split(" | ")[4]);
		
		return packet;
	}
	
}
