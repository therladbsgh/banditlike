package banditlike;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
	FLOOR((char)250, AsciiPanel.yellow),
	WALL((char)177, AsciiPanel.yellow),
	BOUNDS('x', AsciiPanel.brightBlack);
	
	private char glyph;
	private Color color;
	
	public char getGlyph(){ return glyph; }
	public Color getColor(){ return color; }
	
	Tile(char glyph, Color color){
		this.glyph = glyph;
		this.color = color;
	}
	
}
