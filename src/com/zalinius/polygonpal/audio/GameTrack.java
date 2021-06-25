package com.zalinius.polygonpal.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zalinius.zje.music.Track;
import com.zalinius.zje.music.pitch.AbsolutePitch;
import com.zalinius.zje.music.pitch.IndexList;
import com.zalinius.zje.music.pitch.Melody;
import com.zalinius.zje.music.pitch.ScaleFactory;

import net.beadsproject.beads.core.UGen;

public class GameTrack extends Track {
	
	private final SoundFactory sf;
	private Melody bassMelody;
	private GameToMusicAdaptor musicInfo;

	public GameTrack(GameToMusicAdaptor musicInfo) {
		super(musicInfo.beatLength());
		this.musicInfo = musicInfo;		
		sf = new SoundFactory();
		bassMelody = createBassMelody();
	}
	
	@Override
	protected void update(int beats, long ticks) {
		if(ticks % 16 == 8) {
			AbsolutePitch pitch = bassMelody.next();
			float intensity = 0.4f;
			
			UGen bass = sf.bass(pitch.midiPitch(), intensity);
			ac.out.addInput(bass);
		}
		if(useLightPercussion(ticks, musicInfo.lightPercussionActivity())) {
			UGen perc = sf.lightPercussion(50, 0.01);
			ac.out.addInput(perc);
		}
		if(ticks % 16 == 0 && musicInfo.baseDrumActive()) {
			UGen perc = sf.heavyPercussion(100f, musicInfo.baseDrumIntensity());
			ac.out.addInput(perc);
		}
		
	}	
		
	public <E> List<E> removeNElementsRandomly(List<E> list, int n){
		if( n < 0 || n > list.size()) {
			throw new IndexOutOfBoundsException();
		}
		
		while(n > 0) {
			list.remove(new Random().nextInt(list.size()));
			--n;
		}
		
		return list;
	}

	private boolean useLightPercussion(long count, int percussionActivity) {
		count %= 32;
		switch (percussionActivity) {
		case 0:
			return count % 32 == 0;
		case 1:
			return count % 16 == 0;
		case 2:
			return count % 16 == 0 || count == 8;
		case 3:
			return count % 8 == 0;
		case 4:
			return (count % 16 / 4) != 3 && (count % 4 == 0);
		default:
			return count % 32 == 0;
		}
	}


	public static Melody createBassMelody() {
		List<Integer> indices = new ArrayList<>();
		indices.add(5);
		indices.add(5);
		indices.add(5);
		indices.add(5);

		indices.add(5);
		indices.add(6);
		indices.add(7);
		indices.add(6);

		indices.add(5);
		indices.add(5);
		indices.add(5);
		indices.add(5);

		indices.add(5);
		indices.add(4);
		indices.add(3);
		indices.add(2);

		indices.add(2);
		indices.add(2);
		indices.add(2);
		indices.add(2);

		indices.add(2);
		indices.add(3);
		indices.add(4);
		indices.add(3);

		indices.add(2);
		indices.add(2);
		indices.add(2);
		indices.add(2);

		indices.add(2);
		indices.add(3);
		indices.add(4);
		indices.add(5);

		return new Melody(new AbsolutePitch(AbsolutePitch.MIDDLE_C.midiPitch() - 3*AbsolutePitch.OCTAVE_LENGTH), ScaleFactory.majorScale(), new IndexList(indices));
	}



	public static float random(double x) {
		return (float)(Math.random() * x);
	}

	@Override
	public double getBeatLength() {
		return musicInfo.beatLength();
	}


}
