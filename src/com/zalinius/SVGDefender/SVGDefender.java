package com.zalinius.SVGDefender;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.zalinius.architecture.GameContainer;
import com.zalinius.architecture.Graphical;
import com.zalinius.architecture.Logical;

public class SVGDefender implements Graphical, Logical {
	
	private Graphical rect;
	private double theta;
	private AffineTransform transform;
	private PolygonReactor react;
	
	public SVGDefender() {
		transform = new AffineTransform();
		rect = new TransformedShape(new Rectangle2D.Double(50, 50, 100, 200), transform);
		theta = 0;
		react = new PolygonReactor();
	}

	public void update(double delta) {
		theta = delta;
		transform.rotate(theta);
		react.update(delta);
	}

	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		rect.render(g);
		react.render(g);
	}
	
	public static void main(String[] args) {
		SVGDefender game = new SVGDefender();
		GameContainer container = new GameContainer(game, game);
		container.startGame();
	}

}
