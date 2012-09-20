package voting;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.ArrayList;

import javax.swing.JPanel;

public class VotingResultsPanel extends JPanel implements Runnable {

    private ArrayList<Participant> participants;
    private ArrayList<Participant> participantsSorted;
    private boolean refresh = true; //Variable for forcing rerunning of refresh
    private boolean animate = false; //Variable for forcing rerunning of animation
    private boolean initDone = false; //Variable stops rendering of participants, until initialize is done.
    private Thread animator;
    private RenderingHints rh;
    private int entryHeight ; // Size of the entry (background + participants name + team + points)
    private int entryWidth ; 
    private int entryTopSpacing ; //Pixels to be left out from the top when painting participants
    private int entrySideSpacing = 20; // Pixels to be left out from the left side when painting entries
    private int entryCount;
    private int fontSize;
    private FontMetrics fontMetrics;
    //Hard coded values start here
    private String FONT = "Times"; // Font name
    private final int DURATION = 300; // Participants sorting animation duration
    private int delay = 50; //Thread delay
    private Paint entryBackgroundColor = Color.GRAY;
    private Paint entryFontColor = Color.CYAN;
    
    public VotingResultsPanel(ArrayList<Participant> participants) {
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
        if (refresh && !participants.isEmpty()) {  //Checking if we need to refresh entries
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            entryCount = participants.size();
            //Top spacing is 15% of entry width
            entryTopSpacing = (getSize().height / entryCount) * 15 / 100; 
            entryHeight = (getSize().height  - entryTopSpacing * (entryCount+1)) / entryCount;
            fontSize = entryHeight * 80 / 100; //Font size is 80% of entries height
            entryWidth = getSize().width - entrySideSpacing*2;
            
            for (int i = 0; i < participants.size(); i++) {   
                // Setting last position (this is needed for animation)
                participants.get(i).setLastPos(participants.get(i).getNewPos()); 
                //Creating an entry - image, from participants data 
                BufferedImage offImage = new BufferedImage(entryWidth,
                        entryHeight, BufferedImage.TYPE_INT_ARGB);
                //Getting image drawing graphics
                Graphics2D g2 = offImage.createGraphics();
                g2.setRenderingHints(rh);
                //Setting entries background color 
                g2.setPaint(entryBackgroundColor );
                g2.fillRect(0, 0, entryWidth, entryHeight);
                //Setting up font
                Font theFont = new Font(FONT, Font.BOLD, fontSize);
                g2.setPaint(entryFontColor );
                g2.setFont(theFont);
                // fontMetrics is used to get the width of font (mainly for points to align equally)
                fontMetrics = g2.getFontMetrics();
                //Drawing participants data into entry
                g2.drawString(participants.get(i).getParticipantName(), 20, fontSize);
                //Here are some hard coded value to leave spacing from left side and top (entryWidth -50, 20)
                g2.drawString(String.valueOf(participants.get(i).getPoints()),
                        +  entryWidth - 20 - fontMetrics.stringWidth(String.valueOf(participants.get(i).getPoints())), fontSize); //TODO make number offsets equal from the right
                //Setting drawn image to participants data
                participants.get(i).setBuffImage(offImage);
            }       
            //Create a shallow copy of participants list
            //participantsSorted = cloneParticipants(participants);
            participantsSorted = new ArrayList<Participant>(participants);
            //participantsSorted = (ArrayList) participants.clone();
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
        if (initDone && !participants.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHints(rh);
            //Here we go through the list of the participants and draw them on screen
            //We use shallow sorted copy to index them
            for (int i = 0; i < participantsSorted.size(); i++) {
                if (participantsSorted.get(i).getBuffImage() != null){
                    g2d.drawImage(participantsSorted.get(i).getBuffImage(), null, entrySideSpacing,
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
