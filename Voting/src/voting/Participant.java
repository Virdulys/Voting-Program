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
public class Participant implements Comparable<Participant> {
    private int points;
    private String participantName;
    private Team team = null;
    
    //Animation variables
    private BufferedImage buffImage; // We keep generated entry for painting here
    private int lastPos; // We animate from this position to newPos
    private int newPos; // We animate from oldPos to his position
    private int y; //y coordinate is used for animating y values
    private  Timeline animateTimeline; //Animator
    
    public Participant(String participantName, int points) {
        super();
        this.points = points;
        this.participantName = participantName;
    }
    
    public Participant() {
        super();
    }
        
    //Here we initialize the animation from oldPos to newPos
    public void animatePos(int start, int end, int duration) {
        this.animateTimeline = new Timeline(this); //We create new Timeline
        this.animateTimeline.addPropertyToInterpolate(
                Timeline.<Integer> property("y").from(start).to(end)); //Set animation properties
        this.animateTimeline.setDuration(duration);         //Animation duration
        this.animateTimeline.play(); // We start animation
    }
    
    public int getNewPos() {
        return newPos;
    }
    public void setNewPos(int newPos) {
        this.newPos = newPos;
    }
    public int getLastPos() {
        return lastPos;
    }
    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public BufferedImage getBuffImage() {
        return buffImage;
    }
    public void setBuffImage(BufferedImage buffImage) {
        this.buffImage = buffImage;
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
    
    @Override
    public int compareTo(Participant arg0) {
        return this.getPoints() - arg0.getPoints() ;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    

}
