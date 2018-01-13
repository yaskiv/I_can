package home.antonyaskiv.i_can.Model;

import com.indoorway.android.common.sdk.model.Coordinates;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Road {
    private Coordinates coordinates;
    private  Level level;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Road(Coordinates coordinates, Level level) {

        this.coordinates = coordinates;
        this.level = level;
    }
}
