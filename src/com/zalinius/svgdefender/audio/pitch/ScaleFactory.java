package com.zalinius.svgdefender.audio.pitch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.beadsproject.beads.data.Pitch;

public class ScaleFactory {

	private static EightNoteScale majorScale;
	private static EightNoteScale minorScale;


	public static EightNoteScale majorScale() {
		if(majorScale == null) {
			List<Integer> notes = new ArrayList<>();
			for (int i = 0; i < Pitch.major.length; i++) {
				notes.add(Pitch.major[i]);
			}
			notes.add(12);
			majorScale = makeEightNoteScale(notes);
		}
		return majorScale;
	}

	public static EightNoteScale minorScale() {
		if(minorScale == null) {
			List<Integer> notes = new ArrayList<>();
			for (int i = 0; i < Pitch.minor.length; i++) {
				notes.add(Pitch.major[i]);
			}
			notes.add(12);
			minorScale = makeEightNoteScale(notes);
		}
		return minorScale;
	}

	private static EightNoteScale makeEightNoteScale(List<Integer> relativeNotesNumbers) {
		if(relativeNotesNumbers.size() != 8) {
			throw new RuntimeException("Wrong number of notes: " + relativeNotesNumbers.size());
		}
		final List<RelativeNote> relativeNotes = new ArrayList<>();
		for (Iterator<Integer> it = relativeNotesNumbers.iterator(); it.hasNext();) {
			Integer relativeNoteNumber = it.next();
			relativeNotes.add(new RelativeNote(relativeNoteNumber));
		}

		return new EightNoteScale() {
			@Override
			public RelativeNote first() {return  relativeNotes.get(0);	}
			@Override
			public RelativeNote second() {return relativeNotes.get(1);  }
			@Override
			public RelativeNote third() {return  relativeNotes.get(2); 	}
			@Override
			public RelativeNote fourth() {return relativeNotes.get(3);	}
			@Override
			public RelativeNote fifth() {return  relativeNotes.get(4);	}
			@Override
			public RelativeNote sixth() {return  relativeNotes.get(5);	}
			@Override
			public RelativeNote seventh(){return relativeNotes.get(6);	}
			@Override
			public RelativeNote eighth() {return  relativeNotes.get(7);	}
			@Override
			public Iterator<RelativeNote> iterator() {
				return relativeNotes.iterator();
			}

		};
	}

	public static <E> List<E> arrayToList(E[] array){
		List<E> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	public static List<Integer> arrayToList(int[] array){
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
}
