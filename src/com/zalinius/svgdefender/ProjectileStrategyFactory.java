package com.zalinius.svgdefender;

import java.awt.Color;

import com.zalinius.architecture.Locatable;
import com.zalinius.physics.Point;
import com.zalinius.physics.Vector;
import com.zalinius.svgdefender.physics.Physical;

public class ProjectileStrategyFactory {
	
	public static ProjectileStrategy straightPath(double speed, double variation) {
		return new ProjectileStrategy() {
			
			@Override
			public Vector force(Physical projectile) {
				
				return new Vector();
			}

			@Override
			public Color color() {
				return Color.YELLOW;
			}

			@Override
			public double initialSpeed() {
				return speed + (Math.random() - 0.5) * variation;
			}
		};
	}
	
	public static ProjectileStrategy simpleFollow(final Locatable followee, double maxSpeed) {
		return new ProjectileStrategy() {
			
			@Override
			public Vector force(Physical projectile) {			
				Vector direction = new Vector(projectile.center(), followee.center()).normalize();
				Vector propulsion = direction.scale(maxSpeed);
				Vector friction = projectile.velocity().scale(-1);
				
				return propulsion.add(friction);
			}
			
			@Override
			public Color color() {
				return Color.CYAN;
			}

			@Override
			public double initialSpeed() {
				return maxSpeed/2;
			}
		};
	}
	
	public static ProjectileStrategy followWithCentripetalTurning(final Locatable followee, double maxSpeed, double turnRadius) {
		return new ProjectileStrategy() {			
			@Override
			public Vector force(Physical projectile) {
				Point target = followee.center();
				Vector velocity = projectile.velocity();
				double centripetalMagnitude = projectile.mass() * maxSpeed * maxSpeed / turnRadius;
				
				Vector travelRequired = new Vector(projectile.center(), target);
				Vector direction = velocity.rejection(travelRequired).normalize();
				
				return direction.scale(centripetalMagnitude);
			}

			@Override
			public Color color() {
				return Color.BLUE;
			}

			@Override
			public double initialSpeed() {
				return maxSpeed;
			}
		};
		
	}
}
