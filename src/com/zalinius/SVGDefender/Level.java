package com.zalinius.SVGDefender;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Level {
	
	private Deque<TimedProjectile> timedProjectiles;
	private double timePassed;
	public final String name;
	public final String message;
	public final boolean barrier;
	
	public Level(List<TimedProjectile> timedProjectiles, String name, String message, boolean getBarrier) {
		Collections.sort(timedProjectiles);
		this.timedProjectiles = new ArrayDeque<>(timedProjectiles);
		timePassed = 0d;
		
		this.name = name;
		this.message = message;
		this.barrier = getBarrier;
	}
	
	public void update(double delta, List<Projectile> activeProjectiles) {
		timePassed += delta;
		
		while(!timedProjectiles.isEmpty() && timePassed >= timedProjectiles.peek().startTime()) {
			Projectile readyProjectile = timedProjectiles.pop().projectile();
			activeProjectiles.add(readyProjectile);
		}
	}
	
	public boolean levelEmpty() {
		return timedProjectiles.isEmpty();
	}
	


}
