package voting;

import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class VotingResultsPanel extends JPanel implements Runnable{

    private Vector<Participant> participants;
    private Vector<Participant> participantsSorted;
    private boolean refresh = true;
    private boolean animate = false;
    private int delay = 50;
    private RenderingHints rh;
    private int entryHeight = 30; // 290
    private int entryWidth = 350; // 400
    private int entryTopSpacing = 15;
    private int entryLeftSpacing = 20;
    private final int DURATION = 1500;
    private String FONT = "Times";
    private Thread animator;
    

    public VotingResultsPanel() {
        setDoubleBuffered(true);

        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                refresh = true;
            }
        });
        
        animator = new Thread(this);
        animator.start();
    }
    
    public void run() {     
        long beforeTime, timeDiff, sleep, refresher;

        beforeTime = System.currentTimeMillis();
        refresher = 0;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {
            refresher++;
            if (refresher == 2000) {
                refresher = 0;
                refreshResults();
            }
            cycle();
            repaint();

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
        if (refresh) {              
            for (int i = 0; i < participants.size(); i++) {   
                participants.get(i).setLastPos(i);
                
                int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
                int fontSize = (int)Math.round(12.0 * screenRes / 72.0);    
                entryWidth = getSize().width - 40;
                entryHeight = (int)Math.round(screenRes / 150.0)+30;
                
                BufferedImage off_Image = new BufferedImage(entryWidth,
                        entryHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = off_Image.createGraphics(); 
                g2.setRenderingHints(rh);
                g2.setPaint(Color.RED);
                g2.fillRect(0, 0, entryWidth, entryHeight);
                Font theFont = new Font(FONT, Font.PLAIN, fontSize);    
                g2.setPaint(Color.BLUE);
                g2.setFont(theFont);
                g2.drawString(participants.get(i).getParticipantName(), 20, 20);
                g2.drawString("" + participants.get(i).getPoints(),
                        +entryWidth - 50, 20);
                participants.get(i).setBuffImage(off_Image);
            }       
            participantsSorted = (Vector<Participant>) participants.clone();
            Collections.sort(participants, Collections.reverseOrder());
            for (int i = 0; i < participants.size(); i++) {    
                for (int k = 0; k < participantsSorted.size();k++) {
                    if (participants.get(i).getLastPos() == participantsSorted.get(k).getLastPos()) {
                        participants.get(i).setNewPos(k);
                    }
                }
            }    
            
            refresh = false;
            animate = true;
        }
        
        if (animate) {
            System.out.println("Animaiting=============================================");
            for (int i = 0; i < participantsSorted.size(); i++) {  
                System.out.println(participantsSorted.size());
                System.out.println(participantsSorted.get(i).getParticipantName());
                Participant tempParticipant =  participantsSorted.get(i);
                tempParticipant.animatePos(PosToY(tempParticipant.getLastPos()), PosToY(tempParticipant.getNewPos()), DURATION);
            }
            animate= false;
        }
    }
    
    public void paint(Graphics g) {
        super.paintComponent(g);  
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(rh);
        
        for (int i = 0; i < participantsSorted.size(); i++) {
            if (participantsSorted.get(i).BuffImage != null){
                g2d.drawImage(participantsSorted.get(i).BuffImage, null, entryLeftSpacing,
                        participantsSorted.get(i).getY());
            }
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();  
    }

    public void refreshResults() {
        refresh = true;     
    }
    
    public int PosToY (int pos) {
        return entryTopSpacing * (pos + 1) + entryHeight * pos;
    }
    
    public void setParticipantsList(Vector<Participant> participants) {
        this.participants = participants;
    }
}
