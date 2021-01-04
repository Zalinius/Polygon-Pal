package com.zalinius.svgdefender.level;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.zalinius.architecture.Locatable;
import com.zalinius.physics.Point;
import com.zalinius.physics.Vector;
import com.zalinius.svgdefender.Projectile;
import com.zalinius.svgdefender.ProjectileStrategy;
import com.zalinius.svgdefender.ProjectileStrategyFactory;
import com.zalinius.svgdefender.TimedProjectile;

public class LevelFactory implements Iterator<Level>{

	private Locatable player;
	private Rectangle2D.Double playArea;
	private Iterator<Level> levelIterator;

	private int totalGameLevels;
	private int currentLevel; // 0 <= currentLevel < totalGameLevels

	public LevelFactory(Locatable player, Double playArea) {
		this.player = player;
		this.playArea = playArea;
		this.levelIterator = makeLevels().iterator();
	}


	private List<Level> makeLevels(){
		List<Level> levels = new ArrayList<>();
		levels.add(level1());
		levels.add(level2());
		levels.add(level3());
		levels.add(level4());
		levels.add(level5());
		totalGameLevels = levels.size();
		currentLevel = 0;

		levels.add(winScreen());

		return levels;
	}

	private Level level1() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		List<Projectile> projectiles = makeOffscreenProjectiles(10, ProjectileStrategyFactory.straightPath(100, 20));
		for (int i = 0; i < projectiles.size(); i++) {
			timedProjectiles.add(new TimedProjectile(i, projectiles.get(i)));
		}

		return new Level(timedProjectiles, "Level 1", "Avoid the attackers, with WASD", false);
	}

	private Level level2() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		List<Projectile> projectiles = makeOffscreenProjectiles(10, ProjectileStrategyFactory.straightPath(125, 50));
		for (int i = 0; i < projectiles.size(); i++) {
			timedProjectiles.add(new TimedProjectile(i*8/10, projectiles.get(i)));
		}

		return new Level(timedProjectiles, "Level 2", "Your shield will be repaired every level", false);
	}

	private Level level3() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		List<Projectile> projectiles = makeOffscreenProjectiles(15, ProjectileStrategyFactory.straightPath(125, 50));
		projectiles.addAll(makeOffscreenProjectiles(5, ProjectileStrategyFactory.followWithCentripetalTurning(player, 75, 150)));
		for (int i = 0; i < projectiles.size(); i++) {
			timedProjectiles.add(new TimedProjectile(i, projectiles.get(i)));
		}

		return new Level(timedProjectiles, "Level 3", "Drag the barrier to defend yourself", true);
	}

	private Level level4() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		List<Projectile> projectiles = makeOffscreenProjectiles(10, ProjectileStrategyFactory.straightPath(175, 50));
		projectiles.addAll(makeOffscreenProjectiles(10, ProjectileStrategyFactory.followWithCentripetalTurning(player, 125, 300)));
		Collections.shuffle(projectiles);
		for (int i = 0; i < projectiles.size(); i++) {
			timedProjectiles.add(new TimedProjectile(i*7/10, projectiles.get(i)));
		}

		return new Level(timedProjectiles, "Level 4", "", false);
	}

	private Level level5() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		List<Projectile> projectiles = makeOffscreenProjectiles(10, ProjectileStrategyFactory.straightPath(175, 50));
		projectiles.addAll(makeOffscreenProjectiles(10, ProjectileStrategyFactory.followWithCentripetalTurning(player, 125, 300)));
		projectiles.addAll(makeOffscreenProjectiles(5, ProjectileStrategyFactory.simpleFollow(player, 300)));
		Collections.shuffle(projectiles);
		for (int i = 0; i < projectiles.size(); i++) {
			timedProjectiles.add(new TimedProjectile(i*5/10, projectiles.get(i)));
		}

		List<Projectile> instantProjectiles = makeOffscreenProjectiles(10, ProjectileStrategyFactory.straightPath(125, 50));
		for (int i = 0; i < instantProjectiles.size(); i++) {
			timedProjectiles.add(new TimedProjectile(10, instantProjectiles.get(i)));
		}


		return new Level(timedProjectiles, "Final Level", "", true);
	}

	private Level winScreen() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		timedProjectiles.add(new TimedProjectile(Integer.MAX_VALUE, null));
		return new Level(timedProjectiles, "Victory", "", false);
	}

	public static Level loseScreen() {
		List<TimedProjectile> timedProjectiles = new ArrayList<>();
		timedProjectiles.add(new TimedProjectile(Integer.MAX_VALUE, null));
		return new Level(timedProjectiles, "Defeat", "How Tragic", false);
	}

	public List<Projectile> makeOffscreenProjectiles(int n, ProjectileStrategy strategy){
		List<Projectile> projectiles = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			Projectile p = createOffScreenProjectile(strategy);
			projectiles.add(p);
		}

		return projectiles;
	}



	//	
	//	public Level testLevel() {
	//		List<TimedProjectile> timedProjectiles = new ArrayList<>();
	//		for (int i = 0; i < 20; i++) {
	//			Projectile p = createOffScreenProjectile(ProjectileStrategyFactory.followWithCentripetalTurning(player, 90, 150));
	//			timedProjectiles.add(new TimedProjectile(i, p));
	//		}
	//		
	//		return new Level(timedProjectiles);
	//	}

	public Projectile createOffScreenProjectile(ProjectileStrategy strategy) {
		double spawnAngle = Math.random() * 2 * Math.PI;

		double width = playArea.getWidth();
		double height = playArea.getHeight();
		Point gameCenter = new Point(playArea.getCenterX(), playArea.getCenterY());

		double spawnRadius = Math.sqrt(width * width + height * height) / 2d;
		spawnRadius *=  1.1; //safetymargin

		Vector spawnVector = new Vector(Math.cos(spawnAngle), Math.sin(spawnAngle)).scale(spawnRadius);
		Point spawnLocation = gameCenter.add(spawnVector);

		double angleVariation = Math.random()/2 - 0.25;
		Vector initialDirection = new Vector(Math.cos(spawnAngle + angleVariation), Math.sin(spawnAngle  +angleVariation)).scale(spawnRadius);

		Vector initialVelocity = initialDirection.normalize().scale(-1).scale(strategy.initialSpeed());
		return new Projectile(spawnLocation, initialVelocity, strategy);
	}


	@Override
	public boolean hasNext() {
		return levelIterator.hasNext();
	}


	@Override
	public Level next() {
		if(currentLevel + 1 < totalGameLevels) {
			currentLevel ++;
		}
		
		return levelIterator.next();
	}


	public int getCurrentLevel() {
		return currentLevel;
	}
	public int getTotalGameLevels() {
		return totalGameLevels;
	}
	public boolean won() {
		return currentLevel == totalGameLevels;
	}

}
