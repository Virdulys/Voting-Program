package voting;
//aaa
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class VotingResultsPanel extends JPanel {
    public VotingResultsPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        // Draw Text
        g.drawString("This is my custom Panel!",10,20);
    } 
}
