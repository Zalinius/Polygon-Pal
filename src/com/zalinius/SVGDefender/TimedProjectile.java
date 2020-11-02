package com.zalinius.SVGDefender;

public class TimedProjectile implements Comparable<TimedProjectile>{
	private final int startTime;
	private final Projectile projectile;
	
	public TimedProjectile(int startTime, Projectile projectile) {
		this.startTime = startTime;
		this.projectile = projectile;
	}

	@Override
	public int compareTo(TimedProjectile other) {
		return startTime - other.startTime;
	}

	public Projectile projectile() {
		return projectile;
	}

	public double startTime() {
		return startTime;
	}
}
