package voting;

public class Team {
    
    // Team properties
    private String name = null;
    private int points = 0;

    public Team() {
        super();
    }

    public Team(String name) {
        super();
        this.name = name;
    }

    public Team(String name, int points) {
        super();
        this.name = name;
        this.points = points;
    }
    
    public Team(Team team) {
        super();
        this.name = team.getName();
        this.points = team.getPoints();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
