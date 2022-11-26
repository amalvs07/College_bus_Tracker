package com.example.pro.fragmentadmin;

import android.os.Bundle;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SourceLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SourceLocationFragment extends Fragment {
    FirebaseDatabase rootNode;
    FloatingActionButton floatingActionButton;
    DatabaseReference referenceStudent,referenceLogin,referenceBus;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText editTextRouteid,editTextRoutename,editTextLatitude,editTextLongitude;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SourceLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SourceLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SourceLocationFragment newInstance(String param1, String param2) {
        SourceLocationFragment fragment = new SourceLocationFragment();
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
        v= inflater.inflate(R.layout.fragment_source_location, container, false);
         editTextRouteid=v.findViewById(R.id.editTextTextBusRouteId);
         editTextRoutename=v.findViewById(R.id.editTextTextRouteNAme);
         editTextLatitude=v.findViewById(R.id.editTextTextSourcelati);
         editTextLongitude=v.findViewById(R.id.editTextTextSourceLongi);

         floatingActionButton=v.findViewById(R.id.GoToAddLocation);

         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FragmentTransaction fr = getFragmentManager().beginTransaction();
                 fr.replace(R.id.fragment_container,new AdminMapView());
                 fr.commit();
             }
         });

        rootNode = FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/");
        referenceStudent = rootNode.getReference("sourcelocation");
        Bundle bundle = getArguments();
        String latitud = bundle.getString("latitude");
        String  longitud = bundle.getString("longitude");
        editTextLatitude.setText(latitud);
        editTextLongitude.setText(longitud);
        Button b1=v.findViewById(R.id.AddLocations);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeid =editTextRouteid.getText().toString().trim();

                String routename = editTextRoutename.getText().toString().trim();
                String latitude = editTextLatitude.getText().toString().trim();
                String longitude = editTextLongitude.getText().toString().trim();

                if(routeid.isEmpty() ||routename.isEmpty()||latitude.isEmpty()||longitude.isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(routename)){
                              //  Toast.makeText(getContext(), "already exists", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                referenceStudent.child(routename).child("routeid").setValue(routeid);
                                referenceStudent.child(routename).child("routename").setValue(routename);
                                referenceStudent.child(routename).child("latitude").setValue(Double.parseDouble(latitude));
                                referenceStudent.child(routename).child("longitude").setValue(Double.parseDouble(longitude));
                              //  Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.fragment_container,new AdminMapView());
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