package com.zalinius.svgdefender.audio.pitch;

import net.beadsproject.beads.data.Pitch;

public class RelativeNote {

	private int relativeMidiValue;
	
	public RelativeNote(int note) {
		this.relativeMidiValue = note;
	}

	public int midiNote(int root) {
		return root + relativeMidiValue;
	}
	public Note absoluteNote(int root) {
		return new Note(relativeMidiValue + root);
	}
	
	public float frequency(int root) {
		return Pitch.mtof(midiNote(root));
	}
}
