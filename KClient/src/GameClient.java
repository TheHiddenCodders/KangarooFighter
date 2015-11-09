

import java.io.IOException;
import java.net.UnknownHostException;

import Client.Client;
import Client.Command;

public class GameClient extends Client
{
	// Attributes
	private Stage stageToUpdate;
	private String pseudo;
	
	//
	private boolean firstKangarooReceived = false;
	
	public GameClient(String ip, int port) throws UnknownHostException, IOException
	{
		super(ip, port);
	}

	@Override
	public void onServerRequest(String request)
	{
		System.out.println("Received from server:" + request);
		// Transform resquest to command
		Command command = new Command(request);
		
		if (stageToUpdate.getClass().isAssignableFrom(MainStage.class))
		{
			@SuppressWarnings("unused")
			MainStage stage = (MainStage) stageToUpdate;
			// Update stage here
			
		}
		else if (stageToUpdate.getClass().isAssignableFrom(HomeStage.class))
		{
			HomeStage stage = (HomeStage) stageToUpdate;

			// Update stage here
			if (command.getType().equals("log"))
			{
				if (command.getArgs()[0].equals("succeed"))
					stage.setLogSucceed();
				else if (command.getArgs()[0].equals("failed"))
					stage.setWaitingForLogin();
				else
					System.out.println("Command \"" + request + "\" unknown");
			}
		}
		else if (stageToUpdate.getClass().isAssignableFrom(MatchMakingStage.class))
		{
			MatchMakingStage stage = (MatchMakingStage) stageToUpdate;
			
			// Update stage here
			if (command.getType().equals("seekGame"))
			{
				if (command.getArgs()[0].equals("kangarooFound"))
				{
					if (!firstKangarooReceived)
					{
						stage.setPlayerBuilder(request);
						firstKangarooReceived = true;
					}
					else
					{
						stage.setOpponentBuilder(request);
						firstKangarooReceived = false;
					}
				}
				else if (command.getArgs()[0].equals("seek"))
				{
					stage.setSeekingPlayer();
				}
				else
					System.out.println("Command \"" + request + "\" unknown");
			}
		}
		else if (stageToUpdate.getClass().isAssignableFrom(GameStage.class))
		{
			GameStage stage = (GameStage) stageToUpdate;
			
			// Update stage here
			if (command.getType().equals("game"))
			{
				if (command.getArgs()[0].equals("start"))
				{
					stage.setgameState("START");
				}
				else if (command.getArgs()[0].equals("kangaroo"))
				{
					stage.setKangarooUpdate(request);
					System.out.println("Update kangaroo request :" + request);
				}
				else
					System.out.println("Command \"" + request + "\" unknown");
			}
		}
		else 
		{
			System.err.println("Stage \"" + stageToUpdate.getClass().getSimpleName() + "\" is unknown");
		}
	}
	
	public void setStage(Stage stage)
	{
		this.stageToUpdate = stage;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
}
