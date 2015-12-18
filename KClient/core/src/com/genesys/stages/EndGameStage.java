package com.genesys.stages;

import Packets.ClientDataPacket;
import Packets.FriendsDataPacket;
import Packets.LadderDataPacket;
import Packets.NewsPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.genesys.kclient.Kangaroo;
import com.genesys.kclient.Main;
import com.genesys.kclient.PersoBloc;

public class EndGameStage extends Stage 
{
	/*
	 * Attributes
	 */
	/** Used as a wire between stage to access client for example */
	public Main main;
	
	/*
	 *  Components
	 */
	
	private PersoBloc player, opponent;
	private TextButton replay, back;
	private ClientDataPacket playerData;
	private LadderDataPacket ladderData;
	private FriendsDataPacket friendsData;
	private NewsPacket lastNews, lastBeforeNews;
	
	/*
	 * Constructors
	 */
	public EndGameStage(Main main, ClientDataPacket playerData, ClientDataPacket opponentData, Kangaroo winner, FriendsDataPacket friendsData, LadderDataPacket ladderData, NewsPacket lastNews, NewsPacket lastBeforeNews)
	{
		super();
		this.main = main;
		this.playerData = playerData;
		this.ladderData = ladderData;
		this.friendsData = friendsData;
		this.lastNews = lastNews;
		this.lastBeforeNews = lastBeforeNews;
		
		player = new PersoBloc(playerData, main.skin);
		player.setPosition(this.getWidth() / 4 - player.getWidth() / 2, this.getHeight() / 2 - player.getHeight() / 2 + 20);
		this.addActor(player);
		
		opponent = new PersoBloc(opponentData, main.skin);
		opponent.flipKangaroo();
		opponent.setPosition(this.getWidth() / 2 + this.getWidth() / 4 - opponent.getWidth() / 2, this.getHeight() / 2 - opponent.getHeight() / 2 + 20);
		this.addActor(opponent);
		
		replay = new TextButton("Rejouer", main.skin);
		replay.setSize(player.getWidth(), 40);
		replay.setColor(117f / 255f, 117f / 255f, 147f / 255f, 1);
		this.addActor(replay);
		
		back = new TextButton("Retour", main.skin);
		back.setSize(opponent.getWidth(), 40);
		back.setColor(147f / 255f, 117f / 255f, 117f / 255f, 1);
		this.addActor(back);
		
		if (winner.getName().equals(playerData.name))
		{
			player.setBackground(new Texture(Gdx.files.internal("sprites/bloc perso winner.png")));
			opponent.setBackground(new Texture(Gdx.files.internal("sprites/bloc perso looser.png")));
			
			replay.setPosition(player.getX(), player.getY() - 5 - replay.getHeight());
			back.setPosition(opponent.getX(), player.getY() - 5 - back.getHeight());
		}
		else
		{
			player.setBackground(new Texture(Gdx.files.internal("sprites/bloc perso looser.png")));
			opponent.setBackground(new Texture(Gdx.files.internal("sprites/bloc perso winner.png")));
			
			replay.setPosition(opponent.getX(), player.getY() - 5 - replay.getHeight());
			back.setPosition(player.getX(), player.getY() - 5 - back.getHeight());
		}
	}
	
	@Override
	public void act(float delta)
	{			
		// Input update
		if (Gdx.input.justTouched())
		{
			if (back.isPressed())
				main.setStage(new HomeStage(main, playerData, friendsData, ladderData, lastNews, lastBeforeNews));
		}
		super.act(delta);
	}
}
