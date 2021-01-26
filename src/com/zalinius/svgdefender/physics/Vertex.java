package com.zalinius.svgdefender.physics;

import java.awt.geom.Point2D;

import com.zalinius.zje.architecture.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public class Vertex implements Physical, Locatable{
	private Point position;
	private Vector velocity;
	private double mass;
	
	public Vertex(double x, double y) {
		this(new Point(x, y));
	}
	public Vertex(Point position) {
		this(position, 1);
	}
	public Vertex(Point position, double mass) {
		this.position = position;
		velocity = new Vector();
		this.mass = mass;		
	}
	
	/**
	 * Adds an impulse (instantaneous change of momentum) to a vertex
	 * @param momentum in kg*px/s
	 */
	public void impulse(Vector momentum) {
		velocity = velocity.add(momentum.scale(1d/mass));
	}

	public void update(Vector force, double delta) {
		velocity = velocity.add(force.scale(delta/mass));
		position = position.add(velocity.scale(delta));
	}

	public void update(double delta) {
		position = position.add(velocity.scale(delta));
	}

	public double mass(){return mass;}
	
	public double x() {return position.x;}
	public double y() {return position.y;}
	public Point center() { return position;}
	public Vector velocity() {return velocity;}
	public Vector momentum() {return velocity.scale(mass);}
	
	public Point2D getPoint2D() {return new Point2D.Double(position.x, position.y);}

}
