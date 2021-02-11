package com.zalinius.polygonpal.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zalinius.polygonpal.audio.pitch.AbsolutePitch;
import com.zalinius.polygonpal.audio.pitch.IndexList;
import com.zalinius.polygonpal.audio.pitch.Melody;
import com.zalinius.polygonpal.audio.pitch.ScaleFactory;
import com.zalinius.polygonpal.audio.synths.SoundFactory;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Static;

public class GameTrack {


	private final AudioContext ac;
	private final Clock clock;
	private final SoundFactory sf;
	private Melody bassMelody;
	private GameToMusicAdaptor musicInfo;

	public void play() {
		clock.addMessageListener(trackBead());
		ac.out.addDependent(clock);
		ac.start();
	}

	public GameTrack(AudioContext ac, GameToMusicAdaptor musicInfo) {
		AudioContext.setDefaultContext(ac);
		this.ac = ac;
		clock = new Clock(ac, musicInfo.beatLength());

		this.musicInfo = musicInfo;		
		sf = new SoundFactory();
		bassMelody = createBassMelody();
	}

	public static AudioContext aContext() {
		//JavaSoundAudioIO.printMixerInfo();
		AudioContext ac;
		JavaSoundAudioIO jsaIO = new JavaSoundAudioIO();
		jsaIO.selectMixer(1);
		ac = new AudioContext(jsaIO);

		return ac;
	}


	public Bead trackBead() {
		return new Bead() {
			//this is the method that we override to make the Bead do something

			public void messageReceived(Bead message) {
				Clock c = (Clock)message;
				c.setIntervalEnvelope(new Static(musicInfo.beatLength()));
			
				if(c.getCount() % 16 == 8) {
					AbsolutePitch pitch = bassMelody.next();
					float intensity = 0.4f;
					
					UGen bass = sf.bass(pitch.midiPitch(), intensity);
					ac.out.addInput(bass);
				}
				if(useLightPercussion(c.getCount(), musicInfo.lightPercussionActivity())) {
					UGen perc = sf.lightPercussion(50, 0.01);
					ac.out.addInput(perc);
				}
				if(c.getCount() % 16 == 0 && musicInfo.baseDrumActive()) {
					UGen perc = sf.heavyPercussion(100f, musicInfo.baseDrumIntensity());
					ac.out.addInput(perc);
				}
			}
		};

	}
	
	
	
	public <E> List<E> createList(E[] elements){
		List<E> list = new ArrayList<>();
		for (int i = 0; i < elements.length; i++) {
			E e = elements[i];
			list.add(e);			
		}
		
		return list;
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


	public void stop() {
		clock.kill();
		ac.stop();
	}

	public static float random(double x) {
		return (float)(Math.random() * x);
	}


}
