package com.zalinius.polygonpal.level;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

public class NonPlayLevel extends Level {

	private BooleanSupplier levelCompletionCondition;

	public NonPlayLevel(String name, String message, BooleanSupplier levelCompletionCondition) {
		super(new ArrayList<>(), name, message, false);
		this.levelCompletionCondition = levelCompletionCondition;
	}
	
	@Override
	public boolean levelClear() {
		return levelCompletionCondition.getAsBoolean();
	}

}
