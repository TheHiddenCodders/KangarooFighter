package Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Packets.ConnexionPacket;
import Packets.Packets;

/** Encapsulate the server socket and can handle multiple clients.
 * 
 */
public class Server 
{
	// Attributes
	public final static int defaultPort = 9999;
	
	/** port : the listener port */
	private int port;
	/** ip : the current server ip */
	private String ip;
	/** server : the socket allowing communication with this server */
	private ServerSocket server = null;
	/** running : the current state of the server */
	protected boolean running;
	/** clients : all clients connected to this server */
	protected ArrayList<ClientProcessor> clients;
	/** Buffers allowing this server to communicate with other thread of the program */
	public volatile BufferPacket readBuffer;
	public volatile BufferPacket sendBuffer;
	
	/** Default constructor : Create a serverSocket listening the default port.
	 * 
	 */
	public Server()
	{
		this(defaultPort);
	}
	
	/** Constructor : Create a serverSocket with the port give in parameter.
	 * @param port : The port to listen. 
	 */
	public Server(int port)
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
			
			readBuffer = new BufferPacket();
			sendBuffer = new BufferPacket();
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
		// Create the ServerClient/Communication thread
		Thread t1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(running)
				{ 
					try 
					{
						System.out.println("Acceptation thread : Server ready, wait for connections"); 
						
						// Waiting for a client connection
						Socket client = server.accept();

						// Add the client to the list
						clients.add( new ClientProcessor(client, Server.this) );

						// Broadcast to other thread the connection of a new client 
						readBuffer.sendPacket(new ConnexionPacket(clients.get(clients.size() - 1).getIp()));
						
						// Create a new thread to listen this client               
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
	      t1.start();
	      
	      // Create a thread waiting for packet to send to clients
	      Thread t2 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					String name = "Sender Thread";
					ArrayList<Packets> packets;
					ClientProcessor cp;
					
					while(running)
					{
						// Get packets from buffer
						packets = sendBuffer.readPackets();
						
						// Browse the packets received from the main thread
						for (Packets packet : packets)
						{
							// Get the cp associate to this ip
							cp = getCpFromIp(packet.getIp());
							
							// Send the packet to the associate client
							cp.send(packet, name);
						}
					}
		         }
				
		      });
		      t2.start();
	}
	
	/** Browse the client list to find the client with this ip
	 * @param ip : the ip of the client to find
	 * @return the client if found, null otherwise
	 */
	private ClientProcessor getCpFromIp(String ip)
	{
		// Browse the list of client
		for (ClientProcessor cp : clients)
		{
			// If the client with the ip specify in param is found, return it...
			if (cp.getIp().equals(ip))
				return cp;
		}
		
		// ... return null otherwise
		return null;
	}
	
	/**
	 * @return The port listening by the server
	 */
	public int getPort() 
	{
		return port;
	}

	/**
	 * @return the ip
	 */
	public String getIp() 
	{
		return ip;
	}

	/**
	 * @return the socket
	 */
	public ServerSocket getServer() 
	{
		return server;
	}

	/**
	 * @return the running
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
