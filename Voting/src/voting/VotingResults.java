package voting;

import java.util.Vector;

import javax.swing.JFrame;

public class VotingResults extends JFrame{

    private Vector<Participant> participants;
    public VotingResultsPanel resultsPanel;
    
    public void createAndShowGUI() {
        resultsPanel = new VotingResultsPanel();
        add(resultsPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 280);
        setLocationRelativeTo(null);
        setVisible(true);
        resultsPanel.setParticipantsList(participants);
    }

    public void setParticipants(Vector<Participant> participants) {
        this.participants = participants;
    }
    
    public void refreshResults() {
        resultsPanel.refreshResults();
    }
    
}
