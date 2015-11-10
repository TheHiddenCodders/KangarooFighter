package com.genesys.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Object o = 0;
	
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
	 * On client connection, prepare buffers
	 */
	@Override
	public void run()
	{
		try
		{
            input = new ObjectInputStream(this.getInputStream());
            output = new ObjectOutputStream(this.getOutputStream());
            
            // While -1 object havn't been passed
            while (!o.equals(-1))
            {     
	            // Receive from server
	            Object receivedObject = receiveFromServer();
	    		
	    		onReceived(receivedObject);
            }
            
        } 
		catch (IOException e1)
		{
            e1.printStackTrace();
		}		
	}

	/**
	 * Receive object from server
	 * @return object sent by the server
	 */
	public Object receiveFromServer()
	{		
		try
		{
			return input.readObject();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Send object to the server
	 * @param o the object to send
	 */
	public void send(Object o)
	{
		try
		{
			System.out.println("Sent : " + o.toString());
			output.writeObject(o);
			output.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	public abstract void onReceived(Object o);
}
