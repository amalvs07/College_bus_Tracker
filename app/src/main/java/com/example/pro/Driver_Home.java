package com.example.pro;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.pro.Services.LocationService;
import com.example.pro.Services.Util;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Driver_Home extends AppCompatActivity  {
    TextView t1 ;
    SwitchCompat switchCompat;
    String setTextOn="Live Location is sharing";
    String setTextOff="Turn on to share location";
    public LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    String busNoDriver;
    SharedPreferences sh;

    private final String CHANNEL_ID = "simple_notification";
    private final int NOTIFICATION_ID = 01;

    FirebaseDatabase rootNode;
    DatabaseReference  reference,changeDriverstatus,driverdetails;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    private Handler mHandler = new Handler();
    volatile boolean toExit = false;
    String username;
    FloatingActionButton
     b1;
    String Bool="1";

    String keyyy;


    private static int MY_FINE_LOCATION_REQUEST = 99;
    private static int MY_BACKGROUND_LOCATION_REQUEST = 100;

    LocationService mLocationService = new LocationService();
    Intent mServiceIntent;

    Button startServiceBtn, stopServiceBtn;
    int kkk=1;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        //  sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //  username=sh.getString("username", "");
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String myStrValue = preferences.getString("username", "defaultStringIfNothingFound");
       // username = getIntent().getStringExtra("userName");
        username=myStrValue;

        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("location");
        changeDriverstatus = rootNode.getReference("busdriver");
        driverdetails = rootNode.getReference("drivers");

        driverdetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)) {
                    busNoDriver = snapshot.child(username).child("busno").getValue(String.class);
                    if (busNoDriver.isEmpty()) {
                        Toast.makeText(context, "Bus Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        b1 = findViewById(R.id.feedbutton);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Driver_Home.this, DriverFeedBack.class);
                i.putExtra("userName", username);
                i.putExtra("Checked", kkk);
                startActivity(i);
                // startActivity(new Intent(getApplicationContext(), DriverFeedBack.class));
            }
        });

        t1 = findViewById(R.id.textView4);
//        switchCompat = findViewById(R.id.SwitchShare);
//
//
//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (buttonView.isChecked()) {
//                    kkk="yes";
//                    t1.setText(setTextOn);
//                    //  mToastRunnable.run();
//                    changeDriverstatus.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.hasChild(busNoDriver)) {
//                                changeDriverstatus.child(busNoDriver).child("driver_online").setValue("1");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
////                      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, Driver_Home.this);
//                    if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//                            if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                                    != PackageManager.PERMISSION_GRANTED) {
//
//
//                                AlertDialog alertDialog = new AlertDialog.Builder(Driver_Home.this).create();
//                                alertDialog.setTitle("Background permission");
//                                alertDialog.setMessage(getString(R.string.background_location_permission_message));
//
//                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start service anyway",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                starServiceFunc();
//                                                dialog.dismiss();
//                                            }
//                                        });
//
//                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Grant background Permission",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                requestBackgroundLocationPermission();
//                                                dialog.dismiss();
//                                            }
//                                        });
//
//                                alertDialog.show();
//
//
//                            } else if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                                    == PackageManager.PERMISSION_GRANTED) {
//                                starServiceFunc();
//                            }
//                        } else {
//                            starServiceFunc();
//                        }
//
//                    } else if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(Driver_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//
//                            AlertDialog alertDialog = new AlertDialog.Builder(Driver_Home.this).create();
//                            alertDialog.setTitle("ACCESS_FINE_LOCATION");
//                            alertDialog.setMessage("Location permission required");
//
//                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            requestFineLocationPermission();
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//
//                            alertDialog.show();
//
//                        } else {
//                            requestFineLocationPermission();
//                        }
//                    }
//
//
//                } else {
//                    t1.setText(setTextOff);
//
//                    // mHandler.removeCallbacks(mToastRunnable);
//                    kkk="no";
//                    stopServiceFunc();
//                    changeDriverstatus.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.hasChild(busNoDriver)) {
//                                changeDriverstatus.child(busNoDriver).child("driver_online").setValue("0");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//        });
//        if ((getIntent().getStringExtra("Checked")).isEmpty()) {
//            Toast.makeText(context, "Something", Toast.LENGTH_SHORT).show();
//        }
//        else if ((getIntent().getStringExtra("Checked")).equals("yes")){
//            switchCompat.setChecked(true);
//        }
//        else {
//            switchCompat.setChecked(false);
//        }


        startServiceBtn = (Button) findViewById(R.id.start_service_btn);
        stopServiceBtn = (Button) findViewById(R.id.stop_service_btn);

        /**************/

        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {


                            AlertDialog alertDialog = new AlertDialog.Builder(Driver_Home.this).create();
                            alertDialog.setTitle("Background permission");
                            alertDialog.setMessage(getString(R.string.background_location_permission_message));

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start service anyway",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            starServiceFunc();

                                            dialog.dismiss();
                                        }
                                    });

                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Grant background Permission",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestBackgroundLocationPermission();
                                            dialog.dismiss();
                                        }
                                    });

                            alertDialog.show();


                        } else if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            starServiceFunc();

                        }
                    } else {
                        starServiceFunc();

                    }

                } else if (ActivityCompat.checkSelfPermission(Driver_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Driver_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)) {


                        AlertDialog alertDialog = new AlertDialog.Builder(Driver_Home.this).create();
                        alertDialog.setTitle("ACCESS_FINE_LOCATION");
                        alertDialog.setMessage("Location permission required");

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestFineLocationPermission();
                                        dialog.dismiss();
                                    }
                                });


                        alertDialog.show();

                    } else {
                        requestFineLocationPermission();
                    }
                }

            }
        });

        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopServiceFunc();

            }
        });


        /***********/
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            Toast.makeText(this, Integer.toString(requestCode), Toast.LENGTH_LONG).show();

            if ( requestCode == MY_FINE_LOCATION_REQUEST){

                if (grantResults.length !=0 /*grantResults.isNotEmpty()*/ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        requestBackgroundLocationPermission();
                    }

                } else {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION permission denied", Toast.LENGTH_LONG).show();
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                 /*   startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.getPackageName(), null),),);*/

                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:com.example.pro")
                        ));

                    }
                }
                return;

            }else if (requestCode == MY_BACKGROUND_LOCATION_REQUEST){

                if (grantResults.length!=0 /*grantResults.isNotEmpty()*/ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Background location Permission Granted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }



//    @Override
//    public void onLocationChanged(Location location) {
//       // Toast.makeText(Driver_Home.this, "Logitude"+location.getLongitude()+"   "+"Latitude"+location.getLatitude(), Toast.LENGTH_SHORT).show();
//
//
//
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild(username))
//                {
//                    reference.child(username).child("Longitude").setValue(location.getLongitude());
//                    reference.child(username).child("Latitude").setValue(location.getLatitude());
//
//                }
//                else {
//                    reference.child(username).child("Longitude").setValue(location.getLongitude());
//                    reference.child(username).child("Latitude").setValue(location.getLatitude());
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.d("Latitude","disable");
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        Log.d("Latitude","enable");
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.d("Latitude","status");
//    }




//    private Runnable mToastRunnable = new Runnable() {
//        @Override
//        public void run() {
//     //       Toast.makeText(Driver_Home.this, "Logitude"+LocationService.logi+"   "+"Latitude"+LocationService.lati, Toast.LENGTH_SHORT).show();
//            mHandler.postDelayed(this, 5000);
//        }
//    };
public void onBackPressed() {

    if (Util.isMyServiceRunning(mLocationService.getClass(), this)) {
        Toast.makeText(getApplicationContext(), "Please turn of your duty", Toast.LENGTH_SHORT).show();
    }
    else {
        selectImageOption();
    }

}


    private void selectImageOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Driver_Home.this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("isUserLogin");
                editor.commit();

                finish();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        }).setNegativeButton("No",null);
        builder.show();
    }

    private void starServiceFunc(){
        mLocationService = new LocationService();
        mServiceIntent = new Intent(this, mLocationService.getClass());
        mServiceIntent.putExtra("UserID", username);

        //  mToastRunnable.run();
        changeDriverstatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(busNoDriver)) {
                    changeDriverstatus.child(busNoDriver).child("driver_online").setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (!Util.isMyServiceRunning(mLocationService.getClass(), this)) {
            startService(mServiceIntent);
            t1.setText(setTextOn);
            Toast.makeText(this, getString(R.string.service_start_successfully), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.service_already_running), Toast.LENGTH_SHORT).show();
        }
        DisplayNotification();
    }

    private void stopServiceFunc(){
        mLocationService = new LocationService();
        mServiceIntent = new Intent(this, mLocationService.getClass());



        changeDriverstatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(busNoDriver)) {
                    changeDriverstatus.child(busNoDriver).child("driver_online").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (Util.isMyServiceRunning(mLocationService.getClass(), this)) {
            t1.setText(setTextOff);
            stopService(mServiceIntent);
            Toast.makeText(this, "Service stopped!!", Toast.LENGTH_SHORT).show();
            //saveLocation(); // explore it by your self
        } else {
            Toast.makeText(this, "Service is already stopped!!", Toast.LENGTH_SHORT).show();
        }
        endNotification();
    }

    private void requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                MY_BACKGROUND_LOCATION_REQUEST);
    }

    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, MY_FINE_LOCATION_REQUEST);
    }

//    public void saveLocation(){
//        File dir = new File(this.getFilesDir(), "trickyworld");
//        if(!dir.exists()){
//            dir.mkdir();
//        }
//
//        try {
//            File userLocation = new File(dir, "userlocation.txt");
//            FileWriter writer = new FileWriter(userLocation);
//            writer.append(LocationService.locationArrayList.toString());
//            writer.flush();
//            writer.close();
//            LocationService.locationArrayList.clear();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }


    public void DisplayNotification()
    {


        createNotificationChannell();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.location);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.phone));
        builder.setContentTitle("Sharing Driver Location");
        // builder.setContentText(location.getLatitude()+" , "+location.getLongitude());
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    //create notification channel if you target android 8.0 or higher version
    private void createNotificationChannell()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Simple Notification";
            String description = "Include all the simple notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    public void endNotification(){
        createNotificationChannell();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.location);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.phone));
        builder.setContentTitle("Sharing Driver Location");
        // builder.setContentText(location.getLatitude()+" , "+location.getLongitude());
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICATION_ID);

    }
}