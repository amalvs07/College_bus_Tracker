package com.example.pro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pro.bubblenavigation.BubbleNavigationLinearView;
import com.example.pro.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.example.pro.fragmentstudent.FeedFragmentStudent;
import com.example.pro.fragmentstudent.MapsFragmentStudent;
import com.example.pro.fragmentstudent.ProfileFragmentStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_Student extends AppCompatActivity {
    Fragment selectedFragment = null;
    private String myString ;
    SharedPreferences preferences;
    FirebaseDatabase rootNode;
    public static final String LOG_TAG = Home_Student.class.getSimpleName();
    DatabaseReference referenceloc,referenceStudent,referenceDriver,busdetails;
    String DriverUser,driverStatus,  busno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
       // myString=getIntent().getStringExtra("userName");

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String myStrValue = preferences.getString("username", "defaultStringIfNothingFound");
        // username = getIntent().getStringExtra("userName");
        myString=myStrValue;
//        Toast.makeText(this, myString, Toast.LENGTH_SHORT).show();
        Bundle b = new Bundle();
        b.putString("username", myString);


        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        referenceStudent = rootNode.getReference("student");

        referenceDriver = rootNode.getReference("busdriver");

        referenceloc = rootNode.getReference("location");

        busdetails = rootNode.getReference("bus");


        referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(myString)){
                     busno=snapshot.child(myString).child("busno").getValue(String.class);

                    if (!busno.isEmpty()){
                        b.putString("BusNo", busno);
                        busdetails.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String routname=snapshot.child(busno).child("routename").getValue(String.class);
                                    b.putString("routename", routname);

                                }
                                else {
                                    Toast.makeText(Home_Student.this, "Route not exists", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        referenceDriver.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(busno)){
                                    DriverUser=snapshot.child(busno).child("username").getValue(String.class);
                                    driverStatus=snapshot.child(busno).child("driver_online").getValue(String.class);
                                    b.putString("Driveruser", DriverUser);
                                    b.putString("Driverstatus", driverStatus);
                                    if (driverStatus.equals("1")){
                                        Log.e(LOG_TAG, "User Driver is  Online " + driverStatus+"busno :"+busno);
                                        //Toast.makeText(getActivity(), "User Driver is  Online", Toast.LENGTH_SHORT).show();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            showNotification();
                                        }

                                    }else{

                                        Log.e(LOG_TAG, "User Driver is not Online");
                                        // Toast.makeText(getActivity(), "User Driver is not Online", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Log.e(LOG_TAG, "Bus is not available sorry");
                                    //Toast.makeText(getActivity(), "Ops Sorry", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                        // Toast.makeText(getActivity(), "Bus not found", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Bus not found");
                    }

                }else{
                    // Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "User not found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


























        BubbleNavigationLinearView bubbleNavigation = findViewById(R.id.bubbleNavigationStudent);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ProfileFragmentStudent profileFragmentStudent=new ProfileFragmentStudent();
        profileFragmentStudent.setArguments(b);
        fragmentTransaction.replace(R.id.student_fragment_container,profileFragmentStudent).commit();



//        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
//                new ProfileFragmentStudent()).commit();

        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 0:
                        selectedFragment = new ProfileFragmentStudent();
                        break;
                    case 1:
                        selectedFragment = new MapsFragmentStudent();
                        break;
                    case 2:
                        selectedFragment = new FeedFragmentStudent();
                        break;

                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
//                        selectedFragment).commit();

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();

                selectedFragment.setArguments(b);
                fragmentTransaction1.replace(R.id.student_fragment_container,selectedFragment).commit();


            }
        });

    }

























    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification()
    {
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notifisong);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Home_Student. this, "default_notification_channel_id" )
                .setSmallIcon(R.drawable. ic_bts )
                .setContentTitle( "Bus Tracker" )
                .setSound(sound)
                .setContentText( "Your Driver is online" ) ;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE );
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( "NOTIFICATION_CHANNEL_ID" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            notificationChannel.setSound(sound , audioAttributes) ;
            mBuilder.setChannelId( "NOTIFICATION_CHANNEL_ID" ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                mBuilder.build()) ;
    }


    public void onBackPressed() {
        selectImageOption();
    }


    private void selectImageOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home_Student.this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("isStudentLogin");
                editor.commit();

                finish();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        }).setNegativeButton("No",null);
        builder.show();
    }
}