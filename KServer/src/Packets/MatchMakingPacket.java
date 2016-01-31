package Packets;


public class MatchMakingPacket extends Packets
{
	private static final long serialVersionUID = 4023881332979491078L;
	
	public boolean search; // This packet can also get off client from matchmaking
	public float timeInQueue;
	public float eloTolerence; // A percentage. e.g. 1% and elo = 1000 can fight player elo [900 - 1100] 
	
	@Override
	public String toString()
	{
		return super.toString() 
				+ "\n"
				+ "- [search]: " + search + "\n";
				
	}
}
