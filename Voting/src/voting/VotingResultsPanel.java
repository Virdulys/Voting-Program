package voting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JPanel;

public class VotingResultsPanel extends JPanel implements Runnable {

    private Vector<Participant> participants;
    private Vector<Participant> participantsSorted;
    private boolean refresh = true; //Variable for forcing rerunning of refresh
    private boolean animate = true; //Variable for forcing rerunning of animation
    private boolean initDone = false; //Variable stops rendering of participants, until initialize is done.
    private Thread animator;
    private RenderingHints rh; 
    private int entryHeight = 30; // Size of the entry (background + participants name + team + points)
    private int entryWidth = 350; // 400
    private int entryTopSpacing = 15; //Pixels to be left out from the top when painting participants
    private int entryLeftSpacing = 20; // Pixels to be left out from the left side when painting entries
    private final int DURATION = 300; // Participants sorting animation duration
    private String FONT = "Times"; // Font name
    private int delay = 50; //Thread delay
    

    public VotingResultsPanel(Vector<Participant> participants) {
        
        this.participants = participants;
        setBackground(Color.DARK_GRAY);
        setDoubleBuffered(true);
        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                refresh = true; // When window is resized - repaint
            }
        });
        
        animator = new Thread(this);
        animator.start();
    }
    
    public void run() {     
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {

            cycle(); // Call to calculations method
            repaint(); //Call to rendering method

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = delay - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
    }
    
    public void cycle() {
        if (refresh) {  //Checking if we need to refresh entries
            for (int i = 0; i < participants.size(); i++) {   
                // Setting last position (this is needed for animation)
                participants.get(i).setLastPos(participants.get(i).getNewPos()); 
                
                //Screen, entry, font calculations from screen resolution
                int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
                int fontSize = (int)Math.round(30.0 * screenRes / 72.0);    
                entryWidth = getSize().width - 40;
                entryHeight = (int)Math.round(screenRes / 150.0)+50;
                
                //Creating an entry - image, from participants data 
                BufferedImage offImage = new BufferedImage(entryWidth,
                        entryHeight, BufferedImage.TYPE_INT_ARGB);
                //Getting image drawing graphics
                Graphics2D g2 = offImage.createGraphics(); 
                g2.setRenderingHints(rh);
                //Setting entries background color 
                g2.setPaint(Color.GRAY);
                g2.fillRect(0, 0, entryWidth, entryHeight);
                //Setting up font
                Font theFont = new Font(FONT, Font.BOLD, fontSize);
                g2.setPaint(Color.CYAN);
                g2.setFont(theFont);
                //Drawing participants data into entry
                //Here are some hard coded value to leave spacing from left side and top (20, 20)
                g2.drawString(participants.get(i).getParticipantName(), 20, 40);
                //Here are some hard coded value to leave spacing from left side and top (entryWidth -50, 20)
                g2.drawString("" + participants.get(i).getPoints(),
                        +entryWidth - 50, 40);
                //Setting drawn image to participants data
                participants.get(i).setBuffImage(offImage);
            }       
            //Create a shallow copy of participants list
            participantsSorted = (Vector<Participant>) participants.clone();
            //Sort that shallow copy, so that we don't mess up original list
            Collections.sort(participantsSorted, Collections.reverseOrder());
            
            //Set locations of participants in sorted list 
            //Because animation is done from oldPos to newPos
            for (int i = 0; i < participantsSorted.size(); i++) {
                participantsSorted.get(i).setNewPos(i);
            }
            //Setting refresh variable off
            refresh = false;
            animate = true;
        }
        
        if (animate) {
            for (int i = 0; i < participantsSorted.size(); i++) {  
                Participant tempParticipant =  participantsSorted.get(i);
                //Here we turn on animation from oldPos to newPos
                tempParticipant.animatePos(posToY(tempParticipant.getLastPos()), posToY(tempParticipant.getNewPos()), DURATION);
            }
            animate= false;
        }
        //We can now start drawing when everything is calculated for the first time
        initDone = true;
    }
    
    public void paint(Graphics g) {
        super.paintComponent(g);  
        if (initDone) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHints(rh);
            //Here we go through the list of the participants and draw them on screen
            //We use shallow sorted copy to index them
            for (int i = 0; i < participantsSorted.size(); i++) {
                if (participantsSorted.get(i).getBuffImage() != null){
                    g2d.drawImage(participantsSorted.get(i).getBuffImage(), null, entryLeftSpacing,
                            participantsSorted.get(i).getY());
                }
            }
            Toolkit.getDefaultToolkit().sync();
            g.dispose();  
        }
    }

    public void refreshResults() {
        refresh = true;     
    }
    
    //Method that calculates y coordinates based on participants position
    public int posToY (int pos) {
        return entryTopSpacing * (pos + 1) + entryHeight * pos;
    }
}
