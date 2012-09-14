package voting;
//aaa
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import java.awt.Canvas;

public class VotingResults {

    private JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Create the application.
     */
    public VotingResults() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(250,250);
        VotingResultsPanel resultsPanel = new VotingResultsPanel();
        f.add(resultsPanel);
        f.pack();
        //f.setVisible(true);
    }
    
}
