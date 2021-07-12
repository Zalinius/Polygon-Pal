package com.zalinius.polygonpal.pal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zalinius.polygonpal.GameInterface;
import com.zalinius.polygonpal.Projectile;
import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class PolygonPalPlayer implements Graphical, Locatable {

	private List<Vertex> vertices;
	private List<Boolean> edges;
	private Vertex center;
	private PolygonPalFace face;

	private static final int EDGE_STIFFNESS = 30;
	private static final int BROKEN_EDGE_STIFFNESS = 20;
	private static final int CENTER_EDGE_STIFFNESS = 80;

	public PolygonPalPlayer(Point center, GameInterface gameState) {
		this(PolygonFactory.simplePolygon(center, 5), gameState);
	}

	public PolygonPalPlayer(List<Point> points, GameInterface gameState) {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
		for (Iterator<Point> it = points.iterator(); it.hasNext();) {
			Point point = it.next();
			vertices.add(new Vertex(point));
			edges.add(Boolean.TRUE);
		}
		center = new Vertex(Point.center(points), 5);

		directionOfInput = new Vector();

		this.face = new PolygonPalFace(gameState);
	}



	public void update(double delta, List<Projectile> collideables) {
		//Calculate all forces
		List<Vector> forces = new ArrayList<Vector>();
		for (int i = 0; i < vertices.size(); i++) {
			forces.add(new Vector());
		}
		Vector centerForce = new Vector();

		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex1 = vertices.get(i);
			Vertex vertex2 = vertices.get(Math.floorMod(i + 1, vertices.size()));

			boolean edge = edges.get(i);
			int stiffness;
			if(edge) {
				stiffness = EDGE_STIFFNESS;
			}
			else {
				stiffness = BROKEN_EDGE_STIFFNESS;
			}
			Vector forceOn1 = elasticForce(vertex1.position(), vertex2.position(), stiffness, segmentWidth());
			Vector forceOn2 = forceOn1.scale(-1);

			forces.set(i, forces.get(i).add(forceOn1));
			forces.set(Math.floorMod(i + 1, vertices.size()), forces.get(Math.floorMod(i + 1, vertices.size())).add(forceOn2));




			Vector centerForceOn1 = elasticForce(vertex1.position(), center.position(), CENTER_EDGE_STIFFNESS, polygonRadius(vertices.size(), segmentWidth()));
			Vector forceOf1OnCenter = centerForceOn1.scale(-1);

			forces.set(i, forces.get(i).add(centerForceOn1));
			centerForce = centerForce.add(forceOf1OnCenter);


			//friction
			Vector friction = frictionForce(vertex1.velocity());
			forces.set(i, forces.get(i).add(friction));
		}



		//Check for edge collisions
		for (Iterator<Projectile> it = collideables.iterator(); it.hasNext();) {
			Projectile p = it.next();

			for (int i = 0; i < edges.size(); i++) {
				Vertex vertex1 = vertices.get(i);
				Vertex vertex2 = vertices.get(Math.floorMod(i + 1, vertices.size()));
				Boolean edge = edges.get(i);

				if(edge && Collisions.intersection(p.shape(), new Line2D.Double(vertex1.x(), vertex1.y(), vertex2.x(), vertex2.y())) && !p.wasHit()) {
					p.hit();
					it.remove();
					edges.set(i, false);
					face.lostShield();

					Vector momentum = p.momentum();//TODO distribute between vertices based on proximity?
					vertex1.impulse(momentum);
					vertex2.impulse(momentum);
				}
			}
		}

		//Check for fill collision

		Shape interior = buildPath(true);
		for (Iterator<Projectile> it = collideables.iterator(); it.hasNext();) {
			Projectile p = it.next();	

			if(interior.contains(p.position().x, p.position().y)) {
				//find closest vertex, and remove it
				it.remove();
				center.impulse(p.momentum());

				Vertex closestVertex = null;
				for (Iterator<Vertex> itVertex = vertices.iterator(); itVertex.hasNext();) {
					Vertex vertex = itVertex.next();

					if(closestVertex == null || Point.distance(p.position(), vertex.position()) < Point.distance(p.position(), closestVertex.position())) {
						closestVertex = vertex;
					}
				}


				int hitVertex = vertices.indexOf(closestVertex);
				if(edges.get(hitVertex)) {
					hitVertex = Math.floorMod(hitVertex - 1, vertices.size());
				}
				vertices.remove(hitVertex);
				edges.remove(hitVertex);
				face.lostSide();
			}
		}

		//Check for input
		if(!directionOfInput.isZeroVector()) {
			double inputStrength = 2000;
			Vector direction = directionOfInput.normalize();
			Vector inputForce = direction.scale(inputStrength);
			centerForce = centerForce.add(inputForce);
		}

		if(vertices.size() == 0) {
			forces.clear();
			centerForce = frictionForce(center.velocity().scale(10));
		}

		//Apply all forces
		for (int i = 0; i < vertices.size(); ++i) {
			vertices.get(i).update(forces.get(i), delta);
		}
		center.update(centerForce, delta);
	}

	public static Vector frictionForce(Vector velocity) {
		double friction = .9;
		return velocity.scale(-1 * friction);
	}

	/**
	 * The force generated by a spring between two points
	 * @param p1 primary point 
	 * @param p2
	 * @return The elastic force acting on p1, which is the inverse of the one on p2
	 */
	public static Vector elasticForce(Point p1, Point p2, double stiffness, double equilibriumLength) {		
		Vector spring = new Vector(p1, p2);
		Vector direction = spring.normalize();
		double magnitude = stiffness * (spring.length() - equilibriumLength);

		return direction.scale(magnitude);
	}

	public Path2D buildPath(boolean ignoreMissingEdges) {
		Path2D path = new Path2D.Double();		
		if(vertices.size() == 0) {
			return path;
		}

		Iterator<Vertex> it = vertices.iterator();
		Iterator<Boolean> itEdge = edges.iterator();
		Vertex first = it.next();
		path.moveTo(first.x(), first.y());
		while (it.hasNext()) {
			Vertex vertex = it.next();
			Boolean edge = itEdge.next();
			if(ignoreMissingEdges || edge) {
				path.lineTo(vertex.x(), vertex.y());			
			}
			else {
				path.moveTo(vertex.x(), vertex.y());
			}
		}

		if(edges.get(edges.size()-1)) {
			path.lineTo(first.x(), first.y());
		}

		return path;
	}



	public boolean playerDestroyed() {
		return vertices.size() <= 2 || edges.size() <= 2;
	}

	public void render(Graphics2D g) {
		Path2D path = buildPath(false);
		Path2D area = buildPath(true);

		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.WHITE);
		g.draw(path);
		g.setColor(Color.RED);
		g.fill(area);

		g.setColor(Color.WHITE);


		face.renderEyesAndMouth(g, center.position());
	}



	public static double polygonRadius(int sides, double sideLength) {
		return sideLength / (2 * Math.cos(Math.toRadians(90) * ((sides - 2.0) / sides)));
	}

	public static double segmentWidth() {
		return 100;
	}

	public void heal() {
		for (int i = 0; i < edges.size(); i++) {
			edges.set(i, true);
		}
	}





	//INPUT//
	public List<Inputtable> inputs(){
		List <Inputtable> inputs = new ArrayList<>();

		inputs.add(directionInput(KeyEvent.VK_W, new Vector( 0, -1)));
		inputs.add(directionInput(KeyEvent.VK_A, new Vector(-1,  0)));
		inputs.add(directionInput(KeyEvent.VK_S, new Vector( 0,  1)));
		inputs.add(directionInput(KeyEvent.VK_D, new Vector( 1,  0)));

		return inputs;
	}
	private Vector directionOfInput;
	public Inputtable directionInput(int key, Vector direction) {
		return new Inputtable() {

			@Override
			public void released() {
				directionOfInput = directionOfInput.subtract(direction);
			}

			@Override
			public void pressed() {
				directionOfInput = directionOfInput.add(direction);		
			}

			@Override
			public int keyCode() {
				return key;
			}
		};
	}

	@Override
	public Point position() {
		return center.position();
	}

	public void kill() {
		vertices.clear();
		edges.clear();		
	}


	public int edges() {
		return edges.size();
	}
	public int shields() {
		int shields = 0;
		for (Iterator<Boolean> it = edges.iterator(); it.hasNext();) {
			Boolean status = it.next();
			if(status) {
				shields ++;
			}
		}

		return shields;
	}

}