package Packets;


public class SearchLadderPacket extends Packets
{
	public SearchLadderPacket(String ip) 
	{
		super(ip);
	}

	public SearchLadderPacket() 
	{
		super();
	}

	private static final long serialVersionUID = 8410335275464293843L;

	public String name;
	public int pos;

	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "- [name]:" + name + "\n"
				+ "- [pos]:" + pos + "\n";
	}
}
