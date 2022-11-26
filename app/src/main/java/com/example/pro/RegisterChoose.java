package com.example.pro;

import static com.example.pro.R.id.editTextTextUsername;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pro.customspinner.MaterialSpinner;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterChoose extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5,e6,e7;
    Button b1;
    FirebaseDatabase rootNode;
    int login_id=0;
    String BusNOOO;
    DatabaseReference referenceStudent,referenceLogin,referenceBus;

    ArrayList<String>  friends = new ArrayList<>();
    private static  String[] ANDROID_VERSIONS = {
            "Cupcake",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow",
            "Nougat",
            "Oreo"
    };

     String[] strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choose);
        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        referenceStudent = rootNode.getReference("student");
        referenceLogin = rootNode.getReference("login");
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

        e1 = findViewById(R.id.editTextTextPersonName);
        e2 = findViewById(R.id.editTextTextPersonEmail);
        e3 = findViewById(R.id.editTextTextPersonPhoneNo);
     //   e4 = findViewById(R.id.editTextTextBusNo);
        e5 = findViewById(R.id.editTextTextPersonPassword);
        e6 = findViewById(R.id.editTextTextPersonConfirmPassword);
        e7=  findViewById(editTextTextUsername);
        b1 = findViewById(R.id.Register);

        //State details Spinner


         strs = new String[friends.size()];

        for (int i = 0; i < friends.size(); i++) {
            strs[i] = friends.get(i);
        }
        for (String k : strs) {

                    Log.d("Strings And datas", k);
        }

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.State);


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
                    Toast.makeText(RegisterChoose.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {


                    if (password.equals(cnpassword)) {

                        referenceLogin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(username))
                                {
                                    Toast.makeText(RegisterChoose.this, "You have already registered", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //login insert
                                    referenceLogin.child(username).child("username").setValue(username);
                                    referenceLogin.child(username).child("password").setValue(password);
                                    referenceLogin.child(username).child("usertype").setValue("student");
                                    referenceLogin.child(username).child("status").setValue("1");

                                    referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(username))
                                            {
                                                Toast.makeText(RegisterChoose.this, "You have already registered tooo", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                // student insert

                                                referenceStudent.child(username).child("fullname").setValue(name);
                                                referenceStudent.child(username).child("username").setValue(username);
                                                referenceStudent.child(username).child("email").setValue(email);
                                                referenceStudent.child(username).child("busno").setValue(BusNOOO);
                                                 referenceStudent.child(username).child("phoneno").setValue(phoneNo);

                                                Toast.makeText(RegisterChoose.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), LoginPage.class));
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
                        Toast.makeText(RegisterChoose.this, "Password is not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginOrSignUp.class));
    }

}