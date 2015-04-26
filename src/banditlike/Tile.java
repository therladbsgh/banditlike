package banditlike;

import java.awt.Color;
import asciiPanel.AsciiPanel;

/**
 * The properties of a tile.
 * @author Andrew Kim
 */

public enum Tile {
	FLOOR((char)250, AsciiPanel.yellow),
	WALL((char)177, AsciiPanel.yellow),
	BOUNDS('x', AsciiPanel.brightBlack),
	STAIRS_DOWN('>', AsciiPanel.white),
	STAIRS_UP('<', AsciiPanel.white),
	UNKNOWN(' ', AsciiPanel.white);
	
	private char glyph;
	private Color color;
	
	/**
	 * @return glyph
	 */
	public char glyph(){ return glyph; }
	
	/**
	 * @return color
	 */
	public Color color(){ return color; }
	
	
	/**
	 * Enum constructor.
	 * @param glyph
	 * @param color
	 */
	Tile(char glyph, Color color){
		this.glyph = glyph;
		this.color = color;
	}
	
	/**
	 * Checks if the tile is diggable.
	 * @return
	 */
	public boolean isDiggable(){
		return this == Tile.WALL;
	}
	
	/**
	 * Checks if the tile is a ground.
	 * @return
	 */
	public boolean isGround(){
		return this != WALL && this != BOUNDS;
	}
	
}
