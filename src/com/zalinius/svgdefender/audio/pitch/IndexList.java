package com.zalinius.svgdefender.audio.pitch;

import java.util.List;

public class IndexList implements IndexStrategy {
	
	private List<Integer> indices;
	private int position;
	
	public IndexList(List<Integer> indices) {
		this.indices = indices;
		this.position = 0;
	}

	@Override
	public int nextIndex() {
		int indexValue = indices.get(position);
		
		position = (position + 1) % indices.size();
		
		return indexValue;
	}

}
