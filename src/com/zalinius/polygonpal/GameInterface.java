package com.zalinius.polygonpal;

public interface GameInterface {
	
	public int level();
	public int levelCount();
	
	public int polygonSize();
	public int polygonMinSize();
	public int polygonMaxSize();
	public int polygonWalls();
	
	public boolean isAlive();
	public boolean isGameOver();
	public boolean hasWon();
	
	public int ennemiesNearby();
}
