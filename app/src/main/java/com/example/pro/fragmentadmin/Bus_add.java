package com.example.pro.fragmentadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
 * Use the {@link Bus_add#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bus_add extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String Routename;
    EditText e1,e2,e3,e4;
    Button b1;
    ArrayList<String> friends = new ArrayList<>();
    public Bus_add() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bus_add.
     */
    // TODO: Rename and change types and number of parameters
    public static Bus_add newInstance(String param1, String param2) {
        Bus_add fragment = new Bus_add();
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
        v =inflater.inflate(R.layout.fragment_bus_add, container, false);
         e1=v.findViewById(R.id.editTextTextBusNOO);
        e2=v.findViewById(R.id.editTextTextBUsPlate);
//        e3=v.findViewById(R.id.editTextTextBusRouteId);
//        e4=v.findViewById(R.id.editTextTextRouteNAme);
        b1=v.findViewById(R.id.AddBuss);
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        DatabaseReference reference = rootNode.getReference("bus");


       DatabaseReference referenceRoute = rootNode.getReference("sourcelocation");
        referenceRoute.addListenerForSingleValueEvent(new ValueEventListener() {
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

        MaterialSpinner spinner = (MaterialSpinner) v.findViewById(R.id.Routeidid);


        spinner.setItems(friends);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                Routename=item;
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
                String busno =e1.getText().toString().trim();
                String busplate = e2.getText().toString().trim();
//                String busrouteid = e3.getText().toString().trim();
//                String busroutename = e4.getText().toString().trim();
                if(busno.isEmpty() ||busplate.isEmpty()||Routename.isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(busno))
                            {
                                Toast.makeText(getContext(), "You have already  Added Bus", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                reference.child(busno).child("busno").setValue(busno);
                                reference.child(busno).child("busplate").setValue(busplate);
                               // reference.child(busno).child("routeid").setValue(busrouteid);
                                reference.child(busno).child("routename").setValue(Routename);
                                Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.fragment_container,new Bus_view());
                                fr.commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        return v;
    }

}