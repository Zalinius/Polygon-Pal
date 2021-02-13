package com.zalinius.polygonpal.pal;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import com.zalinius.polygonpal.GameInterface;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.utilities.time.GameClock;

public class PolygonPalFace {
	
	private GameInterface gameState;
	
	private boolean justLostShield;
	private boolean justLostSide;
	
	private final double EYE_WIDTH = 10;
	
	public PolygonPalFace(GameInterface gameState) {
		this.gameState = gameState;
		
		this.justLostShield = false;
		this.justLostSide = false;
	}
	
	public void renderEyesAndMouth(Graphics2D g, Point center) {
		if(gameState.isAlive()) {
			if(gameState.hasWon()) {
				victoryFace(g, center);
			}
			else if(justLostSide) {
				hurtFace(g, center);
			}
			else if(justLostShield) {
				hitFace(g, center);
			}
			else if(gameState.ennemiesNearby() >= 2) {
				worriedFace(g, center);
			}
			else {
				happyFace(g, center);
			}
		}
		else {
			deadFace(g, center);
		}

	}
	
	public void lostShield() {
		justLostShield = true;
		GameClock.addActionTimer(()-> {justLostShield = false;}, 1);
	}
	public void lostSide() {
		justLostSide = true;
		GameClock.addActionTimer(()-> {justLostSide = false;}, .25);
	}

	
	private void happyFace(Graphics2D g, Point center) {
		openEyes(g, center, EYE_WIDTH);
		smile(g, center);
	}

	private void hitFace(Graphics2D g, Point center) {
		sadEyes(g, center, EYE_WIDTH);
		hurtSmile(g, center);
	}
	
	private void hurtFace(Graphics2D g, Point center) {
		deadEyes(g, center, EYE_WIDTH);
		frown(g, center);
	}

	private void worriedFace(Graphics2D g, Point center) {
		openEyes(g, center, EYE_WIDTH);
		surpriseFace(g, center);
	}

	
	private void deadFace(Graphics2D g, Point center) {
		deadEyes(g, center, EYE_WIDTH);
		frown(g, center);
	}

	private void victoryFace(Graphics2D g, Point center) {
		happyEyes(g, center, EYE_WIDTH);
		smile(g, center);
	}


	private void openEyes(Graphics2D g, Point center, double radius) {
		g.fill(centeredCircle(center.add(-20, -20), radius));
		g.fill(centeredCircle(center.add(20, -20), radius));
	}

	private void sadEyes(Graphics2D g, Point center, double radius) {
		g.fill(circleSegment(center.add(-2*radius, -2*radius), radius, 200, 250));
		g.fill(circleSegment(center.add(2*radius, -2*radius), radius, 90, 250));
	}
	
	private void happyEyes(Graphics2D g, Point center, double width) {
		double centerOffset = 15;
		Line2D.Double line1 = new Line2D.Double(center.add(-centerOffset-width, -centerOffset).point2D(), center.add(-centerOffset - width/2, -centerOffset - width/2).point2D());
		Line2D.Double line2 = new Line2D.Double(center.add(-centerOffset, -centerOffset).point2D(), center.add(-centerOffset - width/2, -centerOffset - width/2).point2D());
		Line2D.Double line3 = new Line2D.Double(center.add(centerOffset+width, -centerOffset).point2D(), center.add(centerOffset + width/2, -centerOffset - width/2).point2D());
		Line2D.Double line4 = new Line2D.Double(center.add(centerOffset, -centerOffset).point2D(), center.add(centerOffset + width/2, -centerOffset - width/2).point2D());
		g.draw(line1);
		g.draw(line2);
		g.draw(line3);
		g.draw(line4);
	}

	private void deadEyes(Graphics2D g, Point center, double width) {
		double centerOffset = 15;
		Line2D.Double line1 = new Line2D.Double(center.add(-centerOffset - width, -centerOffset - width).point2D(), center.add(-centerOffset, -centerOffset).point2D());
		Line2D.Double line2 = new Line2D.Double(center.add(-centerOffset, -centerOffset - width).point2D(), center.add(-centerOffset - width, -centerOffset).point2D());
		Line2D.Double line3 = new Line2D.Double(center.add(centerOffset + width, -centerOffset - width).point2D(), center.add(centerOffset, -centerOffset).point2D());
		Line2D.Double line4 = new Line2D.Double(center.add(centerOffset, -centerOffset - width).point2D(), center.add(centerOffset + width, -centerOffset).point2D());
		g.draw(line1);
		g.draw(line2);
		g.draw(line3);
		g.draw(line4);
	}

	private void smile(Graphics2D g, Point center) {
		g.drawArc((int)center.x - 10,(int) center.y, 20, 20, 180, 180);
	}

	private void surpriseFace(Graphics2D g, Point center) {
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawArc((int)center.x - 5,(int) center.y, 10, 10, 0, 360);
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	}

	private void frown(Graphics2D g, Point center) {
		g.drawArc((int)center.x - 10,(int) center.y, 20, 20, 0, 180);
	}

	private void hurtSmile(Graphics2D g, Point center) {
		g.setStroke(new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
		double width = 15;
		double angularExtent = 240;
		Arc2D.Double smileSection = new Arc2D.Double(center.x - width/2, center.y +5.0, width, width, (180 - angularExtent)/2, angularExtent, Arc2D.CHORD);
		g.draw(smileSection);
		
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	}



	public static Shape centeredCircle(Point center, double radius) {
		double x = center.x - radius;
		double y = center.y - radius;
		double diameter = 2*radius;
		return new Ellipse2D.Double(x, y, diameter, diameter);
	}

	public static Shape circleSegment(Point center, double radius, double startAngle, double extent) {
		double x = center.x - radius;
		double y = center.y - radius;
		double diameter = 2*radius;
		return new Arc2D.Double(x, y, diameter, diameter, startAngle, extent, Arc2D.CHORD);
	}

}
