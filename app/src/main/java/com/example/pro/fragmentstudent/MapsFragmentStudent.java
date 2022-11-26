package com.example.pro.fragmentstudent;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pro.DatabaseHelper;
import com.example.pro.Home_Student;
import com.example.pro.R;
import com.example.pro.databinding.ActivityMapsBinding;
import com.example.pro.polylinemapdraw.DirectionPointListener;
import com.example.pro.polylinemapdraw.GetPathFromLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsFragmentStudent extends Fragment {
    private static final int REQUEST_LOCATION = 1;
    private ActivityMapsBinding binding;
    double lat = 0;
    public static final String LOG_TAG = Home_Student.class.getSimpleName();
    double lon = 0;
    private GoogleMap mMap;
    public LatLng locApp;
   // FirebaseDatabase rootNode;
    DatabaseReference referenceloc,referenceStudent,referenceDriver,SourceLoac;
    private Marker mBusMarker;

    Bundle bundle ;
    String myValue ;
    String DriverUser,driverStatus,busNo,Routenameeee;

    double sourcelocationlatitude;
    double sourcelocationlongitude;

    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseDatabase database;      // used for store URLs of uploaded files
    String licence_no,email,phone,name;
    DatabaseHelper databaseHelper;
    Location location;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
    LocationCallback locationCallback;
    LocationManager locationManager;
    String latitude, longitude;
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
        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v;
        v= inflater.inflate(R.layout.fragment_maps_student, container, false);


         bundle   = getArguments();
        myValue = bundle.getString("username");

        DriverUser = bundle.getString("Driveruser");

        driverStatus = bundle.getString("Driverstatus");

        Log.e(LOG_TAG, "Driver user"+DriverUser+"status"+driverStatus);


        referenceStudent = rootNode.getReference("student");

        referenceDriver = rootNode.getReference("busdriver");

        referenceloc = rootNode.getReference("location");

        try{


            busNo = bundle.getString("BusNo");

            Routenameeee = bundle.getString("routename");
            SourceLoac = rootNode.getReference("sourcelocation");
            SourceLoac.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Routenameeee)){
                        sourcelocationlatitude = snapshot.child(Routenameeee).child("latitude").getValue(Double.class);
                        sourcelocationlongitude = snapshot.child(Routenameeee).child("longitude").getValue(Double.class);
                        LatLng source = new LatLng(sourcelocationlatitude, sourcelocationlongitude);
                        mMap.addMarker(new MarkerOptions().position(source).title("Source:"+Routenameeee));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(source));


                        LatLng destination = new LatLng(10.100392152628336, 76.35704543441534);
                        mMap.addMarker(new MarkerOptions().position(destination).title("Destination:FISAT"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));

                        PolyAddLinesMap(source,destination);
                    }
                    else {
                        Log.e(LOG_TAG, "invalid routename");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }catch (Exception e){
            Log.e(LOG_TAG, String.valueOf(e));
        }
//        referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild(myValue)){
//                    String busno=snapshot.child(myValue).child("busno").getValue(String.class);
//                    if (!busno.isEmpty()){
//                        referenceDriver.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.hasChild(busno)){
//                                    DriverUser=snapshot.child(busno).child("username").getValue(String.class);
//                                    driverStatus=snapshot.child(busno).child("driver_online").getValue(String.class);
//                                    if (driverStatus.equals("1")){
//                                        Log.e(LOG_TAG, "User Driver is  Online " + driverStatus+"busno :"+busno);
//                                        //Toast.makeText(getActivity(), "User Driver is  Online", Toast.LENGTH_SHORT).show();
//
//
//                                    }else{
//
//                                        Log.e(LOG_TAG, "User Driver is not Online");
//                                        // Toast.makeText(getActivity(), "User Driver is not Online", Toast.LENGTH_SHORT).show();
//                                    }
//                                }else{
//                                    Log.e(LOG_TAG, "Bus is not available sorry");
//                                    //Toast.makeText(getActivity(), "Ops Sorry", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }else {
//                        // Toast.makeText(getActivity(), "Bus not found", Toast.LENGTH_SHORT).show();
//                        Log.e(LOG_TAG, "Bus not found");
//                    }
//
//                }else{
//                    // Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
//                    Log.e(LOG_TAG, "User not found");
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setMaxWaitTime(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

      //  rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");



       // referenceloc = rootNode.getReference("location");

//        referenceloc.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                if (snapshot2.hasChild("arun")){
//                    Double latitude   =snapshot2.child("arun").child("Latitude").getValue(Double.class);
//                    Double longitude   =snapshot2.child("arun").child("Longitude").getValue(Double.class);
//
////                    if (latitude.isEmpty()||longitude.isEmpty()){
////                        Toast.makeText(getContext(), "coordinates not exist", Toast.LENGTH_SHORT).show();
////                    }else {
////                        lat = Double.parseDouble(latitude.toString());
////                        lon = Double.parseDouble(longitude.toString());
//                    lat=latitude;
//                    lon=longitude;
//                    LatLng busLocation = new LatLng(lat, lon);
//                    if (mBusMarker != null) mBusMarker.remove();
//                    mBusMarker = mMap.addMarker(new MarkerOptions().position(busLocation).title("Your bus here").icon(BitmapFromVector(getActivity(),R.drawable.busmarkerrrr)));
//                    //   mMap.moveCamera(CameraUpdateFactory.newLatLng(busLocation));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 16));
//
//
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error2) {
//
//            }
//        });



        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                location = locationResult.getLastLocation();


//                Toast.makeText(getApplicationContext(),
//                        "Lat: "+Double.toString(location.getLatitude()) + '\n' +
//                                "Long: " + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();

                //locationArrayList.add(new LatLng(location.getLatitude(), location.getLongitude()));

               /* for (Location location : locationResult.getLocations()) {
                  location
                }*/
//                if (driverStatus.equals("1")) {


                    referenceloc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(DriverUser)) {
                                Double latitude = snapshot.child(DriverUser).child("Latitude").getValue(Double.class);
                                Double longitude = snapshot.child(DriverUser).child("Longitude").getValue(Double.class);
                                lat = latitude;
                                lon = longitude;
                                Log.e(LOG_TAG, "Longtitude" + lon + "latitude :" + lat);
                                LatLng busLocation = new LatLng(lat, lon);
                                if (mBusMarker != null) mBusMarker.remove();
                                mBusMarker = mMap.addMarker(new MarkerOptions().position(busLocation).title("Your bus here").icon(BitmapFromVector(getActivity(), R.drawable.marker_bus)));
                                //   mMap.moveCamera(CameraUpdateFactory.newLatLng(busLocation));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15));

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                } else {
//                    Toast.makeText(getActivity(), "Driver is not online", Toast.LENGTH_SHORT).show();
//                    lat = 10.4534567;
//                    lon = 76.3786532;
//                    Log.e(LOG_TAG, "Longtitude" + lon + "latitude :" + lat);
//                    LatLng busLocation = new LatLng(lat, lon);
//                    if (mBusMarker != null) mBusMarker.remove();
//                    mBusMarker = mMap.addMarker(new MarkerOptions().position(busLocation).title("Your current location").icon(BitmapFromVector(getActivity(), R.drawable.iconsstudent)));
//                    //   mMap.moveCamera(CameraUpdateFactory.newLatLng(busLocation));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 18));
//                }
            }

        };
        startLocationUpdates();
        return  v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

//    private void OnGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }


    //Custom marker
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

//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (locationGPS != null) {
//                double lat = locationGPS.getLatitude();
//                double longi = locationGPS.getLongitude();
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
//
//            } else {
//                Toast.makeText(getContext(), "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }



    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                Looper.getMainLooper());


    }


    private void PolyAddLinesMap(LatLng source, LatLng destination) {
        new GetPathFromLocation(source, destination, new DirectionPointListener() {
            @Override
            public void onPath(PolylineOptions polyLine) {
                mMap.addPolyline(polyLine);
            }
        }).execute();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }
}