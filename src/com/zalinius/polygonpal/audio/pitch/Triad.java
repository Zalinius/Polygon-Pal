package com.zalinius.polygonpal.audio.pitch;

public interface Triad extends Iterable<RelativePitch>{
	public RelativePitch first();
	public RelativePitch second();
	public RelativePitch third();
}
