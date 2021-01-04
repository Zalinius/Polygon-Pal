package com.zalinius.svgdefender.audio.pitch;

public interface EightNoteScale extends Iterable<RelativeNote>{
	public RelativeNote first();
	public RelativeNote second();
	public RelativeNote third();
	public RelativeNote fourth();
	public RelativeNote fifth();
	public RelativeNote sixth();
	public RelativeNote seventh();
	public RelativeNote eighth();
}
