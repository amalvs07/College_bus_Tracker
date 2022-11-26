package com.example.pro.fragmentadmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.example.pro.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdminMapView extends Fragment implements RoutingListener {
    // Google Map
   //  GoogleMap mMap;
    // latitude and longitude
    double latitude;
    double longitude;
    String newtime;
    DatabaseReference reference;
    ArrayList<LatLng> points;

    Marker mMarker;

LocationManager manager;
private final int MIN_TIME=1000;
    private final int MIN_DISTANCE=1;
    FirebaseDatabase rootNode;
    DatabaseReference referenceloc,referenceStudent,referenceDriver;


FloatingActionButton floatingActionButton;

    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;

    private static final String TAG = "xxx";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCAL_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionsGranted = false;
    final Marker[] marker = {null};
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap=googleMap;
            LatLng sydney = new LatLng(-34, 151);
          mMarker=  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                    latitude=latLng.latitude;
                    longitude=latLng.longitude;
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                    mMap.addMarker(markerOptions);

                }
            });

            if (mLocationPermissionsGranted) {
               // getDeviceLocation();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                subscribeToUpdates();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
           // subscribeToUpdates();
        }

        private void subscribeToUpdates() {
            DatabaseReference reference=FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/").getReference("location");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    setMarker(snapshot);
                }



                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    setMarker(snapshot);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Failed to read value.", error.toException());
                }


            });
        }
    };
    private void setMarker(DataSnapshot snapshot) {
        if (snapshot.hasChild("arun")){
            Double latitude = snapshot.child("arun").child("Latitude").getValue(Double.class);
            Double longitude = snapshot.child("arun").child("Longitude").getValue(Double.class);

            Log.d(TAG, latitude+"getLocationPermission: getting location permissions"+longitude);
            Toast.makeText(getContext(), latitude+""+longitude, Toast.LENGTH_SHORT).show();


            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapFromVector(getActivity(), R.drawable.busmarkerrrr));

            mMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }
    }
//    private void getDeviceLocation() {
//        Log.d(TAG, "getDeviceLocation: getting the device location ");
//        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//       // final String tracking = getIntent().getStringExtra("TRACKING");
//
//        final String[] courierID = {""};
//
//        //TODO fix fucking code madafaka
//        final DatabaseReference DB = FirebaseDatabase.getInstance().getReference();
//
//
//
//
//
//    }
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);



        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v;
        v= inflater.inflate(R.layout.fragment_admin_map_view, container, false);




      //  getLocationPermission();


      //  mMap.getUiSettings().setZoomControlsEnabled(true);
    //    mMap.setMyLocationEnabled(true);

        SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.US);
        newtime = sdfDateTime.format(new Date(System.currentTimeMillis()));
        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");

        referenceStudent = rootNode.getReference("student");

        referenceDriver = rootNode.getReference("busdriver");

        referenceloc = rootNode.getReference("location");

        floatingActionButton=v.findViewById(R.id.floating);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new AddLocationFragment());
                fr.commit();

                Bundle b = new Bundle();
                b.putString("latitude", String.valueOf(latitude));
                b.putString("longitude", String.valueOf(longitude));



                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SourceLocationFragment addLocationFragment=new SourceLocationFragment();
                addLocationFragment.setArguments(b);

                fragmentTransaction.replace(R.id.fragment_container,addLocationFragment).commit();
            }
        });

        manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            checkLocationPermission();
//        }
        return v;
    }

//    private void initMap() {
//    }

    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity()
                    , COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;

//                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCAL_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCAL_PERMISSION_REQUEST_CODE);
        }

    }

    private void readChangesLocation() {

        referenceloc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("arun")){
                    Double latitude = snapshot.child("arun").child("Latitude").getValue(Double.class);
                    Double longitude = snapshot.child("arun").child("Longitude").getValue(Double.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void moveCamera(LatLng coordination, float zoom) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordination, zoom);
        mMap.animateCamera(location);
    }

    private void setMarker(MarkerOptions m) {
        marker[0] = mMap.addMarker(m);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      //  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());




        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }





    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION)
        {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // permission was granted, yay! Do the location-related task you need to do.
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    //   mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                    mMap.setMyLocationEnabled(true);
                    Toast.makeText(getContext(), "permission granted", Toast.LENGTH_LONG).show();

                }

            }
            else
            {
                // permission denied, boo! Disable the functionality that depends on this permission.
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
            }

            // other 'case' lines to check for other permissions this app might request
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }
}