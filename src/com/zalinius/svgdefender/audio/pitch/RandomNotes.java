package com.zalinius.svgdefender.audio.pitch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomNotes implements Iterator<Note>{
	
	private List<Note> notes;
	private RandomIndexStrategy randomStrategy;
	
	private static RandomIndexStrategy defaultRandomStrategy() {
		return RandomIndexStrategyFactory.random();
	}
	
	public RandomNotes(List<RelativeNote> relativeNotes, int root) {
		this(relativeNotes, root, defaultRandomStrategy());
	}
	public RandomNotes(List<RelativeNote> relativeNotes, int root, RandomIndexStrategy strategy) {
		this(relativeNotes.stream().map(note -> note.absoluteNote(root)).collect(Collectors.toList()), strategy);
	}
	
	public RandomNotes(List<Note> notes, RandomIndexStrategy strategy) {
		this.notes = notes;
		this.randomStrategy = strategy;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Note next() {
		int index = randomStrategy.nextIndex(notes.size());
		return notes.get(index);
	}

	
}
