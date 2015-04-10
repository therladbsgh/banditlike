package banditlike;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/*  File: KeyBindingExample.java
 *  
 *  Description: This class was created to provide a simple example of the
 *               application of KeyBinding to a Swing component. In this
 *               example, a JTextField and JButton are displayed on a JPanel in
 *               a JFrame. When the the focus is on the JTextField and an Enter
 *               key is pressed, the JButton action will occur as though the
 *               button had been pressed.
 *               
 *               The behavior shown in this simple example:
 *               1.  When the window first appears, the text field has focus.
 *                   Any keys typed will be entered as data in the text field;
 *               2.  Pressing Enter while the text field has focus will cause
 *                   the data typed to be selected so that any additional typing
 *                   will replace the existing. this behavior is useful for
 *                   multiple entries; and 
 *               3.  If the user presses the Enter button, focus is immediately
 *                   transferred back to the text field and the data in the 
 *                   field is selected.  
 *
 *  Source: Java Tutorials and Swing component APIs as needed. 
 *
 *  Author: GregBrannon, August 2011
 */
public class KeyBindingExample
{
    private static JFrame mainFrame;
    private static JTextField dataField;
    private static JButton enterButton;
    private static JPanel mainPanel;
    private static Action enterAction;
    private static ButtonListener buttonListener;
    
    // the main() method creates a simple JFrame to demonstrate the
    // key binding of the enter key to the component button "enter" 
    public static void main( String[] args )
    {
        mainFrame = new JFrame( "Key Binding Example" );

        mainFrame.add( makePanel() );
        mainFrame.setLocationRelativeTo( null );
        mainFrame.setSize( 200, 100 );
        mainFrame.setResizable( false );
        mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        mainFrame.setVisible( true );

    } // end method main()
    
    static JPanel makePanel()
    {
        // declares the components used to create the JFrame's content and
        // the actions that will occur when the enter button is selected
        mainPanel = new JPanel();
        buttonListener = new ButtonListener();
        dataField = new JTextField( 15 );
        enterButton = new JButton( "Enter" );
        enterButton.addActionListener( buttonListener );
        
        // defines an AbstractAction item that will program the action to occur
        // when the enter key is pressed
        enterAction = new EnterAction();

        // the following two lines do the magic of key binding. the first line
        // gets the dataField's InputMap and pairs the "ENTER" key to the
        // action "doEnterAction" . . .
        dataField.getInputMap().put( KeyStroke.getKeyStroke( "ENTER" ),
                "doEnterAction" );

        // . . . then this line pairs the AbstractAction enterAction to the
        // action "doEnterAction"
        dataField.getActionMap().put( "doEnterAction", enterAction );

        // the following commented line 'seems' to have the same affect as the
        // two previous lines. this may be an acceptable approach when only a  
        // single action is required.
        // dataField.setAction( enterAction );
        
        // add the components to the JPanel and return the completed product
        mainPanel.add( dataField );
        mainPanel.add( enterButton );
        
        return mainPanel;
    }

    // class EnterAction is an AbstractAction that defines what will occur
    // when the enter key is pressed. 
    static class EnterAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            // provides feedback to the console to show that the enter key has
            // been pressed
            System.out.println( "The Enter key has been pressed." );
            
            // pressing the enter key then 'presses' the enter button by calling
            // the button's doClick() method
            enterButton.doClick();
            
        } // end method actionPerformed()
        
    } // end class EnterAction
    
    // class ButtonListener defines the action to occur when the enter button
    // is pressed
    static class ButtonListener implements ActionListener
    {
        public void actionPerformed( ActionEvent bp )
        {
            // provides feedback to the console to show that the enter button
            // was pressed
            System.out.println( "The enter button was pressed." );

            // focus must be returned to the text field in order for the
            // selectAll() method to work.
            dataField.requestFocusInWindow();
            dataField.selectAll();
            
        } // end method actionPerformed()
        
    } // end class ButtonListener

} // end class KeyBindingExample

