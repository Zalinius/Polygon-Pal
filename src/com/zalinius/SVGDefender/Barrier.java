package com.zalinius.SVGDefender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zalinius.architecture.Graphical;
import com.zalinius.architecture.Locatable;
import com.zalinius.architecture.input.Clickable;
import com.zalinius.physics.Point;
import com.zalinius.physics.Vector;

public class Barrier implements Graphical {
	private Vertex v1, v2;
	private DraggableVertex dv1, dv2;
	private double minStableLength;
	private double maxStableLength;
	private double stiffness;
	
	private Locatable mouse;
	
	public Barrier(Point p1, Point p2, Locatable mouse) {
		v1 = new Vertex(p1, 20);
		v2 = new Vertex(p2, 20);
		
		dv1 = new DraggableVertex(v1, v2, mouse);
		dv2 = new DraggableVertex(v2, v1, mouse);
		
		this.minStableLength = 200; 
		this.maxStableLength = 300; 
		this.stiffness = 700;
		this.mouse = mouse;
	}

	public void update(double delta, List<Projectile> collideables) {
		
		Vector forceOn1 = permissiveElasticForce(v1.center(), v2.center(), stiffness, minStableLength, maxStableLength);
		Vector forceOn2 = forceOn1.scale(-1);
		
		
		//Check for edge collisions
		for (Iterator<Projectile> it = collideables.iterator(); it.hasNext();) {
			Projectile p = it.next();
			
			if(PolygonReactor.intersection(p.shape(), new Line2D.Double(v1.x(), v1.y(), v2.x(), v2.y())) && !p.wasHit()) {
				p.hit();
				it.remove();
				
				Vector momentum = p.momentum();//TODO distribute between vertices based on proximity?
				
				double distanceFromV1 = Point.distance(p.center(), v1.center());
				double distanceFromV2 = Point.distance(p.center(), v2.center());
				
				double momentumPortionV1 = distanceFromV2 / (distanceFromV1 + distanceFromV2);
				double momentumPortionV2 = distanceFromV1 / (distanceFromV1 + distanceFromV2);
				
				v1.impulse(momentum.scale(momentumPortionV1));
				v2.impulse(momentum.scale(momentumPortionV2));	
			}
		}
		
		if(dv1.isDragging()) {
			forceOn1 = forceOn1.add(getDraggingForce(dv1, v1));
		}
		if(dv2.isDragging()) {
			forceOn2 = forceOn2.add(getDraggingForce(dv2, v2));
		}
		
		//Friction
		Vector friction1 = frictionForce(v1.velocity());
		forceOn1 = forceOn1.add(friction1);
		Vector friction2 = frictionForce(v2.velocity());
		forceOn2 = forceOn2.add(friction2);
		

		v1.update(forceOn1, delta);
		v2.update(forceOn2, delta);
	}
	
	private float thickness() {
		return (float) (-Point.distance(v1.center(), v2.center()) / 20d + 20d);
	}
	
	public Vector getDraggingForce(DraggableVertex dv, Vertex v) {
		Vector mouseDraggingForce = new Vector(v.center(), mouse.center());
		double maxDraggingForce = 9000;
		double distanceForMaxForce = 300; // in pixels
		double rawMagnitude = mouseDraggingForce.length() / distanceForMaxForce * maxDraggingForce;
		double magnitude = Math.min(rawMagnitude, maxDraggingForce);
		
		mouseDraggingForce = mouseDraggingForce.normalize().scale(magnitude);
		
		return mouseDraggingForce;
	}
	
	public static Vector frictionForce(Vector velocity) {
		double friction = 20;
		return velocity.scale(-1 * friction);
	}
	
	public static Vector permissiveElasticForce(Point p1, Point p2, double stiffness, double minStableLength, double maxStableLength) {
		double magnitude;
		Vector spring = new Vector(p1, p2);

		if(spring.length() <= minStableLength || spring.length() >= maxStableLength) {
			if(spring.length() < minStableLength) {
				magnitude = stiffness * (spring.length() - minStableLength);
			}else {
				magnitude = stiffness * (spring.length() - maxStableLength);
			}
		}
		else {
			magnitude = 0;
		}
		
		Vector direction = spring.normalize();

		return direction.scale(magnitude);
	}


	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(thickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.WHITE);
		g.draw(new Line2D.Double(v1.x(), v1.y(), v2.x(), v2.y()));	
		g.setColor(Color.DARK_GRAY);
		

		
		Shape triangle1 = equilateralTriangle(v1.center(), v2.center());
		Shape triangle2 = equilateralTriangle(v2.center(), v1.center());
		
		g.setColor(triangleColor(triangle1, dv1));
		g.fill(triangle1);
		g.setColor(triangleColor(triangle2, dv2));
		g.fill(triangle2);
	}
	
	public Color triangleColor(Shape triangle, DraggableVertex draggableVertex) {
		Color result;
		if(draggableVertex.isDragging()) {
			result = Color.LIGHT_GRAY;
		}
		else if(triangle.contains(mouse.center().point2D())) {
			result = Color.GRAY;
		}
		else {
			result = Color.DARK_GRAY;
		}
		
		return result;
	}
	
	public static Shape equilateralTriangle(Point center, Point target) {
		Vector alignment = new Vector(center, target);
		Path2D.Double triangle = new Path2D.Double();
		List<Point> trianglePoints = PolygonFactory.regularPolygon(center, 3, 30);
		triangle.moveTo(trianglePoints.get(0).x, trianglePoints.get(0).y);
		triangle.lineTo(trianglePoints.get(1).x, trianglePoints.get(1).y);
		triangle.lineTo(trianglePoints.get(2).x, trianglePoints.get(2).y);
		triangle.closePath();
		
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(alignment.x, alignment.y, center.x, center.y);
		triangle.transform(rotation);
		
		return triangle;
	}



	public List<Clickable> clickables(){
		List<Clickable> c = new ArrayList<>();
		c.add(dv1);
		c.add(dv2);
		return c;
	}


	
	private static class DraggableVertex implements Clickable{
		private Vertex vertex;
		private Locatable partnerVertex;
		private boolean dragging;
		private Locatable mouse;

		
		public DraggableVertex(Vertex vertex, Locatable partnerVertex, Locatable mouse) {
			this.vertex = vertex;
			this.partnerVertex = partnerVertex;
			this.mouse = mouse;
			this.dragging = false;
		}
		
		public Shape clickArea() {
			return equilateralTriangle(vertex.center(), partnerVertex.center());
		}

		public int mouseButtonCode() {
			return MouseEvent.BUTTON1;
		}

		public void mouseClicked() {
			//empty
		}

		public void mousePressed() {
			dragging = true;
		}

		public void mouseReleased() {
			dragging = false;
		}
		
		public boolean isDragging() {
			return dragging;
		}
	}
}
