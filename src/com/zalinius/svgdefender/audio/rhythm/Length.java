package com.zalinius.svgdefender.audio.rhythm;

public class Length {
	
	private final double length; //in beats
	
	private Length(double length) {
		this.length = length;
	}
	public Length(Length ... lengths) {
		double totalLength = 0;
		for (int i = 0; i < lengths.length; i++) {
			totalLength += lengths.length;
		}
		
		this.length = totalLength;
	}
	
	private static final Length WHOLE = new Length(4.0);
	private static final Length HALF = new Length(2.0);
	private static final Length QUARTER = new Length(1.0);
	private static final Length EIGHTH = new Length(1.0/2.0);
	private static final Length SIXTEENTH = new Length(1.0/4.0);
	

}
