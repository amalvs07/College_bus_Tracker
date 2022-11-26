package com.example.pro.fragmentstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pro.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragmentStudent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragmentStudent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
View v;
FirebaseDatabase rootNode;
DatabaseReference reference;
    int id=1;
Button b1;
EditText e1;
    String myValue="123";
    public FeedFragmentStudent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragmentStudent.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragmentStudent newInstance(String param1, String param2) {
        FeedFragmentStudent fragment = new FeedFragmentStudent();
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



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_feed_student, container, false);
        e1=v.findViewById(R.id.editTextTextPersonName2);
        b1=v.findViewById(R.id.Sendfeed);

        Bundle bundle = getArguments();
        String myValue = bundle.getString("username");

        reference = rootNode.getReference("feedback");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=e1.getText().toString();
                if (data.isEmpty()){
                    Toast.makeText(getContext(), "Please Fill Field", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    orderByKey().limitToLast(1)
                    reference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String va = String.valueOf(id);
                            if (snapshot.hasChildren()) {

//                                final String pass = snapshot.child(myValue).child(va).getKey();
//                                if (pass.isEmpty()) {
//                                    Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
//                                } else {


//                                    id = Integer.parseInt(pass) + 1;
//                                    va = String.valueOf(id);
                                reference.child("student").child(myValue).child("username").setValue(myValue);
                                    reference.child("student").child(myValue).child("message").setValue(data);
                                    Toast.makeText(getContext(), "send success", Toast.LENGTH_SHORT).show();
                                    e1.setText("");
//                                }
                            }
                            else {
//                                va = String.valueOf(id);
                                reference.child("student").child(myValue).child("username").setValue(myValue);
                                reference.child("student").child(myValue).child("message").setValue(data);
                                Toast.makeText(getContext(), "send success", Toast.LENGTH_SHORT).show();
                                e1.setText("");
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });
        return  v;
    }
}