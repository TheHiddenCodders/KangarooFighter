package Packets;

import java.util.Date;

public abstract class Notification  extends Packets
{
	public Notification(String ip)
	{
		super(ip);
	}
	
	public Notification()
	{
		super();
	}
	
	private static final long serialVersionUID = 7294896430046500065L;
	public String message;
	public String date;
	// public Style style; style = yesno, yesOnly, ...
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [message]: " + message + "\n"
				+ "- [date]: " + date + "\n";
	}
	
	public abstract String save();
}
