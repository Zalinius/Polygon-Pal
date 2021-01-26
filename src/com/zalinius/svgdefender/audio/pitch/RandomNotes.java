package com.zalinius.svgdefender.audio.pitch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.zalinius.zje.math.random.RandomIndexStrategy;
import com.zalinius.zje.math.random.RandomIndexStrategyFactory;

public class RandomNotes implements Iterator<AbsolutePitch>{
	
	private List<AbsolutePitch> notes;
	private RandomIndexStrategy randomStrategy;
	
	private static RandomIndexStrategy defaultRandomStrategy() {
		return RandomIndexStrategyFactory.random();
	}
	
	public RandomNotes(List<RelativePitch> relativeNotes, int root) {
		this(relativeNotes, root, defaultRandomStrategy());
	}
	public RandomNotes(List<RelativePitch> relativeNotes, int root, RandomIndexStrategy strategy) {
		this(relativeNotes.stream().map(note -> note.absoluteNote(root)).collect(Collectors.toList()), strategy);
	}
	
	public RandomNotes(List<AbsolutePitch> notes, RandomIndexStrategy strategy) {
		this.notes = notes;
		this.randomStrategy = strategy;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public AbsolutePitch next() {
		int index = randomStrategy.nextIndex(notes.size());
		return notes.get(index);
	}

	
}
