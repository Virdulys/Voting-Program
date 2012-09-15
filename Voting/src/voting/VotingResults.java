package voting;

import java.util.Vector;

import javax.swing.JFrame;

public class VotingResults extends JFrame{

    private Vector<Participant> participants;
    private VotingResultsPanel resultsPanel;
    
    public  VotingResults(Vector<Participant> participants) {
        resultsPanel = new VotingResultsPanel(participants);
        add(resultsPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 280);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setParticipants(Vector<Participant> participants) {
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
