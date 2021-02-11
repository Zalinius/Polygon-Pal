package com.zalinius.polygonpal.audio.rhythm;

public class TimeMeasure {
	public final int noteValue;
	public final int notesPerBar;

	public TimeMeasure() {
		this.noteValue = 4;
		this.notesPerBar = 4;
	}
	
	
	@Override
	public String toString() {
		return notesPerBar + "/" + noteValue;
	}
}
