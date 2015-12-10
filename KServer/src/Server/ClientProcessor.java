package Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/** 
 * Manage one client communication in its personal thread.
 * @author Nerisma
 *
 */
public class ClientProcessor implements Runnable 
{
	
	/*
	 * ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  Object o = in.readObject(); 
		  ...
		}
	 * 
	 */
	// Attributes
	private Socket client;
	protected ObjectInputStream input;
	protected ObjectOutputStream output;
	protected InetSocketAddress remote;
	private float lastHeartBeatAnswerTime;
	
	protected Server server;
	
	/** 
	 * Build a ClientProcessor with the Socket (which mean the client) associated.
	 * @param client the client to manage
	 */
	public ClientProcessor(Socket client, Server server)
	{		
		this.client = client;
		this.server = server;
		
		try 
		{
            output = new ObjectOutputStream(client.getOutputStream());	
            output.flush(); // Remove this line and the program freeze
			input = new ObjectInputStream(client.getInputStream());
			remote = (InetSocketAddress) client.getRemoteSocketAddress();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}

	/** 
	 * Launch client connection treatment.
	 */
	public void run()
	{		
		// While connection is active, treat asks
		while (!client.isClosed())
		{						
			// Waiting for client reception
			Object receivedObject = receiveFromClient();			
			
			// If received object equals -1, that's mean client has been quit
			if (receivedObject.equals(-1))
			{
				server.onDisconnection(this);
				break;
			}
			
			// Do things on receive
			server.onReceive(receivedObject, this);
		}
		
		try 
		{
			client.close();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	/** Send an object to the client
	 * 
	 * @param o the object to send
	 */
	public void send(Object o)
	{
		try 
		{
			output.writeObject(o);
			output.flush();
			
			System.out.println("Sent to " + this.getIp() + ":" + o.toString() + "\n");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Get an object from client 
	 * @return the object sent by the client
	 */
	public Object receiveFromClient()
	{
		try
		{	
			return input.readObject();
		} catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get the socket
	 * @return socket
	 */
	public Socket getClient()
	{
		return client;
	}
	
	/** 
	 * Set the last heartbeat answer time
	 */
	public void setLastHeartBeatAnswer(float t)
	{
		lastHeartBeatAnswerTime = t;
	}
	
	/** 
	 * Get the last heartbeat answer time
	 */
	public float getLastHeartBeatAnswer()
	{
		return lastHeartBeatAnswerTime;
	}
	
	public String getIp()
	{
		return remote.getHostString() + ":" + client.getPort();
	}
}
