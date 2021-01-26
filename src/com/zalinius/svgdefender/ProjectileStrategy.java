package com.zalinius.svgdefender;

import java.awt.Color;

import com.zalinius.svgdefender.physics.Physical;
import com.zalinius.zje.physics.Vector;

public interface ProjectileStrategy {
	
	public Vector force(Physical projectile);
	public Color color();
	public double initialSpeed();
}
