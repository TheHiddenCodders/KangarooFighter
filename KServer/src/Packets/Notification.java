package Packets;


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
	public String style; // "YesNo" or "Ok"
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [message]: " + message + "\n"
				+ "- [date]: " + date + "\n"
				+ "- [style]: " + style + "\n";
	}
	
	public abstract String save();
}
