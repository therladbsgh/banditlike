package banditlike;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import banditlike.screens.Screen;
import banditlike.screens.StartScreen;

/**
 * The starting point of the program.
 * @author Andrew Kim
 */

public class ApplicationMain extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1060623638149583738L;
	
	/**
	 * The terminal to show the screen. As AsciiPanel is used, only ASCII characters will show.
	 * This is the only GUI component in the program.
	 */
	private AsciiPanel terminal;
	
	/**
	 * The various screens to display.
	 * Note the screens described here are not GUI components, but merely different "modes" of what to display.
	 */
	private Screen screen;
	
	/**
	 * The game width and height.
	 */
	public final int GAMEWIDTH = 120;
	public final int GAMEHEIGHT = 43;
	
	/**
	 * Class constructor.
	 * Creates a terminal, and adds a key listener.
	 * Size is set to GAMEWIDTH and GAMEHEIGHT.
	 */
	public ApplicationMain(){
		super("Banditlike");
		terminal = new AsciiPanel(GAMEWIDTH, GAMEHEIGHT);
		add(terminal);
		pack();
		screen = new StartScreen();
		addKeyListener(this);
		repaint();
	}
	
	/**
	 * Updates the terminal.
	 */
	@Override
	public void repaint(){
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}

	/**
	 * Responds whenever the key is pressed.
	 * The current screen should respond to the key input and update accordingly.
	 * @param e The key pressed.
	 */
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
	
	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		ApplicationMain app = new ApplicationMain();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}
}
