package Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Kangaroo.Game;
import Kangaroo.Kangaroo;
import Packets.ClientReadyPacket;
import Packets.FriendsDataPacket;
import Packets.GameReadyPacket;
import Packets.HeartBeatPacket;
import Packets.HomePacket;
import Packets.KangarooClientPacket;
import Packets.LadderDataPacket;
import Packets.LoginPacket;
import Packets.MatchMakingPacket;
import Packets.NewsPacket;
import Packets.PlayerPacket;
import Packets.ServerInfoPacket;
import Packets.SignOutPacket;
import Utils.FileUtils;
import Utils.ServerUtils;
import enums.EndGameType;
import enums.ServerInfoType;

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
	private volatile ArrayList<Game> games;
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
			System.out.println("Client connected to the server with ip: " + cp.getIp());
		
		/*
		 * On connection, we'll build a kangaroo associated to the client.
		 * Then we'll add it to the kangaroos holder.
		 */
		kangaroos.add(new Kangaroo(cp));		
		
		// Give the info to server utils
		ServerUtils.kangaroos = kangaroos;
		
		// Update serverInfo for clients
		serverInfoUpdated(cp, ServerInfoType.ExceptMe);
	}

	@Override
	public void onReceive(Object o, ClientProcessor cp)
	{
		String clientIp = cp.getIp();
		
		if (debug)
			System.out.println("Received from "
			+ clientIp + ": " 
			+ o.toString());
		
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
			attemptToLogin(receivedPacket, clientIp);
			
			// Send to the client (who sent the packet) the updated packet
			this.send(cp, receivedPacket);
			
			// If server accepted the request, send client data
			if (receivedPacket.accepted)
			{				
				// Send to the client his data
				//type name = new type(arguments);this.send(cp, getKangarooFromIP(clientIp).getClientDataPacket());
				
				// Send to the client the last news
				this.send(cp, ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
				this.send(cp, ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));			

				// Send to the client his friends
				this.send(cp, getKangarooFromIP(clientIp).getFriendsDataPacket());
				
				// Send to the client the ladder and his pos
				this.send(cp, getKangarooFromIP(clientIp).getLadderDataPacket());
				
				// Send to his connected friends he is connected
				for (int i = 0; i < getKangarooFromIP(clientIp).getFriendsDataPacket().friendsName.size(); i++)
				{
					// If a friend is connected, send him his new friendsDataPacket
					if (getKangarooFromIP(clientIp).getFriendsDataPacket().friendsOnline.get(i))
						this.send(getKangarooFromName(getKangarooFromIP(clientIp).getFriendsDataPacket().friendsName.get(i)).getClient(), getKangarooFromName(getKangarooFromIP(clientIp).getFriendsDataPacket().friendsName.get(i)).getFriendsDataPacket());
				}
			}
		}
		
		/**
		 * Receive Login packet 
		 */
		else if (o.getClass().isAssignableFrom(SignOutPacket.class))
		{			
			SignOutPacket receivedPacket = (SignOutPacket) o;
			attemptToSignOut(receivedPacket, clientIp);
			
			// Send to the client (who sent the packet) the updated packet
			this.send(cp, receivedPacket);
			
			// If server accepted the request, send client data
			if (receivedPacket.accepted)
			{		
				// Send to the client his data
				this.send(cp, getKangarooFromIP(clientIp).getClientDataPacket());
				
				// Send to the client the last news
				this.send(cp, ServerUtils.getNewsPacket(ServerUtils.getLastNewsFiles().getName()));			
				this.send(cp, ServerUtils.getNewsPacket(ServerUtils.getLastBeforeNewsFiles().getName()));			

				// Send to the client his friends
				this.send(cp, getKangarooFromIP(clientIp).getFriendsDataPacket());
				
				// Send to the client the ladder and his pos
				this.send(cp, getKangarooFromIP(clientIp).getLadderDataPacket());
			}
		}
		
		/**
		 * Receive Server info packet
		 */
		else if (o.getClass().isAssignableFrom(ServerInfoPacket.class))
		{
			ServerInfoPacket receivedPacket = (ServerInfoPacket) o;
			receivedPacket.nGamesOnline = games.size(); 
			receivedPacket.nGamesPlayed = ServerUtils.getGamesFiles().size();
			receivedPacket.nKangaroosOnline = kangaroos.size(); 
			receivedPacket.nKangaroosRegistered = ServerUtils.getPlayersFiles().size();
			
			// Send to the client (who sent the packet) the updated packet
			this.send(cp, receivedPacket);
		}
		
		/**
		 * Receive a Match making packet
		 */
		else if (o.getClass().isAssignableFrom(MatchMakingPacket.class))
		{
			MatchMakingPacket receivedPacket = (MatchMakingPacket) o;
			
			if (receivedPacket.search && getGameFromIP(clientIp) == null)
				putOnMatchMaking(clientIp);
			
			else if (!receivedPacket.search && getGameFromIP(clientIp) != null)
			{
				games.remove(getGameFromIP(clientIp));
				serverInfoUpdated(cp, ServerInfoType.ALL);
			}
			
		}
		
		/**
		 * Receive an update kangaroo packet
		 */
		else if (o.getClass().isAssignableFrom(KangarooClientPacket.class))
		{
			KangarooClientPacket receivedPacket = (KangarooClientPacket) o;
			
			// Get the game where this kangaroo is
			Kangaroo sender = getKangarooFromIP(clientIp);
			System.err.println("Before update " + sender.getState());
			sender.updateFromPacket(receivedPacket);
			System.err.println("After update " + sender.getState());
		}
		
		/**
		 * Receive a client ready packet
		 */
		else if (o.getClass().isAssignableFrom(ClientReadyPacket.class))
		{
			// If the second client is ready - send gameReady
			if (o2 != null)
			{
				send(getKangarooFromIP((String) o2).getClient(), new GameReadyPacket());
				send(getKangarooFromIP(clientIp).getClient(), new GameReadyPacket());
				
				o2 = null;
				getGameFromIP(clientIp).run();
			}
			// If the first client is ready - wait for the second
			else
			{
				o2 = clientIp;
			}
		}
		
		/**
		 * Receive a ladder data packet
		 */
		else if (o.getClass().isAssignableFrom(LadderDataPacket.class))
		{
			// Re-send a filled ladder data packet to the sender
			LadderDataPacket packet = ServerUtils.getLadderDataPacket(getKangarooFromIP(clientIp));
			send(cp, o);		
		}
		
		/**
		 * Receive home packet
		 * Receive a ladder data packet
		 */
		else if (o.getClass().isAssignableFrom(HomePacket.class))
		{
			HomePacket packet = (HomePacket) o;
			
			packet.news = new NewsPacket[2];
			packet.ladderPlayers = new PlayerPacket[5];
			packet.serverInfos = new ServerInfoPacket();
			
			send(cp, packet);	
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
			System.out.println("Client disconnected from server with ip: " + cp.getIp());
		
		String clientIp = cp.getIp();
		
		try
		{
			// Check if he is in a game
			for (int i = 0; i < games.size(); i++)
			{
				if (games.get(i).getKangarooFromIp(clientIp) != null)
				{
					if (games.get(i).isPrepared())
						games.get(i).endGame(cp.getIp(), EndGameType.Disconnection);
					
					games.remove(games.get(i));
				}
			}
			
			// Store kangaroo friends packet before removing him
			FriendsDataPacket dcFriendsDataPacket = getKangarooFromIP(clientIp).getFriendsDataPacket();
			
			kangaroos.remove(getKangarooFromIP(clientIp));
						
			cp.getClient().close();
			clients.remove(cp);
			serverInfoUpdated(cp, ServerInfoType.ExceptMe);
			ServerUtils.kangaroos = kangaroos;
			
			// Send to his connected friends he is disconnected
			for (int i = 0; i < dcFriendsDataPacket.friendsName.size(); i++)
			{
				// If a friend is connected, send him his new friendsDataPacket
				if (dcFriendsDataPacket.friendsOnline.get(i))
					this.send(getKangarooFromName(dcFriendsDataPacket.friendsName.get(i)).getClient(), getKangarooFromName(dcFriendsDataPacket.friendsName.get(i)).getFriendsDataPacket());
			}
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * Methods
	 * Here, we'll put all the methods who will be used onReceive or onDisconnection
	 * Those methods threats packets
	 */
	
	/**
	 * Check if the client pseudo exists, if it exists, it check the password match with the pseudo
	 * @param packet the login packet
	 * @return true if it's exists and pwd match, false if isn't
	 */
	public void attemptToLogin(LoginPacket packet, String ip)
	{
		// If already log, unlog him
		for (int i = 0; i < kangaroos.size(); i++)
		{
			if (kangaroos.get(i).getName().equals(packet.pseudo))
			{
				packet.accepted = false;
				return;
			}
		}
		
		// Check fields
		for (File file : ServerUtils.getPlayersFiles())
		{
			// If pseudo exists
			if (file.getName().equals(packet.pseudo))
			{
				packet.pseudoExists = true;
				if (FileUtils.readString(new File(file.getAbsolutePath().concat("/pwd"))).get(0).equals(packet.pwd))
				{
					packet.pwdMatch = true;
					packet.accepted = true;				
					getKangarooFromIP(ip).setName(packet.pseudo);
					break;
				}
				else
				{
					packet.pwdMatch = false;
				}
				break;
			}
			else
			{
				packet.pseudoExists = false;
			}
		}		
	}	
	
	/**
	 * Check if the client pseudo exists, if it's not, it 
	 * @param packet the login packet
	 * @return true if it's already taken, false if isn't
	 */
	public void attemptToSignOut(SignOutPacket packet, String ip)
	{
		// Check fields
		for (File file : ServerUtils.getPlayersFiles())
		{
			// If pseudo exists
			if (file.getName().equals(packet.pseudo))
			{
				packet.pseudoExists = true;
			}
		}	
		
		if (!packet.pseudoExists)
		{
			packet.accepted = true;
			getKangarooFromIP(ip).setName(packet.pseudo);
			ServerUtils.newPlayer(packet.pseudo, packet.pwd);
		}
	}	
	
	/**
	 * Put k on matchmaking
	 * @param k the kangaroo to send in seekGame
	 */
	private void putOnMatchMaking(String ip)
	{
		Kangaroo k = getKangarooFromIP(ip);
		
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
			
			// Update serverInfo for clients
			serverInfoUpdated(k.getClient(), ServerInfoType.ALL);
			
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
			if (kangaroo.getClient().getIp().equals(ip))
				return kangaroo;
		
		return null;
	}
	
	/**
	 * Get kangaroo from name
	 * @param name
	 * @return the kangaroo or null
	 */
	public Kangaroo getKangarooFromName(String name)
	{
		for (Kangaroo k : kangaroos)
		{
			System.out.println(k.getName().equals(name));
			if (k.getName().equals(name))
			{
				return k;
			}
		}
		
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
			// Only game which are at least prepared
			if (game.isPrepared() || game.isRunning())
			{
				if (game.getK1().getClient().getIp().equals(ip) || game.getK2().getClient().getIp().equals(ip))
					return game;
			}
			else
			{
				if (game.getK1().getClient().getIp().equals(ip))
					return game;
			}
		}
		return null;
	}
	
	public ArrayList<Game> getAllGames()
	{
		return games;
	}
	
	public void displayAllKangaroos()
	{
		System.out.println("--------------------------");
		
		for (Kangaroo k : kangaroos)
		{
			System.out.println(k.getName() + " - " + k.getClient().getIp());
		}
		
		System.out.println("--------------------------");
	}
	
	public void displayAllGames()
	{
		System.out.println("--------------------------");
		
		for (int i = 0; i < games.size(); i++)
		{
			System.out.println("Game n°" + i);
			System.out.println(games.get(i).getK1().getName() + " - " + games.get(i).getK2().getName());
		}
		
		System.out.println("--------------------------");
	}
	
	/**
	 * serverInfoUpdated need to be called every time the serverInfos change to send it to clients
	 */
	public void serverInfoUpdated(ClientProcessor client, ServerInfoType requestType)
	{
		// Prepare the packet to send
		ServerInfoPacket infoPacket = new ServerInfoPacket();
		
		infoPacket.nGamesOnline = games.size(); 
		infoPacket.nGamesPlayed = ServerUtils.getGamesFiles().size();
		infoPacket.nKangaroosOnline = kangaroos.size(); 
		infoPacket.nKangaroosRegistered = ServerUtils.getPlayersFiles().size();
		
		// Send the packet
		
		if (requestType == ServerInfoType.ALL)
			this.send(-1,  infoPacket);
		else if (requestType == ServerInfoType.ExceptMe)
			this.sendExcept(client, infoPacket);
		else if (requestType == ServerInfoType.JustMe)
			this.send(client, infoPacket);
	}
}