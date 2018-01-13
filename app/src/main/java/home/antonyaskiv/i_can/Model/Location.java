package home.antonyaskiv.i_can.Model;

import com.indoorway.android.common.sdk.model.Coordinates;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Location {
    private Coordinates coordinates;

    public Location(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
