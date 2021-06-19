package com.zalinius.polygonpal.audio.synths;

import com.zalinius.polygonpal.audio.pitch.AbsolutePitch;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.BufferFactory;
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
		g.addInput(new WavePlayer(new AbsolutePitch(27).frequency(), SoundFactory.RESONANT));
		g.addInput(new WavePlayer(new AbsolutePitch(28).frequency(), SoundFactory.RESONANT));
		g.addInput(new WavePlayer(new AbsolutePitch(29).frequency(), SoundFactory.RESONANT));
		((Envelope)g.getGainUGen()).addSegment(0f, (float)duration, new KillTrigger(g));
		return g;
	}
	
	public UGen bass(int midiNote, double intensity) {
		float freq = Pitch.mtof(midiNote);
		WavePlayer wp = new WavePlayer(freq, RESONANT);
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
	public static final Buffer WIND_SOFT = Buffer.SINE;
	public static final Buffer WIND_GLASS = makeBuffer(sinePower(3),"Glass");
	
	public static final Buffer SAW_SOFT = makeBuffer(absoluteSine(),"Soft Saw");
	public static final Buffer SAW_STRONG = makeBuffer(halfSquare(),"Half Saw");
	public static final Buffer SAW_SHARP = makeBuffer(halfSaw(),"Half Square");
	
	public static final Buffer RESONANT = makeBuffer(sinePower(2),"Resonant");
	public static final Buffer RESONANT_STRONG = makeBuffer(sinePower(20),"Resonant Ultra");
	public static final Buffer RESONANT_SHARP = Buffer.TRIANGLE;

	public static final Buffer STRING_SOFT = makeBuffer(normalizer(test()),"Soft String");
	public static final Buffer STRING_STRONG = makeBuffer(normalizer(sinEnvelope()),"Deep String");
	public static final Buffer STRING_SHARP = makeBuffer(squareSine(),"Sharp String");
	
	public static Buffer makeBuffer(WaveFunction function, String name) {
		BufferFactory factory = new BufferFactory() {
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public Buffer generateBuffer(int bufferSize) {
		    	Buffer b = new Buffer(bufferSize);
		        for(int i = 0; i < bufferSize; i++) {
		        	b.buf[i] = function.value(i, bufferSize);
		        }
		    	return b;
			}
		};
		
		return factory.getDefault();
	}

	
	private static WaveFunctionFractional sinEnvelope() {
		return new WaveFunctionFractional() {
			
			@Override
			public double value(double fraction) {
				return Math.sin(fraction/2)*Math.pow(Math.cos(2*fraction),7);
			}
		};
	}
	
	private static WaveFunctionFractional test() {
		return new WaveFunctionFractional() {
			
			@Override
			public double value(double fraction) {
				return Math.sin(fraction/2) * Math.sin(2*fraction);//TODO quite a nice sound: 
			}
		};
	}
	
	private static WaveFunction squareSine() {
		return new WaveFunction() {

			@Override
			public float value(int i, int max) {
				return (Buffer.SINE.getValueIndex(3*i) *Buffer.SQUARE.getValueIndex(i));
			}

		};
	}

	public static WaveFunction absoluteSine() {
		return new WaveFunction() {
			
			@Override
			public float value(int i, int max) {
				return (float)Math.sin(Math.PI * (double)i / (double)max);
			}
		};
	}

	public static WaveFunction halfSaw() {
		return new WaveFunction() {
			
			@Override
			public float value(int i, int max) {
				return Buffer.SAW.getValueIndex(i)/4;
			}
		};
	}
	public static WaveFunction halfSquare() {
		return new WaveFunction() {
			
			@Override
			public float value(int i, int max) {
				return Buffer.SQUARE.getValueIndex(i)/4;
			}
		};
	}
	public static WaveFunction sinePower(double power) {
		if(power < 1) {
			power = 1;
		}
		final double trueLevel=power;
		return new WaveFunction() {
			
			@Override
			public float value(int i, int max) {
				double sineValue = Buffer.SINE.getValueIndex(i);
				if(sineValue > 0d) {
					return (float) Math.pow(sineValue,trueLevel);
				}
				else {
					return (float) -Math.pow(Math.abs(sineValue),trueLevel);
				}
			}
		};
	}
	
	public static WaveFunction normalizer(WaveFunctionFractional other) {
		return new WaveFunction() {
			
			@Override
			public float value(int i, int max) {
				return (float) other.value(Math.PI * 2*(double) i / (double)max);
			}
		};
	}

}
