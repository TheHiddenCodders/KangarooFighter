package ServerInfo;

import java.util.Comparator;

import Kangaroo.Player;

public class EloComparator implements Comparator<Player> 
{

	@Override
	public int compare(Player player1, Player player2) 
	{
		return player2.getElo() - player1.getElo();
	}

}
