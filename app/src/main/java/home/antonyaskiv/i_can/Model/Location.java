package home.antonyaskiv.i_can.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.indoorway.android.common.sdk.model.Coordinates;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Location implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.coordinates);
    }

    protected Location(Parcel in) {
        this.coordinates = (Coordinates) in.readSerializable();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
