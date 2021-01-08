package com.zalinius.svgdefender.audio;

import com.zalinius.svgdefender.GameInterface;
import com.zalinius.svgdefender.Interpolate;
import com.zalinius.svgdefender.audio.pitch.EightNoteScale;
import com.zalinius.svgdefender.audio.pitch.ScaleFactory;
import com.zalinius.svgdefender.audio.synths.SoundFactory;

import net.beadsproject.beads.data.Buffer;

public class GameToMusicAdaptor {
	
	private GameInterface gameInfo;
	
	public GameToMusicAdaptor(GameInterface gameInfo) {
		this.gameInfo = gameInfo;
	}

	public float bassIntensity() {
		return 1f;		
	}
	

	public float themeIntensity() {
		return 1f;		
	}

	public Buffer themeSynth() {
		return SoundFactory.WIND_SOFT;		
	}
	
	public EightNoteScale themeScale() {
		if(gameInfo.level() + 1 == gameInfo.levelCount()) {
			return ScaleFactory.minorScale();
		}
		else {
			return ScaleFactory.majorScale();
		}
	}
	
	public boolean baseDrumActive() {
		return !gameInfo.isGameOver();
	}
	
	public float baseDrumIntensity() {
		double min = 0.3;
		double max = 0.8;
		
		return (float) Interpolate.linearMapping(gameInfo.polygonMaxSize(), gameInfo.polygonMinSize(), gameInfo.polygonSize(), min, max);
	}
	
	public int lightPercussionActivity() {
		int min = 0;
		int max = 4;

		int lowValue = 0;
		int highValue = 10;
		int nearbyEnnemies = gameInfo.ennemiesNearby() % highValue;
		
		double value = Interpolate.linearMapping(lowValue, highValue, nearbyEnnemies, min, max);		
		return (int)Math.round(value);
	}
	
	public float beatsPerMinutes() {
		double minBPM = 90;
		double maxBPM = 180;
		double current = Interpolate.linearMapping(1, gameInfo.levelCount(), gameInfo.level(), minBPM, maxBPM);

		return (float) current;
	}
	
	//in ms
	public float beatLength() {
		final int SECONDS_IN_MINUTE = 60;
		final int MILLISECONDS_IN_SECOND = 1000;
		return (SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND) / beatsPerMinutes();
	}

}
