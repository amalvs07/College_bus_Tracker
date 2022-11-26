package com.example.pro.fragmentstudent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.pro.Home_Student;
import com.example.pro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragmentStudent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragmentStudent extends Fragment {
    public static final String LOG_TAG = Home_Student.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference referenceLogin;
    FirebaseDatabase rootNode;
    TextView t1,t2,t3,t4;
    View v;
    String myValue="123",DriverStatus;
    DatabaseReference referenceDriverStatus;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bundle bundle ;
    String BusNO ;
    public ProfileFragmentStudent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragmentStudent.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragmentStudent newInstance(String param1, String param2) {
        ProfileFragmentStudent fragment = new ProfileFragmentStudent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");

        referenceLogin = rootNode.getReference("student");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_profile_student, container, false);

        t1=v.findViewById(R.id.Personame);
        t2=v.findViewById(R.id.email);
        t3=v.findViewById(R.id.phone);
        t4=v.findViewById(R.id.Busno);
         bundle = getArguments();
        myValue = bundle.getString("username");
        BusNO=bundle.getString("BusNo");
        referenceLogin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(myValue)){
                    final String name=snapshot.child(myValue).child("fullname").getValue(String.class);
                    final String email=snapshot.child(myValue).child("email").getValue(String.class);
                    final String phno=snapshot.child(myValue).child("phoneno").getValue(String.class);
                    final String busno=snapshot.child(myValue).child("busno").getValue(String.class);
                    t1.setText(name);
                    t2.setText(email);
                    t3.setText(phno);
                    t4.setText(busno);

                }
                else {
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        referenceDriverStatus = rootNode.getReference("busdriver");
//        referenceDriverStatus.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    DriverStatus = snapshot.child(BusNO).child("driver_online").getValue(String.class);
//                    if (DriverStatus.isEmpty()) {
//                        Log.e(LOG_TAG, "User Driver is  Empty " );
//                    } else {
//                        if (DriverStatus.equals("1")) {
//                            Log.e(LOG_TAG, "User Driver is  Online " + DriverStatus + "busno :" + BusNO);
//                            //Toast.makeText(getActivity(), "User Driver is  Online", Toast.LENGTH_SHORT).show();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                showNotification();
//                            }
//
//                        } else {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                showNotificationOff();
//                            }
//                            Log.e(LOG_TAG, "User Driver is not Online");
//                            // Toast.makeText(getActivity(), "User Driver is not Online", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification()
    {
        Uri sound = Uri.parse("android.resource://" + getActivity().getApplicationContext().getPackageName() + "/" + R.raw.notifisong);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "default_notification_channel_id" )
                .setSmallIcon(R.drawable. ic_bts )
                .setContentTitle( "Bus Tracker" )
                .setSound(sound)
                .setContentText( "Your Driver is online" ) ;
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context. NOTIFICATION_SERVICE );
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotificationOff()
    {
        Uri sound = Uri.parse("android.resource://" + getActivity().getApplicationContext().getPackageName() + "/" + R.raw.notifisong);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "default_notification_channel_id" )
                .setSmallIcon(R.drawable. ic_bts )
                .setContentTitle( "Bus Tracker" )
                .setSound(sound)
                .setContentText( "Your Driver is offline" ) ;
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context. NOTIFICATION_SERVICE );
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
}