package com.example.pro.fragmentadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro.R;
import com.example.pro.fragmentadmin.Adapters.StudentAdapter;
import com.example.pro.fragmentadmin.datas.Details;
import com.example.pro.fragmentadmin.datas.DriverData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    RecyclerView recyclerView;
    List<Details> listCont;
    DatabaseReference databaseReference;




    ArrayList<DriverData> stutData;
    StudentAdapter studentAdapter;
    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        listCont = new ArrayList<>();


//        listCont.add(new Details("Mahadi Hasan","01717677540",R.drawable.studentemoji));
//        listCont.add(new Details("Mahadi Hasan","01717677540",R.drawable.studentemoji));
//        listCont.add(new Details("Mahadi Hasan","01717677540",R.drawable.studentemoji));
//        listCont.add(new Details("Mahadi Hasan","01717677540",R.drawable.studentemoji));
//        listCont.add(new Details("Mahadi Hasan","01717677540",R.drawable.studentemoji));
//        listCont.add(new Details("Mahadi Hasan","01717677540",R.drawable.studentemoji));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_student, container, false);

        v= inflater.inflate(R.layout.fragment_student, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.contact_recycleView);
//        FragmentAdaperHome viewAdapter = new FragmentAdaperHome(getContext(), listCont);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(viewAdapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference= FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/").getReference("student");
        stutData =new ArrayList<>();

        recyclerView.setLayoutManager(new  LinearLayoutManager(getActivity()));

        studentAdapter=new StudentAdapter(getContext(),stutData);

        recyclerView.setAdapter(studentAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DriverData driverDatas=dataSnapshot.getValue(DriverData.class);
                    stutData.add(driverDatas);
                }
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}