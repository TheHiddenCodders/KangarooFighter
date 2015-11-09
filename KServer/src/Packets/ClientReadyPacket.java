package Packets;

public class ClientReadyPacket
{
	public String ip;
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [IP]: " + ip + "\n";
				
	}	
}
