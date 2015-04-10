package banditlike;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import banditlike.screens.Screen;
import banditlike.screens.StartScreen;

public class ApplicationMain extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1060623638149583738L;
	
	private AsciiPanel terminal;
	private Screen screen;
	
	public ApplicationMain(){
		super("Banditlike");
		terminal = new AsciiPanel(100,24);
		add(terminal);
		pack();
		screen = new StartScreen();
		addKeyListener(this);
		repaint();
	}
	
	@Override
	public void repaint(){
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		screen = screen.respondToUserInput(e);
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) { 
		
	}
	
	public static void main(String[] args) {
		ApplicationMain app = new ApplicationMain();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}
}
