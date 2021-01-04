package com.zalinius.svgdefender.audio.pitch;

public interface Triad extends Iterable<RelativeNote>{
	public RelativeNote first();
	public RelativeNote second();
	public RelativeNote third();
}
