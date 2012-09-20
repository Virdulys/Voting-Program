package voting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class VotingResults extends JFrame implements KeyListener {
    private boolean fullscreen = false;
    private VotingSettings parent = null;
    
    private ArrayList<Participant> participants;
    private VotingResultsPanel resultsPanel;
    private int standardFormWidth = 400;
    private int standardFormHeight = 280;
    
    public  VotingResults(VotingSettings parent, ArrayList<Participant> participants) {
        addKeyListener(this);
        setLayout(new BorderLayout());
        this.parent = parent;
        this.resultsPanel = new VotingResultsPanel(participants);
        add(resultsPanel, BorderLayout.CENTER);
        //pack();
        setSize(standardFormWidth, standardFormHeight);
        setLocationRelativeTo(null);
        validate();
    }
    
    public  VotingResults(VotingSettings parent, VotingResultsPanel resultsPanel, boolean fullscreen) {
        addKeyListener(this);
        setLayout(new BorderLayout());
        this.parent = parent;
        this.resultsPanel = resultsPanel;
        add(resultsPanel, BorderLayout.CENTER);
        //setUndecorated(fullscreen);
        toggleFullscreen(0, fullscreen);
        //pack();
        //add(resultsPanel);
        //setSize(400, 280);
        //setLocationRelativeTo(null);
        validate();
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }
    
    public void refreshResults() {
        resultsPanel.refreshResults();
    }

    public VotingResultsPanel getResultsPanel() {
        return resultsPanel;
    }

    public void setResultsPanel(VotingResultsPanel resultsPanel) {
        this.resultsPanel = resultsPanel;
    }
    
    public void toggleFullscreen( int screen, boolean fullscreen )
    {
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        if( screen > -1 && screen < gs.length )
        {
            setUndecorated(fullscreen);
            setResizable(!fullscreen);
            if (fullscreen)
                gs[screen].setFullScreenWindow( this );
            else {
              //We don't need to setFullScreenWindow when there is no need for full screen
                setSize(standardFormWidth, standardFormHeight); 
                setLocationRelativeTo(null); // return to center of the screen
            }
        }
        else if( gs.length > 0 )
        {
            setUndecorated(fullscreen);
            setResizable(!fullscreen);
            if (fullscreen)
                gs[screen].setFullScreenWindow( this );
            else {
              //We don't need to setFullScreenWindow when there is no need for full screen
                setSize(standardFormWidth, standardFormHeight); 
                setLocationRelativeTo(null); //Return to center of the screen
            }
        }
        else
        {
            throw new RuntimeException( "No Screens Found" );
        }
        //System.out.println(getWidth());
        validate();
        //System.out.println(getSize());
        //resultsPanel.refreshResults();
        //validate();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Nothing       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            //TODO call fullscreen method from here
            //showOnScreen(0, parent);
            // Call parent method to destroy me
            //manager.
            parent.toggleResultsFullscreen();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
        
    }
}
