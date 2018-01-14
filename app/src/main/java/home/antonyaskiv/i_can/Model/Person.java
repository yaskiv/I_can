package home.antonyaskiv.i_can.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Person implements Parcelable {
    private String p_Name;
    private String p_Surname;
    private String p_email;
    private List<Categories> p_List_of_subscribes;
    private Level p_Level;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Level getP_Level() {
        return p_Level;
    }

    public void setP_Level(Level p_Level) {
        this.p_Level = p_Level;
    }

    private State p_State;

    public Person(String p_Name, String p_Surname, String p_email,
                  List<Categories> p_List_of_subscribes,Location location,Level level) {
        this.p_Name = p_Name;
        this.p_Surname = p_Surname;
        this.p_email = p_email;
        this.p_List_of_subscribes = p_List_of_subscribes;
        this.p_Level=level;
        this.location=location;
    }

    public String getP_Name() {
        return p_Name;
    }

    public void setP_Name(String p_Name) {
        this.p_Name = p_Name;
    }

    public String getP_Surname() {
        return p_Surname;
    }

    public void setP_Surname(String p_Surname) {
        this.p_Surname = p_Surname;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public List<Categories> getP_List_of_subscribes() {
        return p_List_of_subscribes;
    }

    public void setP_List_of_subscribes(List<Categories> p_List_of_subscribes) {
        this.p_List_of_subscribes = p_List_of_subscribes;
    }





    public State getP_State() {
        return p_State;
    }

    public void setP_State(State p_State) {
        this.p_State = p_State;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.p_Name);
        dest.writeString(this.p_Surname);
        dest.writeString(this.p_email);
        dest.writeList(this.p_List_of_subscribes);
        dest.writeParcelable(this.p_Level, flags);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.p_State, flags);
    }

    protected Person(Parcel in) {
        this.p_Name = in.readString();
        this.p_Surname = in.readString();
        this.p_email = in.readString();
        this.p_List_of_subscribes = new ArrayList<Categories>();
        in.readList(this.p_List_of_subscribes, Categories.class.getClassLoader());
        this.p_Level = in.readParcelable(Level.class.getClassLoader());
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.p_State = in.readParcelable(State.class.getClassLoader());
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
