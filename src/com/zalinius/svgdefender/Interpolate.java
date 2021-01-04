package com.zalinius.svgdefender;

public class Interpolate {
	
	public static double linearInterpolant(double u, double v, double x) {
		return (x-u)/(v-u);
	}
	
	public static double linear(double a, double b, double t) {
		return (b - a)*t + a;
	}
	
	/*
	 * Maps a value x from the interval [u,v] to the interval [a,b], in a linear fashion
	 */
	public static double linearMapping(double u, double v, double x, double a, double b) {
		return linear(a, b, linearInterpolant(u, v, x));
	}

	public static double cosine(double a, double b, double t) {
		return (a-b)/2.0 * Math.cos(Math.PI * t) + (a+b)/2.0;
	}

}
