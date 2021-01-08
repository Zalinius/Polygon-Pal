package com.zalinius.svgdefender.audio.pitch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomIndexStrategyFactory {

	public static abstract class AbstractRandomRandomIndexStrategy implements RandomIndexStrategy{
		protected Random rand;
		public AbstractRandomRandomIndexStrategy() {
			rand = new Random();
		}
	}
	
	public static RandomIndexStrategy random() {
		return new AbstractRandomRandomIndexStrategy() {
			
			@Override
			public int nextIndex(int bound) {
				return rand.nextInt(bound);
			}
		};
	}
	
	public static RandomIndexStrategy noConsecutiveRepetition() {
		return new AbstractRandomRandomIndexStrategy() {
			private int lastIndex = -1;
			@Override
			public int nextIndex(int bound) {
				int newIndex;
				if(lastIndex == -1) {
					newIndex = rand.nextInt(bound);
				}
				else {
					newIndex = rand.nextInt(bound - 1);
					if(newIndex >= lastIndex) {
						newIndex += 1;
					}
				}
				
				lastIndex = newIndex;
				return newIndex;
			}
		};
	}
	
	
	public static RandomIndexStrategy increasing(final int maxJump) {
		return new AbstractRandomRandomIndexStrategy() {
			private int lastIndex = -1;
			@Override
			public int nextIndex(int bound) {
				int newIndex;
				if(lastIndex == -1) {
					newIndex = 0;
				}
				else {
					newIndex = lastIndex + (rand.nextInt(maxJump) + 1);
					if(newIndex >= bound) {
						newIndex = newIndex % bound;
					}
				}
				
				lastIndex = newIndex;
				return newIndex;
			}
		};
	}
	
	public static RandomIndexStrategy decreasing(final int maxJump) {
		return new AbstractRandomRandomIndexStrategy() {
			private int lastIndex = -1;
			@Override
			public int nextIndex(int bound) {
				int newIndex;
				if(lastIndex == -1) {
					newIndex = bound - 1;
				}
				else {
					newIndex = lastIndex - (rand.nextInt(maxJump) + 1);
					if(newIndex < 0) {
						newIndex = Math.floorMod(newIndex, bound);
					}
				}
				
				lastIndex = newIndex;
				return newIndex;
			}
		};
	}
	
	/**
	 * @param period
	 * @param bound
	 * @return
	 */
	public static RandomIndexStrategy periodIncreasing(final int period, final int bound, final boolean increasing) {
		if(period > bound) {
			throw new RuntimeException("Period must be lesser or equal to bound: " + period + ", " + bound);
		}
		
		return new AbstractRandomRandomIndexStrategy() {
			private int countInPeriod = 0;
			private List<Integer> indices = new ArrayList<>();
			
			private void initializeIndices() {
				countInPeriod = 0;
				indices.clear();
				for (int i = 0; i < bound; i++) {
					indices.add(i);
				}
				for (int i = 0; i < bound-period; i++) {
					indices.remove(rand.nextInt(indices.size()));
				}
				if(!increasing) {
					Collections.reverse(indices);
				}
			}
			
			@Override
			public int nextIndex(int bound) {
				
				if(countInPeriod == period || indices.isEmpty()) {
					initializeIndices();
				}
				
				int resultPeriod = countInPeriod;
				countInPeriod += 1;
				return indices.get(resultPeriod);
				
			}
		};

	}
	
	
	
	public static void main(String[] args) {
		RandomIndexStrategy strat = periodIncreasing(3, 8, true);
		for(int i = 0; i != 20; ++i) {
			System.out.println(strat.nextIndex(8));
		}
	}
	
}
