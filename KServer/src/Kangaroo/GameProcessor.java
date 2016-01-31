package Kangaroo;

import java.util.ArrayList;

import Packets.MatchMakingPacket;

public class GameProcessor 
{
	/** games : a list containing all games */
	private ArrayList<Game> games;
	/** waitingPlayers : a list of reference on player waiting for play */
	private ArrayList<Player> waitingPlayers;
	/** connectedPlayer : a reference to all connected player */
	private ArrayList<Player> connectedPlayers;
	
	public GameProcessor(ArrayList<Player> connectedPlayers)
	{
		// Create attributes
		this.games = new ArrayList<Game>();
		this.waitingPlayers = new ArrayList<Player>();
		this.connectedPlayers = connectedPlayers;
	}
	
	/** Check if this client can join a game, create one otherwise
	 * @param ip : the client ip
	 */
	private void enterInMatchMaking(MatchMakingPacket packet) 
	{
		// TODO : Check if there is a player with good elo
		// If yes create a game + stop the waiter
		// If no put this player in a waiting thread
		// NOTE : Have a look on server MatchMakingPacket
	}
	
	public int howManyPlayerWainting()
	{
		return waitingPlayers.size();
	}
}
