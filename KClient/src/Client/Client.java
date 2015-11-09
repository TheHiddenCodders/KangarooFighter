package Client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A basic Client object which connect to a server.
 * @author Nerisma
 *
 */
public abstract class Client extends Socket implements Runnable
{
	// Attributes
	private static final String defaultIp = "localhost";
	private static final int defaultPort = 9999;

	private PrintWriter writer;
	private BufferedInputStream reader;
	private String request ="";
	
	/**
	 * Default constructor, create a client that will connect to a default server.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Client() throws UnknownHostException, IOException
	{
		this(defaultIp, defaultPort);
	}
	
	/**
	 * Create a client that will connect to a server with ip and port
	 * @param ip the ip of the server to connect to
	 * @param port the port of the server to connect to
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Client(String ip, int port) throws UnknownHostException, IOException
	{		
		super(ip, port);
	}
	
	/**
	 * On client connexion do..
	 */
	@Override
	public void run()
	{
		try
		{
            writer = new PrintWriter(this.getOutputStream(), true);
            reader = new BufferedInputStream(this.getInputStream());
            
            while (!request.equals("exit"))
            {     
	            // Waiting for answer
	            String answer = readServerRequest();
	            
	            onServerRequest(answer);
            }
            
        } 
		catch (IOException e1)
		{
            e1.printStackTrace();
		}		
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String readServerRequest() throws IOException
	{
		// Needed to read
		String answer = "";
		int stream;
		byte[] b = new byte[4096];
		
		// Read
		stream = reader.read(b);
		answer = new String(b, 0, stream);
		
		return answer;
	}
	
	public void sendRequest(String request)
	{
		System.out.println("Sending to server: " + request);
		this.request = request;
    	writer.write(request);
    	writer.flush();  
	}
	
	public abstract void onServerRequest(String request);
}
