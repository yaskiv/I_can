package home.antonyaskiv.i_can.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class Messages implements Parcelable {
    private Integer m_Id;
    private String m_Title;
    private String m_Text;
    private Categories m_Category;
    private Person m_Owner;

    public Messages(Integer m_Id, String m_Title, String m_Text, Categories m_Category, Person m_Owner) {
        this.m_Id = m_Id;
        this.m_Title = m_Title;
        this.m_Text = m_Text;
        this.m_Category = m_Category;
        this.m_Owner = m_Owner;
    }

    public Integer getM_Id() {
        return m_Id;
    }

    public void setM_Id(Integer m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_Title() {
        return m_Title;
    }

    public void setM_Title(String m_Title) {
        this.m_Title = m_Title;
    }

    public String getM_Text() {
        return m_Text;
    }

    public void setM_Text(String m_Text) {
        this.m_Text = m_Text;
    }

    public Categories getM_Category() {
        return m_Category;
    }

    public void setM_Category(Categories m_Category) {
        this.m_Category = m_Category;
    }

    public Person getM_Owner() {
        return m_Owner;
    }

    public void setM_Owner(Person m_Owner) {
        this.m_Owner = m_Owner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.m_Id);
        dest.writeString(this.m_Title);
        dest.writeString(this.m_Text);
        dest.writeParcelable(this.m_Category, flags);
        dest.writeParcelable(this.m_Owner, flags);
    }

    protected Messages(Parcel in) {
        this.m_Id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.m_Title = in.readString();
        this.m_Text = in.readString();
        this.m_Category = in.readParcelable(Categories.class.getClassLoader());
        this.m_Owner = in.readParcelable(Person.class.getClassLoader());
    }

    public static final Parcelable.Creator<Messages> CREATOR = new Parcelable.Creator<Messages>() {
        @Override
        public Messages createFromParcel(Parcel source) {
            return new Messages(source);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };
}
