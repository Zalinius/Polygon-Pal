package com.zalinius.polygonpal.physics;

import com.zalinius.zje.architecture.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public interface Physical extends Locatable{
	
	public double x();
	public double y();
	public Point center();
	public Vector velocity();
	
	public double mass();
	public Vector momentum();

}
