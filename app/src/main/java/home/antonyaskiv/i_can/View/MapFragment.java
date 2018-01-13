package home.antonyaskiv.i_can.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.indoorway.android.map.sdk.view.drawable.figures.DrawableText;
import com.indoorway.android.map.sdk.view.drawable.layers.MarkersLayer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import home.antonyaskiv.i_can.Model.Level;
import home.antonyaskiv.i_can.Model.Levels;
import home.antonyaskiv.i_can.Model.Location;
import home.antonyaskiv.i_can.Model.Road;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;
import home.antonyaskiv.i_can.R;


public class MapFragment extends Fragment {

    Action1<IndoorwayPosition> listener;
    Action1<IndoorwayLocationSdkState> listenerq;
    Action1<IndoorwayLocationSdkError> listenerw;
    @Inject
    ImplMapFragmentPresenter implMapFragmentPresenter;


    private OnFragmentInteractionListener mListener;
    private IndoorwayPosition currentPosition;
    private Levels levels;
    private Boolean isInRoadToOtherLevel =false;
     Road road;
    public MapFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        final IndoorwayMapView indoorwayMapView = view.findViewById(R.id.mapView);

       /* indoorwayMapView
                // perform map loading using building UUID and map UUID
                .load("CScrSxCVhQg", "3-_M01M3r5w");*/
        onChangeState(indoorwayMapView);
        onErrorState();
        onChangePosition(indoorwayMapView);
        MarkersLayer myLayer = indoorwayMapView.getMarker().addLayer(5);
        myLayer.add(new DrawableText("1",new Coordinates( 52.22201649388719, 21.00672578578200),"Text",2));
        Button btb=view.findViewById(R.id.navigat);
        road=new Road(  new Coordinates( 52.2221864, 21.0070076)
                ,
                new Level("7-QLYjkafkE",0,
                        new Location(
                                new Coordinates( 52.2222302, 21.0070076))));

        btb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigate(indoorwayMapView,road.getCoordinates(),road.getLevel()
                        );
            }
        });


        return view;
    }

    private void initListOfLevels() {
        levels=new Levels();
        List<Level> levelList=new ArrayList<>();
        levelList.add(new Level("7-QLYjkafkE",0,
                new Location(
                        new Coordinates( 52.22201649388719, 21.00672578578200))));
        levelList.add(new Level("gVI7XXuBFCQ",1,
                new Location(
                        new Coordinates( 52.22201649388719, 21.00672578578200))));
        levelList.add(new Level("3-_M01M3r5w",2,
                new Location(
                        new Coordinates( 52.22201649388719, 21.00672578578200))));
        levels.setAllLevels(levelList);
    }

    private Integer searchNumberOfLevel(String uuid,Levels levels)
{
 for (Level level:levels.getAllLevels())
 {
     if(level.getUUID().equals(uuid))
     {
         return level.getNumber();
     }
 }
 return -1;
}/*
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
    private void Navigate(IndoorwayMapView indoorwayMapView, Coordinates coordinates, Level level) {
        if( currentPosition.getMapUuid().equals(level.getUUID()))
        {
        indoorwayMapView.getNavigation()
                .start(currentPosition,level.getLocation().getCoordinates() );
        isInRoadToOtherLevel =false;

        }
        else
            {
                String direction;
                if(searchNumberOfLevel(currentPosition.getMapUuid(),levels)>level.getNumber())
                {
                    direction="Down";
                }
                else
                    {
                        direction="UP";
                    }
                MarkersLayer myLayer = indoorwayMapView.getMarker().addLayer(5);
                myLayer.add(new DrawableText("lfgr",level.getLocation().getCoordinates(),direction,8));

                indoorwayMapView.getNavigation()
                        .start(currentPosition,coordinates );
                isInRoadToOtherLevel =true;

            }
    }



    private void onChangePosition(final IndoorwayMapView indoorwayMapView) {
        listener = new Action1<IndoorwayPosition>() {
            @Override
            public void onAction(IndoorwayPosition position) {
                // store last position as a field
                currentPosition = position;
                Log.d("Position", String.valueOf(currentPosition.getCoordinates().getLatitude()));
                Log.d("Position", String.valueOf(currentPosition.getCoordinates().getLongitude()));


                Log.d("Position",currentPosition.getMapUuid().toString());
                indoorwayMapView.getPosition().setPosition(position, true);

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

                       if(isInRoadToOtherLevel)
                       {
                           indoorwayMapView.getNavigation().stop();
                           Navigate(indoorwayMapView,road.getCoordinates(),road.getLevel());
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
