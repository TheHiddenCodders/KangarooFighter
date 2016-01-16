package Packets;

import java.io.Serializable;

public class GamePacket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2707492392522937092L;
	
	public KangarooServerPacket player, opponent;
	public String mapPath;
	public int round;
	public int playerWins;
	public int opponentWins;
	
	@Override
	public String toString()
	{
		return super.toString()
				+ "\n"
				+ "[player]: " + player.toString() + "\n"
				+ "[opponent]: " + opponent.toString() +"\n"
				+ "[mapPath]: " + mapPath + "\n"
				+ "[round]: " + round + "\n"
				+ "[playerWins]: " + playerWins + "\n"
				+ "[opponentWins]: " + opponentWins + "\n";
	}
	
	/**
	 * @return true if all field are filled, false if not
	 */
	public boolean isFilled()
	{
		return (player != null && opponent != null && mapPath != null);
	}
}
