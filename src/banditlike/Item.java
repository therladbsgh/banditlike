package banditlike;

import java.awt.Color;

/**
 * Basic properties of an item.
 * Should be instantiated in the ItemFactory / StuffFactory.
 * @author Andrew Kim
 */

public class Item {
	
	private char glyph;
	private Color color;
	private String name;
	
	/**
	 * Returns glyph of the item.
	 * @return glyph
	 */
	public char glyph() { return glyph; }
	
	/**
	 * Returns color of the item.
	 * @return color
	 */
	public Color color() { return color; }
	
	/**
	 * Returns name of the item.
	 * @return name
	 */
	public String name() { return name; }
	
	/**
	 * Coass constructor.
	 * @param glyph
	 * @param color
	 * @param name
	 */
	public Item(char glyph, Color color, String name){
		this.glyph = glyph;
		this.color = color;
		this.name = name;
	}

}
