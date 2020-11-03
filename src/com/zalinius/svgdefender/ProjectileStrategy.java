package com.zalinius.svgdefender;

import java.awt.Color;

import com.zalinius.physics.Vector;
import com.zalinius.svgdefender.physics.Physical;

public interface ProjectileStrategy {
	
	public Vector force(Physical projectile);
	public Color color();
	public double initialSpeed();
}
