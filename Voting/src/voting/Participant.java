/**
 * 
 */
package voting;

import java.awt.image.BufferedImage;

import org.pushingpixels.trident.Timeline;

/**
 * @author Vytautas
 *
 */
public class Participant implements Comparable<Participant>{
    private int points;
    private String participantName;
    private String teamName;
    
    public BufferedImage BuffImage;
    public int LastPos;
    public int NewPos;
    public int y;
    public  Timeline animateTimeline;
    
    public Participant(String participantName, String teamName, int points) {
        super();
        this.points = points;
        this.participantName = participantName;
        this.teamName = teamName;
    }
    
    public Participant() {
        super();
    }
    
    public void animatePos(int start, int end, int duration) {
        System.out.println(start + " "+ end);
        this.animateTimeline = new Timeline(this);
        this.animateTimeline.addPropertyToInterpolate(
                Timeline.<Integer> property("y").from(start).to(end));
        this.animateTimeline.setDuration(duration);        
        this.animateTimeline.play();
    }
    
    public int getNewPos() {
        return NewPos;
    }
    public void setNewPos(int newPos) {
        NewPos = newPos;
    }
    public int getLastPos() {
        return LastPos;
    }
    public void setLastPos(int lastPos) {
        LastPos = lastPos;
    }
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public BufferedImage getBuffImage() {
        return BuffImage;
    }
    public void setBuffImage(BufferedImage buffImage) {
        BuffImage = buffImage;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    @Override
    public int compareTo(Participant arg0) {
        return this.getPoints() - arg0.getPoints() ;
    }
    

}
