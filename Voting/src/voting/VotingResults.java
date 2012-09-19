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
    
    public  VotingResults(VotingSettings parent, ArrayList<Participant> participants) {
        addKeyListener(this);
        setLayout(new BorderLayout());
        this.parent = parent;
        this.resultsPanel = new VotingResultsPanel(participants, this);
        add(resultsPanel, BorderLayout.CENTER);
        //pack();
        setSize(400, 280);
        setLocationRelativeTo(null);
        validate();
    }
    
    public  VotingResults(VotingSettings parent, VotingResultsPanel resultsPanel, boolean fullscreen) {
        addKeyListener(this);
        setLayout(new BorderLayout());
        this.parent = parent;
        this.resultsPanel = resultsPanel;
        this.resultsPanel.setParent(this);
        add(resultsPanel, BorderLayout.CENTER);
        //setUndecorated(fullscreen);
        toggleFullscreen(0, fullscreen);
        //pack();
        //add(resultsPanel);
        //setSize(400, 280);
        setLocationRelativeTo(null);
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
            gs[screen].setFullScreenWindow( this );
        }
        else if( gs.length > 0 )
        {
            setUndecorated(fullscreen);
            setResizable(!fullscreen);
            gs[0].setFullScreenWindow( this );
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
