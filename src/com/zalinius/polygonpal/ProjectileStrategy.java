package com.zalinius.polygonpal;

import java.awt.Color;

import com.zalinius.polygonpal.physics.Physical;
import com.zalinius.zje.physics.Vector;

public interface ProjectileStrategy {
	
	public Vector force(Physical projectile);
	public Color color();
	public double initialSpeed();
}
