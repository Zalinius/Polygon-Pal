package com.zalinius.svgdefender.audio.pitch;

import java.util.Arrays;

import net.beadsproject.beads.data.Pitch;

public class AbsolutePitch {
	
	private int midiValue;
	
	public AbsolutePitch(int note) {
		this.midiValue = note;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(Pitch.major));
		
	}
	
	public int midiPitch() {
		return midiValue;
	}
	
	public float frequency() {
		return Pitch.mtof(midiValue);
	}
	
	
	
	public static final AbsolutePitch C4 = new AbsolutePitch(60);
	public static final AbsolutePitch D4 = new AbsolutePitch(62);
	public static final AbsolutePitch E4 = new AbsolutePitch(64);
	public static final AbsolutePitch F4 = new AbsolutePitch(65);
	public static final AbsolutePitch G4 = new AbsolutePitch(67);
	public static final AbsolutePitch A4 = new AbsolutePitch(69);
	public static final AbsolutePitch B4 = new AbsolutePitch(71);
	public static final AbsolutePitch C5 = new AbsolutePitch(72);
	
	public static final int OCTAVE_LENGTH = 12;
	
	public static final AbsolutePitch MIDDLE_C = C4;

}
