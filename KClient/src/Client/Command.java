package Client;

import java.util.ArrayList;

/**
 * The Command class encapsulate a command with a type a arguments. It allows to send it on a network ease communication between clients and server.
 * @author Kurond & Nerisma
 *
 */
public class Command
{
	/*
	 * Attributes
	 */
	private String request;
	private String type;
	private String[] args;
	
	
	/*
	 * Constructors
	 */
	
	/**
	 * The default constructor create a void command (no type and no arguments).
	 */
	public Command()
	{

	}
	
	/**
	 * This constructor create a command with a request received from the network. The type and arguments are automatically set.
	 * 
	 * @param request the request received.
	 */
	public Command(String request)
	{
		setFromRequest(request);
	}
	
	/**
	 * This constructor create a command with the type and all arguments.
	 * 
	 * @param type the type of the command.
	 * @param args all arguments.
	 */
	public Command(String type, String... args)
	{
		setFromArgs(type, args);
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Set the command from a request.
	 * 
	 * @param request the request received.
	 */
	public void setFromRequest(String request)
	{
		//System.out.println(request);
		// Get first part of the command
		type = request.split(":")[0];
		request = request.split(":")[1];
	
		// Get args of the command
		args = request.split(";");
		
		updateRequest();
	}
	
	/**
	 * Set a command with a type and arguments
	 * 
	 * @param type command's type.
	 * @param args all command's arguments
	 */
	public void setFromArgs(String type, String... args)
	{
		this.type = type;
		this.args = args;
		
		updateRequest();
	}
	
	/**
	 * Add arguments to the command
	 * 
	 * @param args arguments to add to the command
	 */
	public void addArgs(String... args)
	{
		if (this.args != null)
		{
			for (int i = this.args.length; i < this.args.length + args.length; i++)
				this.args[i] = args[i - this.args.length];
		}
		else
		{
			this.args = new String[args.length];
			for (int i = 0; i < args.length; i++)
				this.args[i] = args[i];
		}
		
		updateRequest();
	}
	
	/**
	 * Create an ArrayList of command received from a request.
	 * 
	 * @param request the request received.
	 * @return a Array list of Command
	 */
	public static ArrayList<Command> allCommandsFromRequest(String request)
	{
		ArrayList<Command> commands = new ArrayList<Command>();
		
		String[] requests = request.split("|");
		
		for (String req : requests)
			commands.add(new Command(req));
		
		return commands;
	}
	
	private void updateRequest()
	{
		request = type + ":";
		
		for (String arg : args)
			request = request.concat(arg + ";");
	}
	
	/*
	 * Getters and Setters
	 */
	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public String[] getArgs() 
	{
		return args;
	}

	public void setArgs(String[] string) 
	{
		this.args = string;
	}
	
	public String getRequest()
	{
		return request;
	}
}
