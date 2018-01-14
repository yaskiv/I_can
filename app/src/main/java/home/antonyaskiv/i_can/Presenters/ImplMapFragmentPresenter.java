package home.antonyaskiv.i_can.Presenters;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.indoorway.android.common.sdk.model.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import home.antonyaskiv.i_can.Model.Categories;
import home.antonyaskiv.i_can.Model.Level;
import home.antonyaskiv.i_can.Model.Location;
import home.antonyaskiv.i_can.Model.Messages;
import home.antonyaskiv.i_can.Model.Person;
import home.antonyaskiv.i_can.Tools.Constans;
import home.antonyaskiv.i_can.View.MapFragment;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class ImplMapFragmentPresenter {
    private MapFragment mapFragment;
    private Context context;
    public ImplMapFragmentPresenter(Context context) {

        this.context = context;
    }
    public void initMaoFragment(MapFragment mapFragment)
    {
        this.mapFragment=mapFragment;
    }

    private static FirebaseStorage firebaseStorage;
    public static FirebaseDatabase firebaseDatabase;
    private static FirebaseAuth firebaseAuth;
    public static ValueEventListener userEventListener;
    public static  DatabaseReference userReference;

    @Inject
    public ImplMapFragmentPresenter(Context context, FirebaseDatabase firebaseDatabase, FirebaseStorage firebaseStorage, FirebaseAuth firebaseAuth) {
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
    }

    public static String getLoggedUserUid() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return "";
        } else {
            return firebaseUser.getUid();
        }
    }

    public static void insertOrUpdatePerson(Person person){
        HashMap<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(Constans.FIREBASE_PERSON_NAME,person.getP_Name());
        childUpdates.put(Constans.FIREBASE_PERSON_SURNAME, person.getP_Surname());
 //       childUpdates.put(Constans.FIREBASE_PERSON_LIST_OF_SUBSCRIBES,person.getP_List_of_subscribes());
//        childUpdates.put(Constans.FIREBASE_PERSON_LAST_LOCATION,person.getP_Last_location().getCoordinates().toString());
//        childUpdates.put(Constans.FIREBASE_PERSON_STATE_IS_ACTIVE,person.getP_State().getS_isActive());
//        childUpdates.put(Constans.FIREBASE_PERSON_STATE_LAST_LOCATION,person.getP_Last_location().getCoordinates().toString());
        firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_PERSONS).child(getLoggedUserUid())
                .updateChildren(childUpdates);
    }

    public static   void insertOrUpdateMessage(Messages messages){
        HashMap<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(Constans.FIREBASE_MESSAGES_ID,messages.getM_Id());
        childUpdates.put(Constans.FIREBASE_MESSAGES_CATEGORY,messages.getM_Category().getC_Name());
        childUpdates.put(Constans.FIREBASE_MESSAGES_OWNER,getLoggedUserUid());
        childUpdates.put(Constans.FIREBASE_MESSAGES_TEXT,messages.getM_Text());
        childUpdates.put(Constans.FIREBASE_MESSAGES_TITTLE,messages.getM_Title());
        firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_MESSAGES).child(messages.getM_Id().toString() + "-").updateChildren(childUpdates);
    }

    public static void insertOrUpdateCategories(List<Categories> list){
        HashMap<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(Constans.FIREBASE_PERSON_LIST_OF_SUBSCRIBES,list);
        firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_PERSONS).child(getLoggedUserUid())
                .updateChildren(childUpdates);

    }

    public  void insertOrUpdateLocation(Location location){
        HashMap<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(Constans.FIREBASE_PERSON_LOCATION_LON,location.getCoordinates().getLongitude());
        childUpdates.put(Constans.FIREBASE_PERSON_LOCATION_LAT,location.getCoordinates().getLatitude());
        firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_PERSONS).child(getLoggedUserUid())
                .child(Constans.FIREBASE_PERSON_CORDINATES).updateChildren(childUpdates);

    }

    public  void insertOrUpdateLevel(Level level){
        HashMap<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(Constans.FIREBASE_PERSON_LEVEL_UUID,level.getUUID());
        childUpdates.put(Constans.FIREBASE_PERSON_LEVEL_NUMBER,level.getNumber());
        childUpdates.put(Constans.FIREBASE_PERSON_LEVEL_LON,level.getLocation().getCoordinates().getLongitude());
        childUpdates.put(Constans.FIREBASE_PERSON_LEVEL_LAT,level.getLocation().getCoordinates().getLatitude());
        firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_PERSONS).child(getLoggedUserUid())
                .child(Constans.FIREBASE_PERSON_LEVEL).updateChildren(childUpdates);

    }


    public  DatabaseReference getUserData(){
        return firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_PERSONS).child(getLoggedUserUid());
    }

    public static void insertMessage(){
        Messages messages = new Messages(1,"Title1","Text1",new Categories("bla1"),new Person("s",
                "s","s",null,null,null));

        insertOrUpdateMessage(messages);
    }



}
