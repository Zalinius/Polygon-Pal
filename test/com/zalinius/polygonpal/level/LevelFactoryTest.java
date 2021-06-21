package com.zalinius.polygonpal.level;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.Test;

import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;

public class LevelFactoryTest {
	
	@Test
	void isFinalLevel_onFirstLevel_isFalse() throws Exception {
		LevelFactory levels = new LevelFactory(dummyLocatable(), dummyRectangle());
		
		Boolean result = levels.isFinalLevel();
		
		assertEquals(false, result);
	}

	@Test
	void isFinalLevel_onFinalLevel_isTrue() throws Exception {
		LevelFactory levels = new LevelFactory(dummyLocatable(), dummyRectangle());
		
		while (levels.hasNext()) {
			levels.next();
		}
		Boolean result = levels.isFinalLevel();
		
		assertEquals(true, result);
	}
	
	
	private Locatable dummyLocatable() {
		return () -> new Point();
	}
	private Rectangle2D dummyRectangle() {
		return new Rectangle2D.Double(0, 0, 1, 1);
	}

}
