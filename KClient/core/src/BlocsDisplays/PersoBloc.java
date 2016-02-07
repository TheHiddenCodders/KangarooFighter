package BlocsDisplays;

import Class.Bloc;
import Stages.HomeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class PersoBloc extends Bloc
{
	/*
	 * Attributes
	 */
	

	
	/*
	 * Components
	 */
	
	private Image kangaroo;
	private Label name, elo, eloI, games, win, loses;
	private Image winIcon, loseIcon;
	
	/*
	 * Constructors
	 */
	
	public PersoBloc(HomeStage homeStage) 
	{
		super(homeStage);
		
		// Set position and background
		setPosition(272, 42.5f);
		setBackground(new Texture(Gdx.files.internal("sprites/homestage/blocs/perso/background.png")));
		
		// Kangaroo
		kangaroo = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/perso/kangourou.png")));
		kangaroo.setPosition(this.getWidth() / 2 - kangaroo.getWidth() / 2, this.getHeight() - kangaroo.getHeight() - 50);
		kangaroo.getColor().a = 0.55f;
		addActor(kangaroo);
		
		// Labels
		name = new Label(homeStage.main.player.getName(), skin);
		name.setStyle(new LabelStyle(skin.getFont("korean"), Color.TAN));
		name.setPosition(this.getWidth() / 2 - name.getWidth() / 2, this.getHeight() - name.getHeight() - 10);
		addActor(name);
		
		elo = new Label(homeStage.main.player.getElo() + "", skin);
		elo.setStyle(new LabelStyle(skin.getFont("korean"), Color.TAN));
		elo.pack();
		elo.setPosition(this.getWidth() / 2 - elo.getWidth() / 2 - 15, kangaroo.getY() - elo.getHeight() - 10);
		addActor(elo);
		
		eloI = new Label(" elo" , skin);
		eloI.setStyle(new LabelStyle(skin.getFont("korean"), Color.WHITE));
		eloI.pack();
		eloI.setPosition(elo.getX() + elo.getWidth(), elo.getY());
		addActor(eloI);
		
		games = new Label(homeStage.main.player.getGames() + " parties" , skin);
		games.setStyle(new LabelStyle(skin.getFont("default-font"), Color.WHITE));
		games.setPosition(this.getWidth() / 2 - games.getWidth() / 2, elo.getY() - games.getHeight() - 2);
		addActor(games);
		
		win = new Label(homeStage.main.player.getWins() + "" , skin);
		win.setStyle(new LabelStyle(skin.getFont("default-font"), Color.TAN));
		win.setPosition(this.getWidth() / 4 - win.getWidth() / 2, games.getY() - win.getHeight() - 10);
		addActor(win);
		
		winIcon = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/perso/wins.png")));
		winIcon.setPosition(win.getX() + win.getWidth() + 5, win.getY());
		winIcon.scaleBy(0.2f);
		addActor(winIcon);
		
		loseIcon = new Image(new Texture(Gdx.files.internal("sprites/homestage/blocs/perso/loses.png")));
		loseIcon.setPosition(this.getWidth() / 2 + this.getWidth() / 4 - loseIcon.getWidth() * 1.5f, games.getY() - 33);
		loseIcon.scaleBy(0.2f);
		addActor(loseIcon);
		
		loses = new Label(homeStage.main.player.getLoses() + "" , skin);
		loses.setStyle(new LabelStyle(skin.getFont("default-font"), new Color(200f/255f, 80f/255f, 80f/255f, 1)));
		loses.setPosition(loseIcon.getX() + loseIcon.getWidth() + 5, win.getY());
		addActor(loses);
	}
	
	/*
	 * Methods
	 */
	
	@Override
	public void refresh(Object data) {
		// TODO Auto-generated method stub

	}
}
