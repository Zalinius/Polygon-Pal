package com.zalinius.SVGDefender;

import com.zalinius.physics.Point;
import com.zalinius.physics.Vector;

public class Vertex {
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

	public void update(Vector force, double delta) {
		velocity = velocity.add(force.scale(delta/mass));
		position = position.add(velocity.scale(delta));
	}
	
	
	public double x() {return position.x;}
	public double y() {return position.y;}
	public Point position() { return position;}
	public Vector velocity() {return velocity;}

}
