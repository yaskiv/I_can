package home.antonyaskiv.i_can.Model;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Level {
    private  String UUID;
    private Integer number;
    private Location location;

    public Level(String UUID, Integer number, Location location) {
        this.UUID = UUID;
        this.number = number;
        this.location = location;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
