package home.antonyaskiv.i_can.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Level implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UUID);
        dest.writeValue(this.number);
        dest.writeParcelable(this.location, flags);
    }

    protected Level(Parcel in) {
        this.UUID = in.readString();
        this.number = (Integer) in.readValue(Integer.class.getClassLoader());
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel source) {
            return new Level(source);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };
}
