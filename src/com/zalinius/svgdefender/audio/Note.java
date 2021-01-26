package com.zalinius.svgdefender.audio;

import com.zalinius.svgdefender.audio.pitch.AbsolutePitch;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class Note {
	private AbsolutePitch pitch;
	private Gain gainEnvelope;
	
	public Note(AbsolutePitch pitch, double attack, double sustain, double release) {
		this.pitch = pitch;
		gainEnvelope = new Gain(1, new Envelope(0));
		((Envelope)gainEnvelope.getGainUGen()).addSegment(1.0f, (float) attack);
		((Envelope)gainEnvelope.getGainUGen()).addSegment(1.0f, (float) sustain);
		((Envelope)gainEnvelope.getGainUGen()).addSegment(0.0f, (float) release, new KillTrigger(gainEnvelope));
	}
	
	public UGen getPlayableNote(Buffer synth) {
		gainEnvelope.addInput(new WavePlayer(pitch.frequency(), synth));
		return gainEnvelope;
	}

}
