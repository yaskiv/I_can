package home.antonyaskiv.i_can.View;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.indoorway.android.common.sdk.model.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import home.antonyaskiv.i_can.Application.App;
import home.antonyaskiv.i_can.Model.Categories;
import home.antonyaskiv.i_can.Model.Level;
import home.antonyaskiv.i_can.Model.Location;
import home.antonyaskiv.i_can.Model.Messages;
import home.antonyaskiv.i_can.Model.Person;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;
import home.antonyaskiv.i_can.R;
import home.antonyaskiv.i_can.Tools.Constans;
import home.antonyaskiv.i_can.View.Adapters.AdapterForRecyclerViewOnMyPosts;
import home.antonyaskiv.i_can.View.Dialogs.AddDialog;

/**
 * Created by VB on 13.01.2018.
 */

public class MyPostsFragment extends Fragment {

    @Inject
    ImplMapFragmentPresenter implMapFragmentPresenter;
    private RecyclerView recyclerView;

    public MyPostsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_post_form, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment  addDialog=new AddDialog();
                Bundle args = new Bundle();
                args.putParcelable("Person",listPersons.get(implMapFragmentPresenter.getLoggedUserUid()));
                addDialog.setArguments(args);
                android.support.v4.app.FragmentManager quote_fm = ((MainActivity)getContext()).getSupportFragmentManager();;

                addDialog.show(quote_fm, "Add");


            }
        });
        recyclerView = view.findViewById(R.id.recycler_for_my_posts);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getMessageFromFirebase();
        return view;
    }
    HashMap<String, Person> listPersons;
    public  void getMessageFromFirebase()
    {
        implMapFragmentPresenter.insertMessage();
        implMapFragmentPresenter.userReference = implMapFragmentPresenter.firebaseDatabase.getInstance().getReference();
        ValueEventListener userEventListenerMyMessages = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Messages> myMessages = new ArrayList<>();
               listPersons = new HashMap<>();
                HashMap<String, HashMap<String, Object>> mapPersons = (HashMap<String, HashMap<String, Object>>) dataSnapshot.child(Constans.FIREBASE_PERSONS).getValue();
                for (Map.Entry<String, HashMap<String, Object>> entry : mapPersons.entrySet()) {
                    HashMap<String, Object> values = entry.getValue();
                    String name = String.valueOf(values.get(Constans.FIREBASE_PERSON_NAME));
                    String surName = String.valueOf(values.get(Constans.FIREBASE_PERSON_SURNAME));
                    String email = String.valueOf(values.get(Constans.FIREBASE_PERSON_EMAIL));
                    List subscribes = (List) values.get(Constans.FIREBASE_MESSAGES_CATEGORY);
                    Coordinates coordinates = null;
                    String level_uuid = null;
                    Integer level_num = null;
                    if(values.get(Constans.FIREBASE_PERSON_LEVEL)!= null) {
                        HashMap<String, Object> valuesLevel = (HashMap<String, Object>) values.get(Constans.FIREBASE_PERSON_LEVEL);
                        level_uuid = String.valueOf(valuesLevel.get(Constans.FIREBASE_PERSON_LEVEL_UUID));
                        Double level_lon = (Double) valuesLevel.get(Constans.FIREBASE_PERSON_LEVEL_LON);
                        Double level_lat = (Double) valuesLevel.get(Constans.FIREBASE_PERSON_LEVEL_LAT);
                        level_num = ((Long) valuesLevel.get(Constans.FIREBASE_PERSON_LEVEL_NUMBER)).intValue();
                        coordinates = new Coordinates(level_lat,level_lon);
                    }
                    Coordinates coordinatesLocation = null;
                    if(values.get(Constans.FIREBASE_PERSON_CORDINATES) != null) {
                        HashMap<String, Object> valuesLocation = (HashMap<String, Object>) values.get(Constans.FIREBASE_PERSON_CORDINATES);
                        Double location_lat = (Double) valuesLocation.get(Constans.FIREBASE_PERSON_LOCATION_LAT);
                        Double location_lon = (Double) valuesLocation.get(Constans.FIREBASE_PERSON_LOCATION_LON);
                        coordinatesLocation = new Coordinates(location_lat, location_lon);
                    }

                    Person person;
                    if(coordinatesLocation != null && coordinates != null){
                        person = new Person(name,surName,email,subscribes
                                ,new Location(coordinatesLocation),new Level(level_uuid,level_num,new Location(coordinates)));
                    } else {
                        person = new Person(name,surName,email,subscribes
                                ,null,null);
                    }
                    listPersons.put(entry.getKey(), person);
                }


                HashMap<String, HashMap<String, Object>> mapMessage = (HashMap<String, HashMap<String, Object>>) dataSnapshot.child(Constans.FIREBASE_MESSAGES).getValue();
                for (Map.Entry<String, HashMap<String, Object>> entry : mapMessage.entrySet()) {
                    HashMap<String, Object> values = entry.getValue();

                    if (implMapFragmentPresenter.getLoggedUserUid().equals(String.valueOf(values.get(Constans.FIREBASE_MESSAGES_OWNER)))) {
                        Integer id = ((Long) values.get(Constans.FIREBASE_MESSAGES_ID)).intValue();
                        String title = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_TITTLE));
                        String text = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_TEXT));
                        String category = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_CATEGORY));
                        String owner = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_OWNER));
                        Messages messages = new Messages(id, title, text, new Categories(category), listPersons.get(owner));
                        myMessages.add(messages);
                    }
                }
                initRecycler(myMessages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        implMapFragmentPresenter.userReference.addValueEventListener(userEventListenerMyMessages);
    }private List<Messages> list;
    public void initRecycler(List<Messages> list) {
        this.list=list;
        Log.d("List", String.valueOf(list.size()));
        AdapterForRecyclerViewOnMyPosts adapter = new AdapterForRecyclerViewOnMyPosts(list,getContext(),this );
        recyclerView.setAdapter(adapter);
    }

    public void delete(Messages message) {
        remoteMyMessage(message);
    }


    public void remoteMyMessage(Messages messages){
        implMapFragmentPresenter.firebaseDatabase.getInstance().getReference().child(Constans.FIREBASE_MESSAGES).child(messages.getM_Id()+"-")
                .removeValue();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
