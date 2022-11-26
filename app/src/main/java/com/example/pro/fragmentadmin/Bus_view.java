package com.example.pro.fragmentadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro.R;
import com.example.pro.fragmentadmin.Adapters.DemoAdapter;
import com.example.pro.fragmentadmin.datas.DemoData;
import com.example.pro.fragmentadmin.datas.DriverData;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Bus_view#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bus_view extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    RecyclerView recyclerView;
    ArrayList<DriverData> listCont;
    DatabaseReference databaseReference;
    FloatingActionButton floatingActionButton;


    ArrayList<DemoData> demoData;
    DemoAdapter demoAdapter;
    public Bus_view() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bus_view.
     */
    // TODO: Rename and change types and number of parameters
    public static Bus_view newInstance(String param1, String param2) {
        Bus_view fragment = new Bus_view();
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
        v= inflater.inflate(R.layout.fragment_bus_view, container, false);
        floatingActionButton = v.findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Bus_add());
                fr.commit();
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.BusView);
        recyclerView.setLayoutManager(new  LinearLayoutManager(getActivity()));

//        FirebaseRecyclerOptions<BusData> options = new FirebaseRecyclerOptions.Builder<BusData>()
//                        .setQuery(FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/").getReference("bus").child("bus"), BusData.class)
//                        .build();
//        busAdapter=new BusAdapter(options);
//        recyclerView.setAdapter(busAdapter);

        databaseReference= FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/").getReference("bus");
        demoData =new ArrayList<>();

        recyclerView.setLayoutManager(new  LinearLayoutManager(getActivity()));

        demoAdapter=new DemoAdapter(getContext(),demoData);

        recyclerView.setAdapter(demoAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DemoData demo=dataSnapshot.getValue(DemoData.class);
                    demoData.add(demo);
                }
                demoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
//    public void onStart () {
//
//        super.onStart();
//        busAdapter.startListening();
//    }
//    public void onStop () {
//
//        super.onStop();
//        busAdapter.stopListening();
//    }
}