package voting;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class VotingResults extends JFrame{
    private class CustomDispatcher implements KeyEventDispatcher {
        // Custom keyEvent dispatcher that shouldn't block or freeze the program
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                // Nothing
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                //System.out.println(e.getKeyChar());
                if (e.getKeyChar() == 'f') {
                    //TODO call fullscreen method from here
                }
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                // Nothing
            }
            return false;
        }
    }
    
    private ArrayList<Participant> participants;
    private VotingResultsPanel resultsPanel;
    
    public  VotingResults(ArrayList<Participant> participants) {
        pack();
        resultsPanel = new VotingResultsPanel(participants, this);
        add(resultsPanel);
        setSize(400, 280);
        setLocationRelativeTo(null);
        //setVisible(true);
        // Listening to pressed keys
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new CustomDispatcher());
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
}
