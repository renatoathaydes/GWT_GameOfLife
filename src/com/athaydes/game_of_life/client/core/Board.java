package com.athaydes.game_of_life.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.athaydes.game_of_life.client.util.Point;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class Board extends Composite {

	private static final int ROWS = 100;
	private static final int COLS = 100;

	private long refreshDelay = 500; // ms
	
	// this cell is kept to be returned when a non-visible cell is required
	private final Cell INVISIBLE = new Cell(-1, -1);
	
	private ClickHandler cellClickHandler = new CellClickHandler();

	private boolean running = false;

	private Cell[][] cells = new Cell[ROWS][COLS];
	private List<Point> livePoints = new ArrayList<Point>();
	
	private FlowPanel container;
	
	public Board() {
		container = new FlowPanel();
		initWidget(container);
	}
	
	/**
	 * Call only after the Board is attached to the DOM.
	 */
	public void populateBoard() {
		for (int row = 0; row < ROWS; row++) {
			FlowPanel rowPanel = new FlowPanel();
			rowPanel.setStyleName("no-top-space");
			container.add(rowPanel);
			for (int col = 0; col < COLS; col++) {
				cells[row][col] = new Cell(row, col);
				cells[row][col].addClickHandler(cellClickHandler);
				rowPanel.add(cells[row][col]);
			}
		}
		setContainerWidth();
	}
	
	private void setContainerWidth() {
		int cellWidth = cells[0][0].getOffsetWidth();
		container.setWidth(cellWidth * COLS + "px");
	}

	public boolean isRunning() {
		return running;
	}
	
	public void run() {
		running = true;
		long startT = System.currentTimeMillis();
		updateBoard();
		long duration = System.currentTimeMillis() - startT;
		GWT.log("Board update duration was: " + duration);
		new Timer() {

			@Override
			public void run() {
				if (running) {
					Board.this.run();
				}
			}

		}.schedule((int) Math.max(5, refreshDelay - duration));
	}

	public void stop() {
		running = false;
	}

	/**
	 * Rules of the game:
	 * - Any live cell with fewer than two live neighbours dies, as if caused by under-population.
	 * - Any live cell with two or three live neighbours lives on to the next generation.
	 * - Any live cell with more than three live neighbours dies, as if by overcrowding.
	 * - Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	 * @author Renato
	 *
	 */
	private void updateBoard() {
		List<Cell> cellsToUpdate = new ArrayList<Cell>();
		
		Set<Point> toInspect = getPointsToInspect();
		for (Point p : toInspect) {
			int x = p.getX();
			int y = p.getY();
			Cell cell = cell(x, y);
			int liveNbs = 0;
			for (int nr = -1; nr < 2; nr++) {
				for (int nc = -1; nc < 2; nc++) {
					if (nr == 0 && nc == 0) {
						continue;
					}
					if (cell(x + nr, y + nc).isAlive()) {
						liveNbs++;
					}
				}
				
			}
			boolean aliveNext = cell.isAlive() ?
					(liveNbs == 2 || liveNbs == 3) :
						(liveNbs == 3);
			if (cell.isAlive() != aliveNext) {
				cellsToUpdate.add(cell);
			}
			
		}
		
		for (Cell cell : cellsToUpdate) {
			cell.setAlive(!cell.isAlive());
		}
		
		for (Point p : toInspect) {
			Cell cell = cell(p.getX(), p.getY());
			if (cell.isAlive()) {
				livePoints.add(p);
			}
		}
		
	}
	
	private Set<Point> getPointsToInspect() {
		Set<Point> result = new HashSet<Point>();
		for (Point p : livePoints) {
			for (int row = p.getX() - 1; row <= p.getX() + 1; row++) {
				for (int col = p.getY() - 1; col <= p.getY() + 1; col++) {
					result.add(new Point(row, col));
				}
			}
		}
		return result;
	}
	
	private Cell cell(int row, int col) {
		if (row < 0 || col < 0 || row >= ROWS || col >= COLS) {
			return INVISIBLE;
		}
		return cells[row][col];
	}
	
	
	private class CellClickHandler implements ClickHandler {
		
		@Override
		public void onClick(ClickEvent event) {
			Cell cell = (Cell) event.getSource();
			cell.setAlive(!cell.isAlive());
			if (cell.isAlive()) {
				livePoints.add(cell.getLocation());
			}
			GWT.log(livePoints.toString());
		}
		
	}

	public void setCellPattern(CellPattern cellPattern) {
		for (Point p : livePoints) {
			cell(p.getX(), p.getY()).setAlive(false);
		}
		livePoints = new ArrayList<Point>();
		for (Point p : cellPattern.getLivePoints()) {
			cell(p.getX(), p.getY()).setAlive(true);
			livePoints.add(p);
		}
	}

}
