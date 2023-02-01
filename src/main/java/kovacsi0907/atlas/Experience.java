package kovacsi0907.atlas;

import kovacsi0907.atlas.Skills.ExpType;

public class Experience {
    public ExpType type;
    public int points;

    public Experience(ExpType type, int points){
        this.type = type;
        this.points = points;
    }
}
