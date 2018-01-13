package home.antonyaskiv.i_can.Presenters;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

import javax.inject.Inject;

import home.antonyaskiv.i_can.Model.Messages;
import home.antonyaskiv.i_can.Model.Person;
import home.antonyaskiv.i_can.Tools.Constans;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class ImplMapFragmentPresenter {
    private final Context context;
    public ImplMapFragmentPresenter(Context context) {

        this.context = context;
    }

    private static FirebaseStorage firebaseStorage;
    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseAuth firebaseAuth;

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

    public static void insertOrUpdateMessage(Messages messages){
        HashMap<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(Constans.FIREBASE_MESSAGES_ID,messages.getM_Id());
        childUpdates.put(Constans.FIREBASE_MESSAGES_CATEGORY,messages.getM_Category().getC_Name());
        childUpdates.put(Constans.FIREBASE_MESSAGES_OWNER,messages.getM_Owner().getP_Login());
        childUpdates.put(Constans.FIREBASE_MESSAGES_TEXT,messages.getM_Text());
        childUpdates.put(Constans.FIREBASE_MESSAGES_TITTLE,messages.getM_Title());
        firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_MESSAGES).updateChildren(childUpdates);
    }

    public static DatabaseReference getUserData(){
        return firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_PERSONS).child(getLoggedUserUid());
    }

}
