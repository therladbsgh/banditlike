package banditlike.screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import asciiPanel.AsciiPanel;
import javax.swing.KeyStroke;


public class StartScreen implements Screen{
	
	private Screen screen = this;
	public Action enterAction = new EnterAction();
	
	@Override
	public void displayOutput(AsciiPanel terminal){
		terminal.write("Banditlike",1,1);
		terminal.writeCenter("-- press [enter] to start --", 22);
	}
	
//	@Override
//	public Screen respondToUserInput(KeyEvent key){
//		return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
//	}
	
	public Screen respondToUserInput(AsciiPanel terminal){
		System.out.println("LOL");
		return screen;
	}
	
	public Action getEnterAction(){
		return new EnterAction();
	}
	
	
	private class EnterAction extends AbstractAction {
		
        @Override
        public void actionPerformed(ActionEvent e){
        	screen = new PlayScreen();
        	System.out.println("UP pressed.");
        }
		
	}

}
