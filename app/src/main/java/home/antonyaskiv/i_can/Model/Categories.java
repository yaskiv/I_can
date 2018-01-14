package home.antonyaskiv.i_can.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Categories implements Parcelable {
    private String c_Name;

    public Categories(String c_Name) {
        this.c_Name = c_Name;
    }

    public String getC_Name() {
        return c_Name;
    }

    public void setC_Name(String c_Name) {
        this.c_Name = c_Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.c_Name);
    }

    protected Categories(Parcel in) {
        this.c_Name = in.readString();
    }

    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel source) {
            return new Categories(source);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}
