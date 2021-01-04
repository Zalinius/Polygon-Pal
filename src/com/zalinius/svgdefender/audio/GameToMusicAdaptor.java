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
		double min = 0.2;
		double max = 0.6;
		
		return (float) Interpolate.linearMapping(gameInfo.polygonMaxSize(), gameInfo.polygonMinSize(), gameInfo.polygonSize(), min, max);
	}
	
	
	public float beatsPerMinutes() {
		return 120f;
	}

}
