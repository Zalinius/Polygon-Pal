package com.zalinius.polygonpal.audio.pitch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.beadsproject.beads.data.Pitch;

public class ScaleFactory {

	private static EightPitchScale majorScale;
	private static EightPitchScale minorScale;


	public static EightPitchScale majorScale() {
		if(majorScale == null) {
			List<Integer> notes = new ArrayList<>();
			for (int i = 0; i < Pitch.major.length; i++) {
				notes.add(Pitch.major[i]);
			}
			notes.add(12);
			majorScale = makeEightNoteScale(notes, "major");
		}
		return majorScale;
	}

	public static EightPitchScale minorScale() {
		if(minorScale == null) {
			List<Integer> notes = new ArrayList<>();
			for (int i = 0; i < Pitch.minor.length; i++) {
				notes.add(Pitch.minor[i]);
			}
			notes.add(12);
			minorScale = makeEightNoteScale(notes, "minor");
		}
		return minorScale;
	}

	private static EightPitchScale makeEightNoteScale(List<Integer> relativeNotesNumbers, String name) {
		if(relativeNotesNumbers.size() != 8) {
			throw new RuntimeException("Wrong number of notes: " + relativeNotesNumbers.size());
		}
		final List<RelativePitch> relativeNotes = new ArrayList<>();
		for (Iterator<Integer> it = relativeNotesNumbers.iterator(); it.hasNext();) {
			Integer relativeNoteNumber = it.next();
			relativeNotes.add(new RelativePitch(relativeNoteNumber));
		}

		return new EightPitchScale() {
			@Override
			public RelativePitch first() {return  relativeNotes.get(0);	}
			@Override
			public RelativePitch second() {return relativeNotes.get(1);  }
			@Override
			public RelativePitch third() {return  relativeNotes.get(2); 	}
			@Override
			public RelativePitch fourth() {return relativeNotes.get(3);	}
			@Override
			public RelativePitch fifth() {return  relativeNotes.get(4);	}
			@Override
			public RelativePitch sixth() {return  relativeNotes.get(5);	}
			@Override
			public RelativePitch seventh(){return relativeNotes.get(6);	}
			@Override
			public RelativePitch eighth() {return  relativeNotes.get(7);	}
			@Override
			public Iterator<RelativePitch> iterator() {
				return relativeNotes.iterator();
			}
			@Override
			public RelativePitch nth(int index) {
				return relativeNotes.get(index);
			}
			
			@Override
			public String toString() {
				return name + " scale";
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
