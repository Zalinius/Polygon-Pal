package com.zalinius.polygonpal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.zalinius.polygonpal.audio.GameToMusicAdaptor;
import com.zalinius.polygonpal.audio.GameTrack;
import com.zalinius.polygonpal.level.Level;
import com.zalinius.polygonpal.level.LevelFactory;
import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.architecture.input.Clickable;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.plugins.BackgroundColor;
import com.zalinius.zje.plugins.Plugin;

public class PolygonPalGame extends GameContainer implements GameInterface{
	
	private PolygonPalPlayer player;
	
	private List<Projectile> projectiles;
	private List<Barrier> barriers;
	private Level activeLevel;
	private LevelFactory levels;
	
	private boolean gameOver;
	
	private double timeSinceLevelStart;
	
	private GameTrack music;
	
		
	public PolygonPalGame() {
		super("Polygon Pal c:", res().width, res().height);
		player = new PolygonPalPlayer(new Point(playArea().getCenterX(), playArea().getCenterY()));
		
		projectiles = new LinkedList<>();
		barriers = new ArrayList<>();
		
		levels = new LevelFactory(player, playArea());
		
		activeLevel = levels.next();
		timeSinceLevelStart = 0;
		gameOver = false;
		
		music = new GameTrack(GameTrack.aContext(), new GameToMusicAdaptor(this));
		music.play();
		addControls(keyInputs(), mouseInputs());
	}
	
	@Override
	public List<Plugin> getPlugins() {
		List<Plugin> plugins = new ArrayList<>();
		plugins.add(new BackgroundColor(Color.BLACK));
		return plugins;
	}
	
	public static Dimension res() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		return dim;
	}
	
	public final double width = 1700;
	public final double height = 1000;
	public Rectangle2D.Double playArea(){
		return new Rectangle2D.Double(0, 0, res().getWidth(), res().getHeight());
	}
	
	/**
	 * @param playArea The area where projectiles shouldn't spawn, but should head towards
	 * @return a random projectile outside the play area, with a velocity pointing towards the area
	 */
	
	public List<Clickable> mouseInputs(){
		List<Clickable> mouseInput = new ArrayList<>();
		
		for (Iterator<Barrier> it = barriers.iterator(); it.hasNext();) {
			Barrier barrier = it.next();
			mouseInput.addAll(barrier.clickables());
		}
		
		return mouseInput;
	}
	public List<Inputtable> keyInputs(){
		List<Inputtable> keyInputs = new ArrayList<>();
		
		keyInputs.addAll(player.inputs());
		
		return keyInputs;
	}

	public void update(double delta) {
		activeLevel.update(delta, projectiles);
				
		for (Iterator<Barrier> it = barriers.iterator(); it.hasNext();) {
			Barrier barrier = it.next();
			barrier.update(delta, projectiles);
		}
		
		player.update(delta, projectiles);

		for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
			Projectile projectile = it.next();
			projectile.update(delta);
		}
		
		//Remove faraway projectiles
		for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
			Projectile projectile = it.next();
			Rectangle2D.Double playArea = playArea();
			Point center = new Point(playArea.getCenterX(), playArea.getCenterY());
			double velocityRelativeToCenter = Vector.dotProduct(new Vector(projectile.center(), center), projectile.momentum());
			if(velocityRelativeToCenter < 0 && !playArea.contains(projectile.center().point2D())) {
				it.remove();
			}
		}
		
		//Check if dead
		if(!gameOver && player.playerDestroyed()) {
			gameOver = true;
			changeLevel(LevelFactory.loseScreen());
			player.kill();
		}else if(isLevelComplete()) {
			if(levels.hasNext()) {
				nextLevel();
			}
		}
		
		timeSinceLevelStart += delta;
	}
	
	private void nextLevel() {
		changeLevel(levels.next());
	}
	
	private void changeLevel(Level level) {
		timeSinceLevelStart = 0;
		player.heal();
		activeLevel = level;
		if(activeLevel.barrier) {
			spawnBarrier();
		}

	}
	
	private void spawnBarrier() {
		Barrier newBarrier = new Barrier(new Point(200, 200), new Point(200, 470), mouseLocator());
		barriers.add(newBarrier);
		List<Clickable> newBarrierControls = new ArrayList<>();
		newBarrierControls.addAll(newBarrier.clickables());
		addControls(null, newBarrierControls);
	}
	
	public boolean isLevelComplete() {
		return activeLevel.levelEmpty() && projectiles.isEmpty();
	}

	public void render(Graphics2D g) {
		
		g.setColor(Color.RED);		
		player.render(g);
		for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
			Projectile projectile = it.next();
			projectile.render(g);
		}
		
		for (Iterator<Barrier> it = barriers.iterator(); it.hasNext();) {
			Barrier barrier = it.next();
			barrier.render(g);
		}
		
		
		double textDuration = 7;
		double textFadeInTime = 1;
		double textFadeOutTime = 4;
		double fadeInDuration = textFadeInTime;
		double fadeOutDuration = textDuration - textFadeOutTime;
		if(timeSinceLevelStart < textDuration) {
			Color color = Color.white;
			if(timeSinceLevelStart < textFadeInTime) {
				color = new Color(1, 1, 1, (float)(timeSinceLevelStart/ fadeInDuration));
			}
			else if(timeSinceLevelStart > textFadeOutTime) {
				color = new Color(1, 1, 1, 1f - (float)((timeSinceLevelStart - textFadeOutTime) / fadeOutDuration));
			}
			
			Font fontName = new Font(null, Font.BOLD , 50);
			Font fontMessage = new Font(null, 0 , 30);

			String name = activeLevel.name;
			String message = activeLevel.message;
			Rectangle2D rect = playArea();
			FontMetrics metricsName = g.getFontMetrics(fontName);
			FontMetrics metricsMessage = g.getFontMetrics(fontMessage);
			
		    double xName = (rect.getWidth() - metricsName.stringWidth(name)) / 2;
		    double xMessage = (rect.getWidth() - metricsMessage.stringWidth(message)) / 2;
		    double yName = ((rect.getHeight()/2 - metricsName.getHeight()) / 2) + metricsName.getAscent();
		    double yMessage = ((3*rect.getHeight()/2 - metricsMessage.getHeight()) / 2) + metricsMessage.getAscent();
		    

			
			g.setColor(color);
			
			g.setFont(fontName);
			g.drawString(activeLevel.name, (int)xName, (int) yName);

			g.setFont(fontMessage);
			g.drawString(activeLevel.message, (int)xMessage, (int) yMessage);
		}
	}
	
	public static void main(String[] args) {
		PolygonPalGame game = new PolygonPalGame();
		game.startGame();
	}

	
	
	@Override
	public int level() {
		return levels.getCurrentLevel();
	}

	@Override
	public int levelCount() {
		return levels.getTotalGameLevels();
	}

	@Override
	public int polygonSize() {
		return player.edges();
	}

	@Override
	public int polygonWalls() {
		return player.shields();
	}
	
	@Override
	public int polygonMaxSize() {
		return 5;
	}
	@Override
	public int polygonMinSize() {
		return 3;
	}

	@Override
	public boolean isGameOver() {
		return gameOver;
	}

	@Override
	public boolean hasWon() {
		return levels.won();
	}

	@Override
	public int ennemiesNearby() {
		double threshold = 300;
		int nearbyProjectiles = 0;
		List<Projectile> projectilesCopy = new ArrayList<>(projectiles);
		for (Iterator<Projectile> it = projectilesCopy.iterator(); it.hasNext();) {
			Projectile projectile = it.next();
			if(Point.distance(projectile.center(), player.center()) < threshold) {
				nearbyProjectiles ++;
			}
		}
		return nearbyProjectiles;
	}


}
