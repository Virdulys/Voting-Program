/**
 * 
 */
package voting;

/**
 * @author Vytautas
 *
 */
public class Participant {
    private int points;
    private String participantName;
    private String teamName;
    
    public Participant(String participantName, String teamName, int points) {
        super();
        this.points = points;
        this.participantName = participantName;
        this.teamName = teamName;
    }
    
    public Participant() {
        super();
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
    
    
    

}
