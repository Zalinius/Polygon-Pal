package com.zalinius.svgdefender;

import java.awt.Color;

import com.zalinius.physics.Vector;

public interface ProjectileStrategy {
	
	public Vector force(Physical projectile);
	public Color color();
	public double initialSpeed();
}
