package home.antonyaskiv.i_can.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.indoorway.android.common.sdk.listeners.generic.Action0;
import com.indoorway.android.common.sdk.listeners.generic.Action1;
import com.indoorway.android.common.sdk.model.Coordinates;
import com.indoorway.android.common.sdk.model.IndoorwayMap;
import com.indoorway.android.common.sdk.model.IndoorwayNode;
import com.indoorway.android.common.sdk.model.IndoorwayObjectParameters;
import com.indoorway.android.common.sdk.model.IndoorwayPosition;
import com.indoorway.android.location.sdk.IndoorwayLocationSdk;
import com.indoorway.android.location.sdk.model.IndoorwayLocationSdkError;
import com.indoorway.android.location.sdk.model.IndoorwayLocationSdkState;
import com.indoorway.android.map.sdk.view.IndoorwayMapView;
import com.indoorway.android.map.sdk.view.drawable.figures.DrawableCircle;
import com.indoorway.android.map.sdk.view.drawable.figures.DrawableText;
import com.indoorway.android.map.sdk.view.drawable.layers.MarkersLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import home.antonyaskiv.i_can.Application.App;
import home.antonyaskiv.i_can.Model.Categories;
import home.antonyaskiv.i_can.Model.Level;
import home.antonyaskiv.i_can.Model.Levels;
import home.antonyaskiv.i_can.Model.Location;
import home.antonyaskiv.i_can.Model.Messages;
import home.antonyaskiv.i_can.Model.Person;
import home.antonyaskiv.i_can.Model.Road;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;
import home.antonyaskiv.i_can.R;
import home.antonyaskiv.i_can.Tools.Constans;
import home.antonyaskiv.i_can.View.Adapters.AdpterForRecyclerViewOnMapFragment;


public class MapFragment extends Fragment {

    Action1<IndoorwayPosition> listener;
    Action1<IndoorwayLocationSdkState> listenerq;
    Action1<IndoorwayLocationSdkError> listenerw;
    @Inject
    ImplMapFragmentPresenter implMapFragmentPresenter;

    private IndoorwayMapView indoorwayMapView;
    private OnFragmentInteractionListener mListener;
    private IndoorwayPosition currentPosition;
    private Levels levels;
    private Boolean isInRoadToOtherLevel =false;
     Road road;
    private RecyclerView recyclerView;

    public MapFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        implMapFragmentPresenter.initMaoFragment(this);
        setHasOptionsMenu(true);
    }
    private void ChceckLocation()
    {
        LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}



        if(!gps_enabled ) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Loc");
            dialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(myIntent);

                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initListOfLevels();
        CheckPermissions();
        ChceckLocation();
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        indoorwayMapView= view.findViewById(R.id.mapView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        recyclerView = view.findViewById(R.id.recycler_mapFr);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        getMessageFromFirebase();
        getLevelsFromFirebase();
        onChangeState(indoorwayMapView);
        onErrorState();

        onChangePosition(indoorwayMapView);



        return view;
    }
    public  void getMessageFromFirebase(){
        implMapFragmentPresenter.insertMessage();
        final List<Messages> listMessages = new ArrayList<>();
        implMapFragmentPresenter.userReference= implMapFragmentPresenter.firebaseDatabase.getInstance().getReference();
        implMapFragmentPresenter.userEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Messages> listMessages = new ArrayList<>();
                HashMap<String,Person> listPersons = new HashMap<>();
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

                    listPersons.put(entry.getKey(),person);
                }


                HashMap<String, HashMap<String, Object>> mapMessage = (HashMap<String, HashMap<String, Object>>) dataSnapshot.child(Constans.FIREBASE_MESSAGES).getValue();
                for (Map.Entry<String, HashMap<String, Object>> entry : mapMessage.entrySet()) {
                    HashMap<String, Object> values = entry.getValue();

                    Integer id = ((Long)values.get(Constans.FIREBASE_MESSAGES_ID)).intValue();
                    String title = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_TITTLE));
                    String text = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_TEXT));
                    String category = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_CATEGORY));
                    String owner = String.valueOf(values.get(Constans.FIREBASE_MESSAGES_OWNER));
                    Messages messages = new Messages(id,title,text,new Categories(category),listPersons.get(owner));
                    if(!owner.equals(implMapFragmentPresenter.getLoggedUserUid())) {
                        listMessages.add(messages);
                    }
                }
                initRecycler(listMessages);
                Log.d("blablabla ", String.valueOf(listMessages.size()));
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        implMapFragmentPresenter.userReference.addValueEventListener(implMapFragmentPresenter.userEventListener);
        implMapFragmentPresenter. userReference.getDatabase().toString();

    }
    List<Person> listP;
    public void initRecycler(List<Messages> list) {
        listP=new ArrayList<>();
        for (Messages messages: list)
        {
            listP.add(messages.getM_Owner());
        }
        Set<Person> hs = new HashSet<>();
        hs.addAll(listP);
        listP.clear();
        listP.addAll(hs);

        Log.d("List", String.valueOf(list.size()));
        AdpterForRecyclerViewOnMapFragment adapter = new AdpterForRecyclerViewOnMapFragment(list,getContext(),this );
        recyclerView.setAdapter(adapter);
    }
    public  void getLevelsFromFirebase(){
        implMapFragmentPresenter.userReference = implMapFragmentPresenter.firebaseDatabase.getInstance().getReference();
        ValueEventListener userEventListenerLevels = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Level> listLevels = new ArrayList<>();

                HashMap<String, HashMap<String, Object>> mapMessage = (HashMap<String, HashMap<String, Object>>) dataSnapshot.child(Constans.FIREBASE_LEVELS).getValue();
                for (Map.Entry<String, HashMap<String, Object>> entry : mapMessage.entrySet()) {
                    HashMap<String, Object> values = entry.getValue();

                    Integer number = ((Long) values.get(Constans.FIREBASE_LEVELS_NUMBER)).intValue();
                    String uuid = String.valueOf(values.get(Constans.FIREBASE_LEVELS_UUID));
                    Double lon = (Double) values.get(Constans.FIREBASE_LEVELS_LON);
                    Double lat = (Double) values.get(Constans.FIREBASE_LEVELS_LAT);
                    Coordinates coordinates = new Coordinates(lat, lon);
                    Level level = new Level(uuid, number, new Location(coordinates));
                    listLevels.add(level);
                }
                levels.setAllLevels(listLevels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        implMapFragmentPresenter.userReference.addValueEventListener(userEventListenerLevels);
    }
    private void initListOfLevels() {
        levels=new Levels();
        List<Level> levelList=new ArrayList<>();
        levelList.add(new Level("7-QLYjkafkE",0,
                new Location(
                        new Coordinates( 52.2222229, 21.0069496))));
        levelList.add(new Level("gVI7XXuBFCQ",1,
                new Location(
                        new Coordinates( 52.2222229, 21.0069496))));
        levelList.add(new Level("3-_M01M3r5w",2,
                new Location(
                        new Coordinates( 52.2222229, 21.0069496))));
        levels.setAllLevels(levelList);
    }

    private Level searchNumberOfLevel(String uuid,Levels levels)
{
 for (Level level:levels.getAllLevels())
 {
     if(level.getUUID().equals(uuid))
     {
         return level;
     }
 }
 return null;
}

/*
private void addProximity(Coordinates coordinates)
{
    IndoorwayLocationSdk.instance().customProximityEvents()
            .add(new IndoorwayProximityEvent(
                    "proximity-event-id", // identifier
                    IndoorwayProximityEvent.Trigger.ENTER, // trigger on enter or on exit?
                    new IndoorwayProximityEventShape.Circle(
                            coordinates,
                            3.0
                    ),
                    currentPosition.getBuildingUuid(), // building identifier
                    currentPosition.getMapUuid(), // map identifier
                    0L, // (optional) timeout to show notification, will be passed as parapeter to listener
                    new IndoorwayNotificationInfo("title", "description", "url", "image") // (optional) data to show in notification
            ));
}*/
    public void Navigate(Coordinates coordinates, Level level) {
        if( currentPosition.getMapUuid().equals(level.getUUID()))
        {
        indoorwayMapView.getNavigation()
                .start(currentPosition,coordinates );
        isInRoadToOtherLevel =false;

        }
        else
            {
                String direction;
                if(searchNumberOfLevel(currentPosition.getMapUuid(),levels).getNumber()>level.getNumber())
                {
                    direction="Down";
                }
                else
                    {
                        direction="UP";
                    }
                    level=searchNumberOfLevel(level.getUUID(),levels);
                MarkersLayer myLayer = indoorwayMapView.getMarker().addLayer(5);
                myLayer.add(new DrawableText("lfgr",level.getLocation().getCoordinates(),direction,8));

                indoorwayMapView.getNavigation()
                        .start(currentPosition,level.getLocation().getCoordinates() );
                isInRoadToOtherLevel =true;

            }
    }

    long timestamp=0;

    private void onChangePosition(final IndoorwayMapView indoorwayMapView) {
        listener = new Action1<IndoorwayPosition>() {
            @Override
            public void onAction(final IndoorwayPosition position) {
                // store last position as a field
                currentPosition = position;
                Log.d("Position", String.valueOf(currentPosition.getCoordinates().getLatitude()));
                Log.d("Position", String.valueOf(currentPosition.getCoordinates().getLongitude()));

                indoorwayMapView.getPosition().setPosition(position, true);
                Log.d("Position",currentPosition.getMapUuid().toString());
                long currentTimestamp = System.currentTimeMillis() / 1000;
                if(timestamp != 0 && currentTimestamp-timestamp<10){
                    return;
                }

                timestamp = System.currentTimeMillis() / 1000;
                Level level=searchNumberOfLevel(position.getMapUuid(), levels);

                implMapFragmentPresenter.insertOrUpdateLocation(new Location(position.getCoordinates()));
                implMapFragmentPresenter.insertOrUpdateLevel
                        (new Level(position.getMapUuid(),
                                level.getNumber()
                                , level.getLocation()));

            }
        };

        IndoorwayLocationSdk.instance()
                .position()
                .onChange()
                .register(listener);
    }

    private void onErrorState() {
        listenerw= new Action1<IndoorwayLocationSdkError>() {
              @Override
              public void onAction(IndoorwayLocationSdkError error) {
                  if (error instanceof IndoorwayLocationSdkError.BleNotSupported) {
                      Log.d("Position","1");
                       } else if (error instanceof IndoorwayLocationSdkError.MissingPermission) {
                      Log.d("Position","2");
                      // Some permissions are missing, ask for it.permission
                  } else if (error instanceof IndoorwayLocationSdkError.BluetoothDisabled) {
                      Log.d("Position","3");
                      // Bluetooth is disabled, user have to turn it on
                  } else if (error instanceof IndoorwayLocationSdkError.LocationDisabled) {
                      Log.d("Position","4");
                      // Location is disabled, user have to turn it on
                  } else if (error instanceof IndoorwayLocationSdkError.UnableToFetchData) {
                      Log.d("Position","5");
                      // Network-related error, service will be restarted on network connection established
                  } else if (error instanceof IndoorwayLocationSdkError.NoRadioMaps) {
                      Log.d("Position","6");
                      // Measurements have to be taken in order to use location
                  }
              }
          };

        IndoorwayLocationSdk.instance()
                .state()
                .onError()
                .register(listenerw);
    }

    private void onChangeState(final IndoorwayMapView indoorwayMapView) {
        indoorwayMapView

                .setOnMapLoadCompletedListener(new Action1<IndoorwayMap>() {
                    @Override
                    public void onAction(IndoorwayMap indoorwayMap) {
                    if(listP!=null) {
                        for (Person person : listP) {
                            MarkersLayer myLayer = indoorwayMapView.getMarker().addLayer(4);
                            myLayer.add(
                                    new DrawableCircle(
                                            String.valueOf(new Random().nextInt()),
                                            1, // radius in meters, eg. 0.4f
                                            Color.RED, // circle background color, eg. Color.RED
                                            Color.BLUE, // color of outline, eg. Color.BLUE
                                            2, // width of outline in meters, eg. 0.1f
                                            person.getLocation().getCoordinates() // coordinates of circle center point
                                    )
                            );

                        }
                    }
                       if(isInRoadToOtherLevel)
                       {
                           indoorwayMapView.getNavigation().stop();
                           Navigate(road.getCoordinates(),road.getLevel());
                       }
                        List<IndoorwayNode> paths = indoorwayMap.getPaths();


                        List<IndoorwayObjectParameters> mapObjects = indoorwayMap.getObjects();
                        Log.d("fff","fdfdf");
                    }
                });
        indoorwayMapView

                .setOnMapLoadFailedListener(new Action0() {
                    @Override
                    public void onAction() {
                       Log.d("Map","error");
                    }
                });

        listenerq= new Action1<IndoorwayLocationSdkState>() {
             @Override
             public void onAction(IndoorwayLocationSdkState indoorwayLocationSdkState) {
              Log.d("Position",  indoorwayLocationSdkState.toString());

             }
         };


        IndoorwayLocationSdk.instance()
                .state()
                .onChange()
                .register(listenerq);
    }

    private void CheckPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        IndoorwayLocationSdk.instance()
                .position()
                .onChange()
                .unregister(listener)
                ;

        IndoorwayLocationSdk.instance()
                .state()
                .onError()
                .unregister(listenerw);
        IndoorwayLocationSdk.instance()
                 .state()
                .onChange()
                .unregister(listenerq);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
