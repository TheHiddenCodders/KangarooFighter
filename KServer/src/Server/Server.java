package Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/** Encapsulate the server socket and can handle multiple clients.
 * 
 */
public abstract class Server 
{
	// Attributes
	public final static int defaultPort = 9999;
	
	private int port;
	private String ip;
	private ServerSocket server = null;
	protected boolean running;
	protected ArrayList<ClientProcessor> clients;
	
	/** Default constructor, create a serverSocket with a default port.
	 * 
	 */
	Server()
	{
		this(defaultPort);
	}
	
	/** This constructor create a serverSocket with the port give in parameter.
	 * 
	 * @param port The port where the serverSocker listening to.
	 * 
	 */
	Server(int port)
	{
		try
		{
			// Initialize attributes
			this.port = port;
			this.ip = InetAddress.getLocalHost().getHostAddress();
			this.running = true;
			
			clients = new ArrayList<ClientProcessor>();
			
			// Create the serverSocket with the port 
			server = new ServerSocket(port);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/** Launch the server and wait, in its own thread, a client connection
	 * 
	 */
	public void open()
	{
		// Create the thread
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(running)
				{ 
					try 
					{
						System.out.println("Server ready, wait for connections"); 
						
						// Waiting for a client connection
						Socket client = server.accept();

						// Add the client to the list
						clients.add( new ClientProcessor(client, Server.this) );

						// Send a message to the connected client
						onConnection( clients.get(clients.size() - 1) );
						
						// Create a new thread to manage this client               
						Thread t = new Thread( clients.get(clients.size() - 1) );
						t.start();
	               }
	               catch (IOException e) 
	               {
	            	   e.printStackTrace();
	               }
				}
	            
	            try 
	            {
	               server.close();
	            } 
	            catch (IOException e) 
	            {
	               e.printStackTrace();
	               server = null;
	            }
	         }
			
	      });
	      t.start();
	}
	
	public abstract void onConnection(ClientProcessor cp);
	public abstract void onReceive(Object o, ClientProcessor cp);
	public abstract void onDisconnection(ClientProcessor cp);
	
	/** Send an object to the client #clientIndex
	 * 
	 * @param clientIndex the index of the client, -1 all clients
	 * @param o the object to send
	 * 
	 */
	public void send(int clientIndex, Object o)
	{
		if ( clientIndex == -1)
		{
			for (int i = 0; i < clients.size(); i++)
				clients.get(i).send(o);
			
			System.out.println("Sent : " + o.toString() + "to all \n");
		}
		else if ( clientIndex >= 0 && clientIndex < clients.size() )
		{
			clients.get(clientIndex).send(o);
			System.out.println("Sent : " + o.toString() + "to client n°" + clientIndex + "\n");
		}
	}
	
	/** Send an object to a specific client
	 * 
	 * @param cp a reference to the client
	 * @param o the object to send
	 * 
	 */
	public void send(ClientProcessor cp, Object o)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			if (clients.get(i) == cp)
			{
				send(i, o);
				break;
			}
		}
	}

	
	/**
	 * 
	 * @return The port listening by the server
	 * 
	 */
	public int getPort() 
	{
		return port;
	}

	/**
	 * @return the ip
	 * 
	 */
	public String getIp() 
	{
		return ip;
	}

	/**
	 * @return the socket
	 * 
	 */
	public ServerSocket getServer() 
	{
		return server;
	}

	/**
	 * @return the running
	 * 
	 */
	public boolean isRunning() 
	{
		return running;
	}

	/** Close the server
	 * 
	 */
	public void close()
	{
		this.running = false;
		System.out.println("A new client is connected");  
	}
}
