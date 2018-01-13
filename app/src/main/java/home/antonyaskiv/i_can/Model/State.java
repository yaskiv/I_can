package home.antonyaskiv.i_can.Model;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class State {
    private Boolean s_isActive;
    private Location s_Last_exit;

    public Boolean getS_isActive() {
        return s_isActive;
    }

    public void setS_isActive(Boolean s_isActive) {
        this.s_isActive = s_isActive;
    }

    public Location getS_Last_exit() {
        return s_Last_exit;
    }

    public void setS_Last_exit(Location s_Last_exit) {
        this.s_Last_exit = s_Last_exit;
    }
}
