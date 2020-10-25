package com.zalinius.SVGDefender;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.zalinius.architecture.GameContainer;
import com.zalinius.architecture.Graphical;
import com.zalinius.architecture.Logical;
import com.zalinius.architecture.input.Clickable;
import com.zalinius.physics.Vector;

public class SVGDefender extends GameContainer {
	
	private Graphical rect;
	private double theta;
	private AffineTransform transform;
	private PolygonReactor react;
	
	public SVGDefender() {
		super("SVG Defender", 1920, 1000, Color.BLACK);
		transform = new AffineTransform();
		rect = new TransformedShape(new Rectangle2D.Double(50, 50, 100, 200), transform);
		theta = 0;
		react = new PolygonReactor(mouseLocator());
		
		addControls(null, mouseInputs());
	}
	
	
	
	public List<Clickable> mouseInputs(){
		List<Clickable> mouseInput = new ArrayList<>();
		
		mouseInput.add(react);
		
		return mouseInput;
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
		game.startGame();
	}



	@Override
	public Vector resolution() {
		return new Vector(1800, 1000);
	}

}
