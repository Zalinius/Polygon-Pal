package com.zalinius.svgdefender.audio.pitch;

import java.util.Arrays;

import net.beadsproject.beads.data.Pitch;

public class Note {
	
	private int midiValue;
	
	public Note(int note) {
		this.midiValue = note;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(Pitch.major));
		
	}
	
	public int midiNote() {
		return midiValue;
	}
	
	public float frequency() {
		return Pitch.mtof(midiValue);
	}
	
	
	
	public static final Note C4 = new Note(60);
	public static final Note D4 = new Note(62);
	public static final Note E4 = new Note(64);
	public static final Note F4 = new Note(65);
	public static final Note G4 = new Note(67);
	public static final Note A4 = new Note(69);
	public static final Note B4 = new Note(71);
	public static final Note C5 = new Note(72);
	
	public static final int OCTAVE_LENGTH = 12;
	
	public static final Note MIDDLE_C = C4;

}
