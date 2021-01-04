package com.zalinius.svgdefender.audio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zalinius.svgdefender.audio.pitch.EightNoteScale;
import com.zalinius.svgdefender.audio.pitch.Note;
import com.zalinius.svgdefender.audio.pitch.PitchPlus;
import com.zalinius.svgdefender.audio.pitch.ScaleFactory;

public class Melody implements Iterable<Note> {
	private List<Note> notes;
	
	public Melody(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}
	
	public static Melody testMelody() {
		List<Note> notes= new ArrayList<>();
		EightNoteScale scale = ScaleFactory.majorScale();
		int root = 74;
		notes.add(scale.first().absoluteNote(74));
		notes.add(scale.second().absoluteNote(74));
		notes.add(scale.third().absoluteNote(74));
		notes.add(scale.fourth().absoluteNote(74));
		notes.add(scale.fifth().absoluteNote(74));
		notes.add(scale.sixth().absoluteNote(74));
		notes.add(scale.seventh().absoluteNote(74));
		notes.add(scale.eighth().absoluteNote(74));
		return new Melody(notes);
	}

}
