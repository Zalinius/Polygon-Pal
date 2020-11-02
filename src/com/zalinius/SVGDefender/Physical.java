package com.zalinius.SVGDefender;

import com.zalinius.architecture.Locatable;
import com.zalinius.physics.Point;
import com.zalinius.physics.Vector;

public interface Physical extends Locatable{
	
	public double x();
	public double y();
	public Point center();
	public Vector velocity();
	
	public double mass();
	public Vector momentum();

}
