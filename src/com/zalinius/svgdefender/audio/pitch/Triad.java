package com.zalinius.svgdefender.audio.pitch;

public interface Triad extends Iterable<RelativePitch>{
	public RelativePitch first();
	public RelativePitch second();
	public RelativePitch third();
}
