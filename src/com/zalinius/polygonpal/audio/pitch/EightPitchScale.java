package com.zalinius.polygonpal.audio.pitch;

public interface EightPitchScale extends Iterable<RelativePitch>{
	public RelativePitch first();
	public RelativePitch second();
	public RelativePitch third();
	public RelativePitch fourth();
	public RelativePitch fifth();
	public RelativePitch sixth();
	public RelativePitch seventh();
	public RelativePitch eighth();
	
	public RelativePitch nth(int index);
}
