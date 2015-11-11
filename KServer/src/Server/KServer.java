package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Kangaroo.Game;
import Kangaroo.Kangaroo;
import Packets.ClientReadyPacket;
import Packets.GameFoundPacket;
import Packets.GameReadyPacket;
import Packets.HeartBeatPacket;
import Packets.LoginPacket;
import Packets.MatchMakingPacket;
import Packets.ServerInfoPacket;
import Packets.UpdateKangarooPacket;

/**
 * Server class, contain everything need to interact with clients.
 * @author Nerisma
 *
 */
public class KServer extends Server 
{
	// Attributes
	/** Since our server put kangaroos together, we need an array to store them. */
	private ArrayList<Kangaroo> kangaroos;
	/** We'll link our kangaroos to a game with this */
	private HashMap<ClientProcessor, Game> gameLinker;
	/** A list of all the games currently opened */
	private ArrayList<Game> games;
	/** In case two packet are needed for an action */
	private Object o2;
	
	private static final boolean debug = true;
	
	/**
	 * Simple constructor, build arrays, maps..
	 */
	public KServer()
	{
		kangaroos = new ArrayList<Kangaroo>();
		games = new ArrayList<Game>();
		gameLinker = new HashMap<ClientProcessor, Game>();
	}
	
	/* 
	 * Extended methods
	 */
	
	@Override
	public void onConnection(ClientProcessor cp)
	{
		if (debug)
			System.out.println("Client connected to the server with ip: " + cp.getClient().getInetAddress().getHostAddress());
		
		/*
		 * On connection, we'll build a kangaroo associated to the client.
		 * Then we'll add it to the kangaroos holder.
		 */
		kangaroos.add(new Kangaroo(cp));		 
	}

	@Override
	public void onReceive(Object o, ClientProcessor cp)
	{
		if (debug)
			System.out.println("Received: " + o.toString());
		
		/**
		 * Receive heartbeat (not implemented yet)
		 */
		if (o.getClass().isAssignableFrom(HeartBeatPacket.class))			
			cp.setLastHeartBeatAnswer(System.currentTimeMillis());
		
		/**
		 * Receive Login packet 
		 */
		else if (o.getClass().isAssignableFrom(LoginPacket.class))
		{			
			LoginPacket receivedPacket = (LoginPacket) o;
			receivedPacket.accepted = !isClientPseudoAlreadyTaken(receivedPacket);
			
			// Send to the client (who sent the packet) the updated packet
			this.send(cp, receivedPacket);
		}
		
		/**
		 * Receive Server info packet
		 */
		else if (o.getClass().isAssignableFrom(ServerInfoPacket.class))
		{
			ServerInfoPacket receivedPacket = (ServerInfoPacket) o;
			receivedPacket.nGamesOnline = games.size(); 
			receivedPacket.nGamesPlayed = 0; // TODO
			receivedPacket.nKangaroosOnline = kangaroos.size(); 
			receivedPacket.nKangaroosRegistered = 0; // TODO
			
			// Send to the client (who sent the packet) the updated packet
			this.send(cp, receivedPacket);
		}
		
		/**
		 * Receive a Match making packet
		 */
		else if (o.getClass().isAssignableFrom(MatchMakingPacket.class))
		{
			MatchMakingPacket receivedPacket = (MatchMakingPacket) o;
			
			if (receivedPacket.search && getGameFromIP(receivedPacket.ip) == null)
				putOnMatchMaking(receivedPacket);
			
			else if (!receivedPacket.search && getGameFromIP(receivedPacket.ip) != null)
				games.remove(getGameFromIP(receivedPacket.ip));
			
		}
		
		/**
		 * Receive an update kangaroo packet
		 */
		else if (o.getClass().isAssignableFrom(UpdateKangarooPacket.class))
		{
			UpdateKangarooPacket receivedPacket = (UpdateKangarooPacket) o;
			
			// Update kangaroo
			getKangarooFromIP(receivedPacket.ip).updateFromPacket(receivedPacket);
			
			// Send kangaroo to the opponent
			Game game = getGameFromIP(receivedPacket.ip);
			System.out.println("Game is null : " + (game == null));
			Kangaroo k = game.getKangarooFromOpponentIp(receivedPacket.ip);
			System.out.println("K is null : " + (k == null));
			send(k.getClient(), receivedPacket);
			
		}
		
		/**
		 * Receive a client ready packet
		 */
		else if (o.getClass().isAssignableFrom(ClientReadyPacket.class))
		{
			ClientReadyPacket packet = (ClientReadyPacket) o;
			
			if (o2 != null)
			{
				ClientReadyPacket packet2 = (ClientReadyPacket) o2;
				send(getKangarooFromIP(packet2.ip).getClient(), new GameReadyPacket());
				send(getKangarooFromIP(packet.ip).getClient(), new GameReadyPacket());
			}
			else
			{
				o2 = o;
			}
		}
		
		else
		{
			System.err.println("This packet isn't handled by Server side: " + o.getClass());
		}
			
	}

	@Override
	public void onDisconnection(ClientProcessor cp)
	{
		if (debug)
			System.out.println("Client disconnected from server with ip: " + cp.getClient().getInetAddress().getHostAddress());
		
		try
		{
			kangaroos.remove(getKangarooFromIP(cp.getClient().getInetAddress().getHostAddress()));
			
			for (Game game : games)
			{
				if (game.getKangarooFromIp(cp.getClient().getInetAddress().getHostAddress()) != null)
				{
					games.remove(game);
				}
			}
			cp.getClient().close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Methods
	 * Here, we'll put all the methods who will be used onReceive or onDisconnection
	 * Those methods threats packets
	 */
	
	/**
	 * Check if the client pseudo is already taken
	 * @param packet the login packet
	 * @return true if it's already taken, false if isn't
	 */
	public boolean isClientPseudoAlreadyTaken(LoginPacket packet)
	{
		for (int i = 0; i < kangaroos.size(); i++)
		{
			if (kangaroos.get(i).getName().equals(packet.pseudo))
			{
				if (debug)
					System.err.println("Pseudo \"" + packet.pseudo + "\" already taken.");
				
				return true;
			}
		}
		
		getKangarooFromIP(packet.ip).setName(packet.pseudo);
		return false;
	}	
	
	/**
	 * Put k on matchmaking
	 * @param k the kangaroo to send in seekGame
	 */
	private void putOnMatchMaking(MatchMakingPacket p)
	{
		Kangaroo k = getKangarooFromIP(p.ip);
		
		boolean gameFound = false;
		
		// Check if a game is waiting
		for (Game game : games)
		{
			// if a waiting game is found
			if ( game.isWaiting() )
			{
				// Link the player in this game
				game.linkKangaroo(k);
				gameLinker.put(k.getClient(), game);
				gameFound = true;
				
				System.out.println("Kangaroo : " + k.getName() + " found a game : " + games.indexOf(game));
				System.out.println("Kangaroo : " + game.getK1().getName() + " found another kangaroo to play");
				
				// Send to kangaroos that game have been found
				GameFoundPacket foundPacket = new GameFoundPacket();
				foundPacket.ip = game.getK1().getClient().remote.getAddress().getHostAddress();
				send(game.getK1().getClient(), foundPacket);
				
				foundPacket.ip = game.getK2().getClient().remote.getAddress().getHostAddress();
				send(game.getK2().getClient(), foundPacket);
				
				// Launch game
				game.init();
				
				break;
			}
		}
		
		// If the game was not found
		if (!gameFound)
		{
			// Create a new game, wait another player and send a request
			Game newGame = new Game(k);
			games.add( newGame );
			gameLinker.put(k.getClient(), newGame);
			
			System.out.println("Kangaroo : " + k.getName() + " created a game and wait : " + games.indexOf(newGame));
		}
	}
	
	/**
	 * Get a kangaroo from the ip of a client
	 * @param ip
	 * @return the associated kangaroo or null if no kangaroo exist with this ip
	 */
	public Kangaroo getKangarooFromIP(String ip)
	{
		for (Kangaroo kangaroo : kangaroos)
			if (kangaroo.getClient().remote.getAddress().getHostAddress().equals(ip))
				return kangaroo;
		
		return null;
	}
	
	/**
	 * Get a game from the ip of a client
	 * @param ip
	 * @return the associated game or null if no game exist with this ip
	 */
	public Game getGameFromIP(String ip)
	{
		for (Game game : games)
		{
			// Only game which are running
			if (game.isRunning())
			{
				if (game.getK1().getClient().remote.getHostString().equals(ip) || game.getK2().getClient().remote.getHostString().equals(ip))
					return game;
			}
		}
		return null;
	}
	
	public void displayAllKangaroos()
	{
		System.out.println("--------------------------");
		
		for (Kangaroo k : kangaroos)
		{
			System.out.println(k.getName() + " - " + k.getClient().getClient().getInetAddress().getHostAddress());
		}
		
		System.out.println("--------------------------");
	}
}
