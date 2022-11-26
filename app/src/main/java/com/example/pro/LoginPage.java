package com.example.pro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {
EditText username,password;
Button b1;
    FirebaseDatabase rootNode;
    int login_id=0;
    SharedPreferences sh;
    DatabaseReference referenceLogin;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
         preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        b1=findViewById(R.id.button);
        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");

        referenceLogin = rootNode.getReference("login");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEnteredUsername = username.getText().toString().trim();
                final String userEnteredPassword = password.getText().toString().trim();


                if (userEnteredUsername.isEmpty()||userEnteredPassword.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    referenceLogin.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(userEnteredUsername))
                            {

                                final String pass=snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                                if (pass.equals(userEnteredPassword))
                                {
                                    SharedPreferences.Editor ed = sh.edit();
                                    ed.putString("username", userEnteredUsername);
                                    ed.commit();
                                    final String usertype=snapshot.child(userEnteredUsername).child("usertype").getValue(String.class);
                                    final String userstatus=snapshot.child(userEnteredUsername).child("status").getValue(String.class);

                                    if (usertype.equals("student"))
                                    {
                                        if (userstatus.equals("1")) {
                                            Toast.makeText(LoginPage.this, "Student Login Successfully", Toast.LENGTH_SHORT).show();

                                            //   startActivity(new Intent(getApplicationContext(), Driver_Home.class));
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putBoolean("isStudentLogin", true);
                                            editor.putString("username", userEnteredUsername);
                                            editor.commit();
                                            Intent i = new Intent(LoginPage.this, Home_Student.class);
                                            i.putExtra("userName", userEnteredUsername);
                                            startActivity(i);
                                        }else {
                                            Toast.makeText(LoginPage.this, "Permission denied by admin", Toast.LENGTH_SHORT).show();
                                        }

                                    }else if (usertype.equals("drivers")){
                                        if (userstatus.equals("1")) {

                                            Toast.makeText(LoginPage.this, "Driver Login Successfully", Toast.LENGTH_SHORT).show();
                                            // startActivity(new Intent(getApplicationContext(), Driver_Home.class));

                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putBoolean("isUserLogin", true);
                                            editor.putString("username", userEnteredUsername);
                                            editor.commit();
                                            Intent i = new Intent(LoginPage.this, Driver_Home.class);
                                            i.putExtra("userName", userEnteredUsername);
                                            startActivity(i);
                                        }else {
                                            Toast.makeText(LoginPage.this, "Permission denied by admin", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if (usertype.equals("admin")){
                                        Toast.makeText(LoginPage.this, "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean("isAdminLogin", true);
                                      //  editor.putString("username",userEnteredUsername);
                                        editor.commit();
                                        startActivity(new Intent(getApplicationContext(), Admin_Home.class));
                                    }
                                    else {
                                        Toast.makeText(LoginPage.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                    }


                                }else {
                                    Toast.makeText(LoginPage.this, "Password entered incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginPage.this, "User name does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
