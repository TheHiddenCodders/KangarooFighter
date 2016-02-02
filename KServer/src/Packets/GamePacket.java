package Packets;


public class GamePacket extends Packets
{
	public GamePacket(String ip) 
	{
		super(ip);
	}

	public GamePacket() 
	{
		super();
	}

	private static final long serialVersionUID = -2707492392522937092L;
	
	public KangarooServerPacket player, opponent;
	public PlayerPacket playerData, opponentData;
	public String mapPath;
	public int playerWins;
	public int opponentWins;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "[player]: " + player.toString() + "\n"
				+ "[opponent]: " + opponent.toString() + "\n"
				+ "[playerData]: " + playerData.toString() + "\n"
				+ "[opponentData]: " + opponentData.toString() + "\n"
				+ "[mapPath]: " + mapPath + "\n"
				+ "[playerWins]: " + playerWins + "\n"
				+ "[opponentWins]: " + opponentWins + "\n";
	}
}
