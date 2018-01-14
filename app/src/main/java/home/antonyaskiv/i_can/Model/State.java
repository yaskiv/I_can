package home.antonyaskiv.i_can.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class State implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.s_isActive);
        dest.writeParcelable(this.s_Last_exit, flags);
    }

    public State() {
    }

    protected State(Parcel in) {
        this.s_isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.s_Last_exit = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>() {
        @Override
        public State createFromParcel(Parcel source) {
            return new State(source);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };
}
