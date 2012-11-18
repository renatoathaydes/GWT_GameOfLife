package com.athaydes.game_of_life.client.core;

import com.athaydes.game_of_life.client.GWT_GameOfLife;
import com.athaydes.game_of_life.client.util.Point;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * Representation of a single cell in the game of life.
 * @author Renato
 *
 */
public class Cell extends Widget implements HasClickHandlers {

	private boolean alive = false;
	private String color = defaultColor;
	
	private static final String defaultColor = "blue";
	private static final String defaultBorderColor = "yellow";
	
	private final Point location;
	
	
	public Cell(int row, int col) {
		location = new Point(row, col);
		setElement(Document.get().createDivElement());
		sinkEvents(Event.ONCLICK);
		setStyleName("cell");
		updateColor();
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
		updateColor();
	}
	
	public void setColor(String color) {
		this.color = color;
		updateColor();
	}
	
	public void showBorder(boolean show) {
		if (show) {
			getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			getElement().getStyle().setBorderColor(defaultBorderColor);	
		} else {
			getElement().getStyle().setBorderStyle(BorderStyle.NONE);
		}
	}
	
	private void updateColor() {
		String colorToSet = alive ? color : GWT_GameOfLife.BACK_COLOR;
		getElement().getStyle().setBackgroundColor(colorToSet);
	}
	
	public Point getLocation() {
		return location;
	}
	
}
