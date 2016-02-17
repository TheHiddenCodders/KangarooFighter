package Packets;


public class Notification  extends Packets
{
	public Notification(String ip)
	{
		super(ip);
		
		style = "Ok";
	}
	
	public Notification()
	{
		super();
		
		style = "Ok";
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
	
	public  String save()
	{
		String result = new String();
		result = result.concat("Notification-");
		result = result.concat(message + "-");
		result = result.concat(date + "-");
		
		return result;
	}
}
