package home.antonyaskiv.i_can.Model;

import java.util.List;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Person {
    private String p_Name;
    private String p_Surname;
    private String p_Login;
    private String p_email;
    private List<Categories> p_List_of_subscribes;
    private Location p_Last_location;
    private Level p_Level;

    public Level getP_Level() {
        return p_Level;
    }

    public void setP_Level(Level p_Level) {
        this.p_Level = p_Level;
    }

    private State p_State;

    public Person(String p_Name, String p_Surname, String p_Login, String p_email, List<Categories> p_List_of_subscribes, State p_State) {
        this.p_Name = p_Name;
        this.p_Surname = p_Surname;
        this.p_Login = p_Login;
        this.p_email = p_email;
        this.p_List_of_subscribes = p_List_of_subscribes;
        this.p_State = p_State;
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

    public String getP_Login() {
        return p_Login;
    }

    public void setP_Login(String p_Login) {
        this.p_Login = p_Login;
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

    public Location getP_Last_location() {
        return p_Last_location;
    }

    public void setP_Last_location(Location p_Last_location) {
        this.p_Last_location = p_Last_location;
    }

    public State getP_State() {
        return p_State;
    }

    public void setP_State(State p_State) {
        this.p_State = p_State;
    }
}
