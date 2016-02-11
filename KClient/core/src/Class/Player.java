package Class;

import java.util.ArrayList;

import Packets.FriendsPacket;
import Packets.PlayerPacket;

public class Player 
{
	/*
	 * Attributes
	 */
	
	private String name;
	private int elo;
	private int games; 
	private int loses;
	private int wins;
	private int pos;
	private ArrayList<PlayerPacket> friends;
	
	/*
	 * Constructors
	 */
	
	public Player()
	{
		
	}
	
	public Player(PlayerPacket packet)
	{
		update(packet);
	}
	
	/*
	 * Methods
	 */
	
	public void update(PlayerPacket packet)
	{
		name = packet.name;
		elo = packet.elo;
		games = packet.games;
		loses = packet.looses;
		wins = packet.wins;
		pos = packet.pos;
		friends = packet.friends;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getElo() {
		return elo;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public int getLoses() {
		return loses;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public ArrayList<PlayerPacket> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<PlayerPacket> friends) {
		this.friends = friends;
	}
	
}
