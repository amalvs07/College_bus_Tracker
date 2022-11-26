package com.example.pro.fragmentadmin;

import static com.example.pro.R.id.editTextTextUsername;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pro.R;
import com.example.pro.customspinner.MaterialSpinner;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverFragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverFragmentAdd extends Fragment {
    EditText e1,e2,e3,e4,e5,e6,e7;
    Button b1;
    FirebaseDatabase rootNode;
    int login_id=0;
    DatabaseReference referenceStudent,referenceLogin,referenceDriver,referenceBus;
    String BusNOOO;
    ArrayList<String> friends = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    public String messageContent="Bus Tracker App:Your username and password will be first word from your email address.for example if ****@gmail.com is email then **** will be username and password.Make sure you login once.";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverFragmentAdd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverFragmentAdd.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverFragmentAdd newInstance(String param1, String param2) {
        DriverFragmentAdd fragment = new DriverFragmentAdd();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v= inflater.inflate(R.layout.fragment_driver_add, container, false);



        SendSms();

        e1 = v.findViewById(R.id.editTextTextPersonName);
        e2 = v.findViewById(R.id.editTextTextPersonEmail);
        e3 = v.findViewById(R.id.editTextTextPersonPhoneNo);
       // e4 = v.findViewById(R.id.editTextTextBusNo);
        e5 = v.findViewById(R.id.editTextTextPersonPassword);
        e6 = v.findViewById(R.id.editTextTextPersonConfirmPassword);
        e7=  v.findViewById(editTextTextUsername);
        b1 = v.findViewById(R.id.Register);
        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        referenceStudent = rootNode.getReference("drivers");
        referenceLogin = rootNode.getReference("login");
        referenceDriver = rootNode.getReference("busdriver");
        referenceBus = rootNode.getReference("bus");
        referenceBus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String friend = dataSnapshot.getKey();
                    friends.add(friend);

                }
                Log.d("TAG", String.valueOf(friends));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MaterialSpinner spinner = (MaterialSpinner) v.findViewById(R.id.State);


        spinner.setItems(friends);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                BusNOOO=item;
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                // Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =e1.getText().toString().trim();
              //  String Busno = e4.getText().toString().trim();
                String email = e2.getText().toString().trim();
                String phoneNo = e3.getText().toString().trim();
                String password = e5.getText().toString().trim();
                String cnpassword = e6.getText().toString().trim();

                String username = e7.getText().toString().trim();

                if(name.isEmpty() ||BusNOOO.isEmpty()||email.isEmpty()||phoneNo.isEmpty()||password.isEmpty()||cnpassword.isEmpty()||username.isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {


                    if (password.equals(cnpassword)) {

                        referenceLogin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(username))
                                {
                                    Toast.makeText(getContext(), "You have already Added", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //login insert
                                    referenceLogin.child(username).child("username").setValue(username);
                                    referenceLogin.child(username).child("password").setValue(password);
                                    referenceLogin.child(username).child("usertype").setValue("drivers");
                                    referenceLogin.child(username).child("status").setValue("1");

                                    referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(username))
                                            {
                                                Toast.makeText(getContext(), "You have already Added tooo", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                // student insert

                                                referenceStudent.child(username).child("fullname").setValue(name);
                                                referenceStudent.child(username).child("username").setValue(username);
                                                referenceStudent.child(username).child("email").setValue(email);
                                                referenceStudent.child(username).child("busno").setValue(BusNOOO);
                                                referenceStudent.child(username).child("phoneno").setValue(phoneNo);
//                                                referenceStudent.child(username).child("driver_online").setValue("0");
                                                referenceDriver.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        referenceDriver.child(BusNOOO).child("username").setValue(username);
                                                        referenceDriver.child(BusNOOO).child("driver_online").setValue("0");



//                                                        SendSms sendSMS = new SendSms(phoneNo, messageContent);
//                                                        execute(sendSMS);





                                                        //use the sms manager to send message
                                                        SmsManager sm=SmsManager.getDefault();
                                                        sm.sendTextMessage(phoneNo, null, messageContent, null, null);

                                                        Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                                                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                                                        fr.replace(R.id.fragment_container,new DriverFragment());
                                                        fr.commit();
                                                    }




                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });




                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });






                    }
                    else {
                        Toast.makeText(getContext(), "Password is not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return v;

    }

    private void SendSms() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    public void execute(Runnable runnable) {
        new Thread(runnable).start();
    }

}