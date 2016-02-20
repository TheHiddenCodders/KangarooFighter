package Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import Packets.DisconnexionPacket;
import Packets.Packets;

/** 
 * Manage one client communication in its personal thread.
 * @author Nerisma & Kurond
 *
 */
public class ClientProcessor implements Runnable 
{
	/** client : the socket allowing communication with the client */
	private Socket client;
	/** input : a buffer to store receiving packets */
	protected ObjectInputStream input;
	/** output : a buffer to store sending packets */
	protected ObjectOutputStream output;
	//protected InetSocketAddress remote;
	//private float lastHeartBeatAnswerTime;
	
	/** server : a reference to the server object */
	protected Server server;
	
	/** 
	 * Constructor : Build a ClientProcessor with a client socket.
	 * @param client : the client to manage
	 */
	public ClientProcessor(Socket client, Server server)
	{		
		// Make references to the client and the server
		this.client = client;
		this.server = server;
		
		// Try to build buffers
		try 
		{
            output = new ObjectOutputStream(client.getOutputStream());	
            output.flush(); // Remove this line and the program freeze
			input = new ObjectInputStream(client.getInputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}

	/** Wait for Reading data from the client and send it to the server program.
	 */
	public void run()
	{		
		// While connection is active
		while (!client.isClosed())
		{				
			Object receivedObject = null;
			
			// Waiting for object from the client
			while (receivedObject == null)
				receivedObject = receiveFromClient();	
			
			// Cast the result into packet
			Packets receivedPacket = null;
			if (receivedObject instanceof Packets)
			{
				receivedPacket = (Packets) receivedObject;
				receivedPacket.setIp( getIp() );
			}
			else
			{
				System.out.println("Receiver Thread : Receive something not a packet");
			}
			
			// If the client has been disconnected
			if (receivedObject.getClass().isAssignableFrom(DisconnexionPacket.class))
			{
				break;
			}
			
			// Send the received packet to the server program
			server.readBuffer.sendPacket(receivedPacket);
		}
		
		// When the client is disconnected
		
		try 
		{
			// Try to close the socket
			client.close();
			
			// Send disconnection packet to the server program
			server.readBuffer.sendPacket(new DisconnexionPacket(this.getIp()));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	/** Send an object to the client
	 * @param o the object to send
	 */
	public void send(Object o, String threadName)
	{
		try 
		{
			System.err.println(o.getClass());
			
			// Write the object into the buffer
			output.writeObject(o);
			
			// Send the buffer content to the client
			output.flush();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/** Get an object from client 
	 * @return the object received from the client
	 */
	public Object receiveFromClient()
	{
		// Try to read an object
		try
		{	
			// And return it
			return input.readObject();
		} 
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		
		// Otherwise return null
		return null;
	}
	
	/** Get the client socket
	 * @return socket
	 */
	public Socket getClient()
	{
		return client;
	}
	
	/** Get the ip of the managed client
	 */
	public String getIp()
	{
		return ((InetSocketAddress) client.getRemoteSocketAddress()).getHostString() + ":" + client.getPort();
	}
}
