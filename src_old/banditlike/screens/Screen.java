package banditlike.screens;

import java.awt.event.KeyEvent;
import javax.swing.Action;
import asciiPanel.AsciiPanel;

public interface Screen {
	
	public void displayOutput(AsciiPanel terminal);
	
//	public Screen respondToUserInput(KeyEvent key);
//	
	public Screen respondToUserInput(AsciiPanel terminal);
	
	public Action getEnterAction();
	

}
