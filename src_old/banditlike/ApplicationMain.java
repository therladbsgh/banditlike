package banditlike;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.KeyStroke;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import banditlike.screens.Screen;
import banditlike.screens.StartScreen;

public class ApplicationMain extends JFrame /* implements KeyListener */ {
	
	private static final long serialVersionUID = 1060623638149583738L;
	
	private AsciiPanel terminal;
	private Screen screen;
	
	public ApplicationMain(){
		super("Banditlike");
		terminal = new AsciiPanel(100,32);
		add(terminal);
		pack();
		screen = new StartScreen();
		//addKeyListener(this);
		repaint();
		setKeyBindings();
	}
	
	@Override
	public void repaint(){
		terminal.clear();
		screen = screen.respondToUserInput(terminal);
		screen.displayOutput(terminal);
		super.repaint();
	}
	
	public void setKeyBindings(){
		terminal.getInputMap().put(KeyStroke.getKeyStroke("UP"),"enterAction");
		terminal.getActionMap().put("enterAction", screen.getEnterAction());
	}
	
//	public void keyPressed(KeyEvent e){
//		screen = screen.respondToUserInput(e);
//	}
//	
//	public void keyReleased(KeyEvent e){
//		
//	}
//	
//	public void keyTyped(KeyEvent e){
//		
//	}
	
	public static void main(String[] args){
		ApplicationMain app = new ApplicationMain();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}

}
