package com.athaydes.game_of_life.client;

import java.util.Collection;

import java.util.HashSet;

public class CellPattern {
	
	private Collection<Point> livePoints = new HashSet<Point>();
	
	public CellPattern(Collection<Point> points) {
		for (Point p : points) {
			livePoints.add(p);
		}
	}
	
	public Collection<Point> getLivePoints() {
		return livePoints;
	}
	
	public Collection<Point> getLivePoints(int offsetX, int offsetY) {
		Collection<Point> points = new HashSet<Point>(livePoints.size());
		for (Point p : livePoints) {
			points.add(new Point(p.getX() + offsetX, p.getY() + offsetY));
		}
		return points;
	}

}
