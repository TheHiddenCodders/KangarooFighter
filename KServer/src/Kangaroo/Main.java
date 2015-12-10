package Kangaroo;

import java.io.BufferedInputStream;
import java.io.IOException;

import Server.KServer;

public class Main 
{
	public static BufferedInputStream inputReader;
	public static String msg = "";
	
	public static void main(String[] args) throws IOException
	{
		KServer server = new KServer();
		server.open();
		
		// Create a timer that update all games
		Thread gameThread = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				while(true)
				{
					// Update running games
					//for (Game game : server.getAllGames())
					System.err.println("");
					for (int i = 0; i < server.getAllGames().size(); i++)
					{
						System.err.println("NOP");
						if (server.getAllGames().get(i).isEnded())
						{
							server.getAllGames().remove(server.getAllGames().get(i));
						}
						
						if ( server.getAllGames().get(i).isRunning() )
						{
							server.getAllGames().get(i).stateMachine();
						}
					}
				}
			}
		});
		gameThread.start();
		
		inputReader = new BufferedInputStream(System.in);
		
		while (!msg.equals("exit"))
		{		
			int stream;
			byte[] b = new byte[4096];
				
			// Read
			stream = inputReader.read(b);
			msg = new String(b, 0, stream);
			msg = msg.replace(System.lineSeparator(), "");
			
			// Commands
			if (msg.contains("display player"))
			{
				if (msg.split("-")[1].equals("all"))
					server.displayAllKangaroos();
			}
			
			if (msg.contains("display game"))
			{
				if (msg.split("-")[1].equals("all"))
					server.displayAllGames();
			}
		}
	}

}
