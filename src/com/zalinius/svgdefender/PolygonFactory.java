package com.zalinius.svgdefender;

import java.util.ArrayList;
import java.util.List;

import com.zalinius.physics.Point;

public class PolygonFactory {
	public static List<Point> simplePolygon(Point center, int sides) {
		double radius = PolygonReactor.polygonRadius(sides, PolygonReactor.segmentWidth());
		return regularPolygon(center, sides, radius);
	}
	
	
	public static List<Point> regularPolygon(Point center, int sides, double radius) {
		List<Point> points = new ArrayList<>();
		sides = Math.max(sides, 3);
		
		for(int i = 0; i != sides; ++i) {
			double angle = (2*Math.PI) * ((double)i / (double)sides);
			double x = radius * Math.cos(angle) + center.x;
			double y = radius * Math.sin(angle) + center.y;
			
			points.add(new Point(x, y));
		}
		
		return points;
	}
		
	public static List<Point> sillyPentagon(){
		List<Point> points = new ArrayList<>();
		points.add(new Point(250, 450));
		points.add(new Point(350, 450));
		points.add(new Point(400, 550));
		points.add(new Point(300, 600));
		points.add(new Point(200, 550));

		return points;
	}
}
