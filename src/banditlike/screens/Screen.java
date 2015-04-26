package banditlike.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

/**
 * The screen interface to be implemented in all screens.
 * All screens should be able to respond to user input and display an output.
 * @author Andrew Kim
 */

public interface Screen {
	
	public void displayOutput(AsciiPanel terminal);
	
	public Screen respondToUserInput(KeyEvent key);
	
}
