package Packets;

public class Notification  extends Packets
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
	// public Style style; style = yesno, yesOnly, ...
}
