package Packets;


public class LaunchGamePacket extends Packets
{
	private static final long serialVersionUID = 2290013288161525254L;
	
	public String p1Ip;
	public String p2Ip;
	
	public LaunchGamePacket()
	{
		p1Ip = "";
		p2Ip = "";
	}
	
	public LaunchGamePacket(String p1Ip, String p2Ip)
	{
		this.p1Ip = p1Ip;
		this.p2Ip = p2Ip;
	}
}
