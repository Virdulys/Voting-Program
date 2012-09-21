package voting;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class VotingResultsPanel extends JPanel implements Runnable {

    private ArrayList<Participant> participants;
    private ArrayList<Participant> participantsSorted;
    private boolean refresh = true; //Variable for forcing rerunning of refresh
    private boolean animate = false; //Variable for forcing rerunning of animation
    private boolean initDone = false; //Variable stops rendering of participants, until initialize is done.
    private Thread animator;
    private RenderingHints rh;
    private int entryHeight; // Size of the entry (background + participants name + team + points)
    private int entryWidth; 
    private int entryTopSpacing; //Pixels to be left out from the top when painting participants    
    private int entryCount;
    private int fontSize;
    private FontMetrics fontMetrics;
    private int resultsNumber;
    //Hard coded values start here
    private int entrySideSpacing = 20; // Pixels to be left out from the left side when painting entries
    private String FONT = "Times"; // Font name
    private final int DURATION = 300; // Participants sorting animation duration
    private int delay = 50; //Thread delay
    private int defaultColor = 60;
    private int gradientStep = 10;
    private Paint entryBackgroundColor = new Color(defaultColor, defaultColor, defaultColor);
    private Paint entryFontColor = Color.WHITE;
    private Paint pointsFontColor = new Color(255, 164, 209);
    private Image background = null;
    private BufferedImage backgroundBuff = null;
    
    public VotingResultsPanel(ArrayList<Participant> participants) {
        /*try {
            this.background = ImageIO.read(new File("/images/bg_dark.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        Toolkit toolkit = this.getToolkit();
        this.background = toolkit.getImage(this.getClass().getResource("/images/bg_dark.jpg"));
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
            Thread.sleep(100);
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
        createBackground();
        if (refresh && !participants.isEmpty()) {  //Checking if we need to refresh entries
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            backgroundBuff = new BufferedImage(getSize().width,
                    getSize().height, BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D bg = backgroundBuff.createGraphics();
            
            int iw = background.getWidth(this);
            int ih = background.getHeight(this);
            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getSize().width; x += iw) {
                    for (int y = 0; y < getSize().height; y += ih) {
                        bg.drawImage(background, x, y, iw, ih, this);
                    }
                }
            }
            
            //With a help of this, we now can scale entryHeight with
            //Setting -> resultsNumber!
            /*if (participants.size() >= resultsNumber)
                entryCount = participants.size();
            else
                entryCount = resultsNumber; */
            entryCount = resultsNumber;
            //Top spacing is 15% of entry width
            entryTopSpacing = (getSize().height / entryCount) * 15 / 100; 
            entryHeight = (getSize().height  - entryTopSpacing * (entryCount+1)) / entryCount;
            fontSize = entryHeight * 80 / 100; //Font size is 80% of entries height
            entryWidth = getSize().width - entrySideSpacing*2;
            
            for (int i = 0; i < participants.size(); i++) {
                int color = defaultColor - gradientStep*i;
                if (color < 0)
                    color = 0;
                entryBackgroundColor = new Color(color, color, color);
                // Setting last position (this is needed for animation)
                participants.get(i).setLastPos(participants.get(i).getNewPos()); 
                //Creating an entry - image, from participants data 
                BufferedImage offImage = new BufferedImage(entryWidth,
                        entryHeight, BufferedImage.TYPE_INT_ARGB);
                //Getting image drawing graphics
                Graphics2D g2 = offImage.createGraphics();
                g2.setRenderingHints(rh);
                //Setting entries background color 
                g2.setPaint(entryBackgroundColor);
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
                g2.setPaint(entryBackgroundColor);
                g2.fillRect(entryWidth - 40 - fontMetrics.stringWidth(
                        String.valueOf(participants.get(i).getPoints())),0, entryWidth, entryHeight );
                g2.setPaint(pointsFontColor ); //Set points color
                g2.drawString(String.valueOf(participants.get(i).getPoints()),
                                + entryWidth - 20 - fontMetrics.stringWidth(
                                        String.valueOf(participants.get(i).getPoints())),
                                fontSize); 
                //TODO We need to shorten this up, it looks just awful
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(rh);
        g2d.drawImage(backgroundBuff, null, 0, 0);
        if (backgroundBuff == null)
            createBackground();
        
        if (initDone && !participants.isEmpty()) {
            
            //Here we did such an awesome thing, that no comment can describe it!
            //Ok ok i will try
            //Here we can scale size with a help of setting -> resultsNumber!
            int paintOffset = 0;
            /*if (participantsSorted.size() > resultsNumber){
                paintOffset = (getHeight() - ((entryHeight+entryTopSpacing) * resultsNumber))/2;
            }*/
            if (participantsSorted.size() < resultsNumber) {
                paintOffset = (getHeight() - ((entryHeight+entryTopSpacing) * participantsSorted.size()))/2;
            }
            //Here we go through the list of the participants and draw them on screen
            //We use shallow sorted copy to index them
            for (int i = 0; i < participantsSorted.size(); i++) {
                if (i == resultsNumber)
                    break;
                if (participantsSorted.get(i).getBuffImage() != null) {
                    g2d.drawImage(participantsSorted.get(i).getBuffImage(), null, entrySideSpacing,
                            participantsSorted.get(i).getY() + paintOffset);
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
    public void setResultsNumber(int resultsNumber) {
        this.resultsNumber = resultsNumber;
    }
    
    private void createBackground() {
        backgroundBuff = new BufferedImage(getSize().width,
                getSize().height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D bg = backgroundBuff.createGraphics();
        
        int iw = background.getWidth(this);
        int ih = background.getHeight(this);
        if (iw > 0 && ih > 0) {
            for (int x = 0; x < getSize().width; x += iw) {
                for (int y = 0; y < getSize().height; y += ih) {
                    bg.drawImage(background, x, y, iw, ih, this);
                }
            }
        }        
    }
}
