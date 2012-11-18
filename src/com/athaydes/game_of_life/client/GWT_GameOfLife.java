package com.athaydes.game_of_life.client;

import java.util.Arrays;

import com.athaydes.game_of_life.client.core.Board;
import com.athaydes.game_of_life.client.core.CellPattern;
import com.athaydes.game_of_life.client.util.Point;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Rules of the game:
 * - Any live cell with fewer than two live neighbours dies, as if caused by under-population.
 * - Any live cell with two or three live neighbours lives on to the next generation.
 * - Any live cell with more than three live neighbours dies, as if by overcrowding.
 * - Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 * @author Renato
 *
 */
public class GWT_GameOfLife implements EntryPoint {

	public static final String BACK_COLOR = "black";
	
	private Board board;
	
	public void onModuleLoad() {
		GWT.log("Loading game of life");
		VerticalPanel container = new VerticalPanel();
		container.setSpacing(5);
		container.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		RootPanel.get("contents").add(container);
		
		container.add(createButtons());
		
		board = new Board();
		container.add(board);
		
		GWT.log("Populating board");
		board.populateBoard();
		
		GWT.log("READY");
		
	}
	
	private Widget createButtons() {
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setSpacing(10);
		
		final String startLabel = "Start";
		final String stopLabel = "Stop";
		final Button startStopButton = new Button(startLabel);
		
		startStopButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (startStopButton.getText().equals(startLabel)) {
					board.run();
					startStopButton.setText(stopLabel);
				} else {
					board.stop();
					startStopButton.setText(startLabel);
				}
			}
			
		});
		
		Button setPatternButton = new Button("Set pattern", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//[INFO] [gwt_gameoflife] - [(10, 11), (11, 11), (12, 11), (12, 10), (11, 9)]
				board.setCellPattern(new CellPattern(Arrays.asList(
						new Point(10, 11),
						new Point(11, 11),
						new Point(12, 11),
						new Point(12, 10),
						new Point(11, 9)
				)));
			}
		});
		
		buttonsPanel.add(startStopButton);
		buttonsPanel.add(setPatternButton);
		
		return buttonsPanel;
	}

}
