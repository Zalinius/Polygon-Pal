package com.zalinius.svgdefender.audio;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.zalinius.svgdefender.audio.pitch.IndexList;
import com.zalinius.svgdefender.audio.pitch.Melody;
import com.zalinius.svgdefender.audio.pitch.Note;
import com.zalinius.svgdefender.audio.pitch.PitchPlus;
import com.zalinius.svgdefender.audio.pitch.RandomIndexStrategyFactory;
import com.zalinius.svgdefender.audio.pitch.RandomNotes;
import com.zalinius.svgdefender.audio.pitch.RelativeNote;
import com.zalinius.svgdefender.audio.pitch.ScaleFactory;
import com.zalinius.svgdefender.audio.synths.SoundFactory;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Clip;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.NBitsConverter;
import net.beadsproject.beads.ugens.Reverb;
import net.beadsproject.beads.ugens.Static;

public class TestTrack {


//	public static void main(String[] args) throws InterruptedException {
//		TestTrack track = new TestTrack(aContext());
//		track.play();
//		rawr = true;
//		int count = 0;
//		while(count < 10) {
//			Thread.sleep(40000);
//			rawr = !rawr;
//			count ++;
//		}
//		track.stop();
//	}

	public static AudioContext aContext() {
		AudioContext ac;
		JavaSoundAudioIO jsaIO = new JavaSoundAudioIO();
		jsaIO.selectMixer(2);
		ac = new AudioContext(jsaIO);
				
		return ac;
	}

	private final AudioContext ac;
	private final Clock clock;
	private final SoundFactory sf;
	private RandomNotes notes;
	private Melody bassMelody;
	
	private GameToMusicAdaptor musicInfo;

	public TestTrack(AudioContext ac, GameToMusicAdaptor musicInfo) {
		this.ac = ac;
		AudioContext.setDefaultContext(ac);
		this.musicInfo = musicInfo;
		clock = new Clock(ac, musicInfo.beatLength());

		sf = new SoundFactory();
		List<RelativeNote> triadNotes = ScaleFactory.arrayToList(PitchPlus.pentatonic).stream().map(i -> new RelativeNote(i)).collect(Collectors.toList());
		notes = new RandomNotes(triadNotes, Note.MIDDLE_C.midiNote(), RandomIndexStrategyFactory.periodIncreasing(3, triadNotes.size(), true));
		bassMelody = createBassMelody();
		
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

		return new Melody(new Note(Note.MIDDLE_C.midiNote() - 3*Note.OCTAVE_LENGTH), ScaleFactory.majorScale(), new IndexList(indices));
	}

	public void play() {
		clock.addMessageListener(trackBead());
		ac.out.addDependent(clock);
		ac.start();
	}

	public Bead trackBead() {
		return new Bead() {
			//this is the method that we override to make the Bead do something

			public void messageReceived(Bead message) {
				Clock c = (Clock)message;
				c.setIntervalEnvelope(new Static(musicInfo.beatLength()));
				if(c.getCount() % 16 == 8) {

					UGen bass = sf.bass(bassMelody.next().midiNote());
					ac.out.addInput(bass);
				}
				if(useLightPercussion(c.getCount(), musicInfo.lightPercussionActivity())) {
				//if(c.getCount() % 16 == 8 || c.getBeatCount() % 16 == 0 && c.getCount() % 4 == 2) {
					UGen perc = sf.lightPercussion(50f);
					ac.out.addInput(perc);
				}
				if(c.getCount() % 16 == 0 && musicInfo.baseDrumActive()) {
					UGen perc = sf.heavyPercussion(100f, musicInfo.baseDrumIntensity());
					ac.out.addInput(perc);
				}
				Buffer buffer = musicInfo.themeSynth();

				if(c.isBeat() && (c.getBeatCount() % 4 != 3 || c.getBeatCount() % 16 == 15)) {
					int note =-(c.getBeatCount() / 8) * 6;
					note = 0;
					switch (c.getBeatCount() % 8) {
					case 0:
						note += 70;
						break;
					case 1:
						note += 65;
						break;
					case 2:
						note += 63;
						break;
					case 3:
						note += 61;
						break;
					case 4:
						note += 70;
						break;
					case 5:
						note += 65;
						break;
					case 6:
						note += 73;
						break;
					case 7:
						note += 70;
						break;

					default:
						break;
					}
					Note nextNote = notes.next();
				//	nextNote = new Note(40);
					UGen notePlayed = sf.mainNote(note, buffer);
					UGen notePlayed2 = sf.mainNote(nextNote.midiNote()-12, buffer);
					UGen notePlayed3 = sf.mainNote(nextNote.midiNote(), buffer);

				    //ac.out.addInput(notePlayed);
				//	ac.out.addInput(notePlayed2);
					
					Reverb ugen = new Reverb(ac);
					ugen.setSize(1);
					ugen.addInput(notePlayed3);
					//ac.out.addInput(ugen);
										
				}
//				
//				if(c.getCount() % 4 == 2) {
//					if(random(1, 4)) {
//						int extra1 = coinFlip()? 48:72;						
//						int extra2 = coinFlip()? 48:72;						
//						int extra3 = coinFlip()? 48:72;						
//						
//						UGen note1 = sf.mainNote(PitchPlus.majorTriad[0] + extra1);
//						UGen note2 = sf.mainNote(PitchPlus.majorTriad[1] + extra2);
//						UGen note3 = sf.mainNote(PitchPlus.majorTriad[2] + extra3);
//						ac.out.addInput(note1);
//						ac.out.addInput(note2);
//						ac.out.addInput(note3);
//
//					}
//					else {
//						UGen note = sf.mainNote(notes.next().midiNote());
//						ac.out.addInput(note);
//					}
//				}
			}
		};

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

	public void stop() {
		clock.kill();
		ac.stop();
	}

	public static float random(double x) {
		return (float)(Math.random() * x);
	}
	
	public static boolean coinFlip() {
		return Math.random() < .5d;
	}
	public static boolean random(int chance, int denominator) {
		return (chance-1) >= (new Random().nextInt(denominator));
	}
}
