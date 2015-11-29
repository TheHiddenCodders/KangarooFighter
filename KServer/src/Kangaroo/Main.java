package Kangaroo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
		Timer t1 = new Timer();
		t1.schedule(new TimerTask(){

			@Override
			public void run() 
			{
				// Update running games
				for(Game game : server.getAllGames())
				{
					if ( game.isRunning() )
					{
						game.stateMachine();
					}
				}
			}
			
		}, 0, 30);
		
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
			if (msg.contains("display"))
			{
				if (msg.split("-")[1].equals("all"))
					server.displayAllKangaroos();
			}
		}
	}

}
