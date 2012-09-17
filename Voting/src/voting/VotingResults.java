package voting;

import java.util.ArrayList;

import javax.swing.JFrame;

public class VotingResults extends JFrame{

    private ArrayList<Participant> participants;
    private VotingResultsPanel resultsPanel;
    
    public  VotingResults(ArrayList<Participant> participants) {
        resultsPanel = new VotingResultsPanel(participants);
        add(resultsPanel);
        setSize(400, 280);
        setLocationRelativeTo(null);
        //setVisible(true);
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
