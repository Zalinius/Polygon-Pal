package com.zalinius.svgdefender.audio.pitch;

import java.util.Iterator;
import java.util.List;

public class Melody implements Iterator<Note> {

	private int root;
	private EightNoteScale scale;
	private IndexStrategy strategy;
	
	
	public Melody(Note root, EightNoteScale scale, IndexStrategy strategy) {
		this.root = root.midiNote();
		this.scale = scale;
		this.strategy = strategy;
	}
	
	
	@Override
	public boolean hasNext() {
		return true;
	}
	@Override
	public Note next() {
		return scale.nth(strategy.nextIndex()).absoluteNote(root);
	}
	
}
