package com.example.pro;

import android.content.Intent;
import android.os.Bundle;
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

public class DriverFeedBack extends AppCompatActivity {

    EditText e1;
    Button b1;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String username,bool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_feed_back);
        e1=findViewById(R.id.editTextTextPersonName2);
        b1=findViewById(R.id.Sendfeed);
        username=getIntent().getStringExtra("userName");
        bool=getIntent().getStringExtra("Checked");

        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("feedback");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feed =e1.getText().toString();
                if (feed.isEmpty()){
                    Toast.makeText(DriverFeedBack.this, "Please fill field", Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child("driver").child(username).child("username").setValue(username);
                            reference.child("driver").child(username).child("message").setValue(feed);
                            Toast.makeText(DriverFeedBack.this, "Feedback Send Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(DriverFeedBack.this, Driver_Home.class);
                            i.putExtra("userName", username);
                            i.putExtra("Checked", bool);
                            startActivity(i);
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
        Intent i = new Intent(DriverFeedBack.this, Driver_Home.class);
        i.putExtra("userName", username);
        i.putExtra("Checked", bool);
        startActivity(i);
    }
}