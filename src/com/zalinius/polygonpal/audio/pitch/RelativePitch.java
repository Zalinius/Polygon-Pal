package com.zalinius.polygonpal.audio.pitch;

import net.beadsproject.beads.data.Pitch;

public class RelativePitch {

	private int relativeMidiValue;
	
	public RelativePitch(int note) {
		this.relativeMidiValue = note;
	}

	public int midiNote(int root) {
		return root + relativeMidiValue;
	}
	public AbsolutePitch absoluteNote(int root) {
		return new AbsolutePitch(relativeMidiValue + root);
	}
	
	public float frequency(int root) {
		return Pitch.mtof(midiNote(root));
	}
}
