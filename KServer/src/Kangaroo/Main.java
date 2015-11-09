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
		
		inputReader = new BufferedInputStream(System.in);
		
		while (!msg.equals("exit"))
		{
			int stream;
			byte[] b = new byte[4096];
				
			// Read
			stream = inputReader.read(b);
			msg = new String(b, 0, stream);
			msg = msg.replace(System.lineSeparator(), "");
		}
	}

}
