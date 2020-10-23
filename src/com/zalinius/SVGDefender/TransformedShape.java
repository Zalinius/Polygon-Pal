package com.zalinius.SVGDefender;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import com.zalinius.architecture.Graphical;

public class TransformedShape implements Graphical {
	
	private Shape shape;
	private AffineTransform transform;
	
	public TransformedShape(Shape shape, AffineTransform transform) {
		this.shape = shape;
		this.transform = transform;
	}
	
	private AffineTransform getInverse() {
		AffineTransform inverse = null;
		try {
			inverse = transform.createInverse();
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException("Why did you make a non invertable transform??");
		}
		
		return inverse;
	}



	public void render(Graphics2D g) {
		g.transform(transform);
		g.draw(shape);
		g.transform(getInverse());

	}

}
