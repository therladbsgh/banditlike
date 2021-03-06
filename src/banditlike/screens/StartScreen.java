package banditlike.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

/**
 * The starting screen, when the program begins.
 * @author Andrew Kim
 */

public class StartScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.write("Banditlike", 1, 1);
		terminal.writeCenter("-- press [enter] to start --", 22);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
	}
}
