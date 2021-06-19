package com.zalinius.polygonpal.audio;

import com.zalinius.polygonpal.GameInterface;
import com.zalinius.zje.math.Interpolation;

public class GameToMusicAdaptor {
	
	private GameInterface gameInfo;
	
	public GameToMusicAdaptor(GameInterface gameInfo) {
		this.gameInfo = gameInfo;
	}

	public float bassIntensity() {
		return 1f;		
	}	

	public boolean baseDrumActive() {
		return !gameInfo.isGameOver();
	}
	
	public float baseDrumIntensity() {
		double min = 0.3;
		double max = 0.8;
		
		return (float) Interpolation.linearMapping(gameInfo.polygonMaxSize(), gameInfo.polygonMinSize(), gameInfo.polygonSize(), min, max);
	}
	
	public int lightPercussionActivity() {
		int min = 0;
		int max = 4;

		int lowValue = 0;
		int highValue = 10;
		int nearbyEnnemies = gameInfo.ennemiesNearby() % highValue;
		
		double value = Interpolation.linearMapping(lowValue, highValue, nearbyEnnemies, min, max);		
		return (int)Math.round(value);
	}
	
	public float beatsPerMinutes() {
		double minBPM = 90;
		double maxBPM = 180;
		double current = Interpolation.linearMapping(0, gameInfo.levelCount() - 1 , gameInfo.level(), minBPM, maxBPM);

		return (float) current;
	}
	
	//in ms
	public float beatLength() {
		final int SECONDS_IN_MINUTE = 60;
		final int MILLISECONDS_IN_SECOND = 1000;
		return (SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND) / beatsPerMinutes();
	}

}
