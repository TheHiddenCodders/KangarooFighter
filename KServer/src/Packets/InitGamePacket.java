package Packets;

import enums.GameTypes;


public class InitGamePacket extends Packets
{
	public InitGamePacket(String ip) 
	{
		super(ip);
	}

	public InitGamePacket() 
	{
		super();
	}

	private static final long serialVersionUID = -2707492392522937092L;
	
	public GameTypes gameType;
	public KangarooPacket player, opponent;
	public PlayerPacket opponentData;
	public String mapPath;
	public int playerWins;
	public int opponentWins;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "[player]: " + /*player.toString() + */ "\n"
				+ "[opponent]: " + /*opponent.toString() + */ "\n"
				+ "[opponentData]: " + opponentData.toString() + "\n"
				+ "[mapPath]: " + mapPath + "\n"
				+ "[playerWins]: " + playerWins + "\n"
				+ "[opponentWins]: " + opponentWins + "\n";
	}
}
