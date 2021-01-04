package com.zalinius.svgdefender.audio;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
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

	public final int BEAT_LENGTH = 500;
	private int currentBeatLength;

	private final AudioContext ac;
	private final Clock clock;
	private final SoundFactory sf;
	private RandomNotes notes;
	private Deque<Buffer> sounds;
	private Deque<String> names;
	
	private GameToMusicAdaptor musicInfo;

	public TestTrack(AudioContext ac, GameToMusicAdaptor musicInfo) {
		this.ac = ac;
		AudioContext.setDefaultContext(ac);
		clock = new Clock(ac, BEAT_LENGTH);
		this.musicInfo = musicInfo;

		sf = new SoundFactory();
		List<RelativeNote> triadNotes = ScaleFactory.arrayToList(PitchPlus.pentatonic).stream().map(i -> new RelativeNote(i)).collect(Collectors.toList());
		notes = new RandomNotes(triadNotes, Note.MIDDLE_C.midiNote(), RandomIndexStrategyFactory.periodIncreasing(3, triadNotes.size(), true));
		
		sounds = new ArrayDeque<>();

		currentBeatLength = BEAT_LENGTH;
//		sounds.add(SoundFactory.WIND_SOFT);
//		sounds.add(SoundFactory.WIND_GLASS);
//		
//		sounds.add(SoundFactory.RESONANT);
//		sounds.add(SoundFactory.RESONANT_SHARP);
//		sounds.add(SoundFactory.RESONANT_STRONG);
//		
//		sounds.add(SoundFactory.STRING_SOFT);
//		sounds.add(SoundFactory.STRING_STRONG);
		sounds.add(SoundFactory.STRING_SHARP);
//		
//		sounds.add(SoundFactory.SAW_SOFT);
//		sounds.add(SoundFactory.SAW_STRONG);
//		sounds.add(SoundFactory.SAW_SHARP);
		
		names = new ArrayDeque<>();
		names.add("Soft Wind");
		names.add("Glassy Wind");
		names.add("Resonant");
		names.add("Sharp Resonance");
		names.add("Strong Resonance");
		names.add("Soft Strings");
		names.add("Strong Strings");
		names.add("Sharp Strings");
		names.add("Soft Saw");
		names.add("Strong Saw");
		names.add("Sharp Saw");
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

				//	System.out.println(c.getCount() + " " + c.getBeatCount() + " " + c.isBeat());
				//
				//				        if(c.isBeat()) {
				//				          //choose some nice frequencies
				//				          if(random(1) < 0.5) return;
				//				          start = (int)random(3);
				//				          
				////				          switch (start) {
				////						case 0:
				////							pitch = 0;
				////							break;
				////						case 1:
				////							pitch = 7;
				////							break;
				////						case 2:
				////							pitch = 9;
				////							break;
				////						default:
				////							break;
				////						}
				//				          int pitch1 = pitch + 0;
				//				          int pitch2 = pitch + 4;
				//				          int pitch3 = pitch + 7;
				//				          float freq1 = Pitch.mtof(pitch1 + ((int)random(2) + 2) * 12 + 32);
				//				          float freq2 = Pitch.mtof(pitch2 + ((int)random(2) + 2) * 12 + 32);
				//				          float freq3 = Pitch.mtof(pitch3 + ((int)random(2) + 2) * 12 + 32);
				//				    
				//				          WavePlayer wp1 = new WavePlayer(ac, freq1, Buffer.TRIANGLE);
				//				          WavePlayer wp2 = new WavePlayer(ac, freq2, Buffer.TRIANGLE);
				//				          WavePlayer wp3 = new WavePlayer(ac, freq3, Buffer.TRIANGLE);
				//				          Gain g = new Gain(ac, 3, new Envelope(ac, 0));
				//				          g.addInput(wp1);
				//				          g.addInput(wp2);
				//				          g.addInput(wp3);
				//				        //  ac.out.addInput(g);
				//				          ((Envelope)g.getGainUGen()).addSegment(0.1f, random(50));
				//				          ((Envelope)g.getGainUGen()).addSegment(0, random(1000), new KillTrigger(g));
				//				       }
				//
//				if(c.getCount() % 8 == 0) {
//
//					UGen bass = sf.bass(32);
//					ac.out.addInput(bass);
//				}
				if(c.getCount() % 16 == 8 || c.getBeatCount() % 16 == 15 && c.getCount() % 4 == 2) {
					UGen perc = sf.lightPercussion(50f);
					ac.out.addInput(perc);
				}
				if(c.getCount() % 16 == 0 && musicInfo.baseDrumActive()) {
					System.out.println("BASS: " + musicInfo.baseDrumIntensity());
					UGen perc = sf.heavyPercussion(100f, musicInfo.baseDrumIntensity());
					ac.out.addInput(perc);
				}
				final Buffer buffer = sounds.peek();

				if(c.isBeat() && (c.getBeatCount() % 4 != 3 || c.getBeatCount() % 16 == 15)) {
					System.out.println(names.peek());
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
						sounds.addLast(sounds.removeFirst());
						names.addLast(names.removeFirst());
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
					ac.out.addInput(notePlayed3);
										
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
