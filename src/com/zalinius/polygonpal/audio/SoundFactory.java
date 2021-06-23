package com.zalinius.polygonpal.audio;

import com.zalinius.zje.music.pitch.AbsolutePitch;
import com.zalinius.zje.music.synths.SynthFactory;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Noise;
import net.beadsproject.beads.ugens.WavePlayer;

public class SoundFactory {


	public UGen lightPercussion(double duration, double intensity) {
		Noise n = new Noise();
		Gain g = new Gain(1, new Envelope((float)intensity));
		g.addInput(n);
		((Envelope)g.getGainUGen()).addSegment(0f, (float)duration, new KillTrigger(g));
		
		return g;
	}
	
	public UGen heavyPercussion(double duration, double intensity) {
		Gain g = new Gain(1, new Envelope((float) (intensity/3))); //Divide by 3 because of the three notes
		g.addInput(new WavePlayer(new AbsolutePitch(27).frequency(), SynthFactory.RESONANT));
		g.addInput(new WavePlayer(new AbsolutePitch(28).frequency(), SynthFactory.RESONANT));
		g.addInput(new WavePlayer(new AbsolutePitch(29).frequency(), SynthFactory.RESONANT));
		((Envelope)g.getGainUGen()).addSegment(0f, (float)duration, new KillTrigger(g));
		return g;
	}
	
	public UGen bass(int midiNote, double intensity) {
		float freq = Pitch.mtof(midiNote);
		WavePlayer wp = new WavePlayer(freq, SynthFactory.RESONANT);
		Gain g = new Gain(1, new Envelope(0));
		g.addInput(wp);
		((Envelope)g.getGainUGen()).addSegment((float)intensity, 50);
		((Envelope)g.getGainUGen()).addSegment(0, 200, new KillTrigger(g));
		
		return g;
	}
	
	public UGen mainNote(int midiNote, Buffer buffer) {
		float freq = Pitch.mtof(midiNote);
		WavePlayer wp = new WavePlayer(freq, buffer);
		Gain g = new Gain(1, new Envelope(0));
		g.addInput(wp);
		
		((Envelope)g.getGainUGen()).addSegment(0.1f, 400);
		((Envelope)g.getGainUGen()).addSegment(0.1f, 700);
		((Envelope)g.getGainUGen()).addSegment(0, 1000, new KillTrigger(g));
		
		return g;
	}

}
