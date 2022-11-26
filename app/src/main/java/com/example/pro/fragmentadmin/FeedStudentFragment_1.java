package com.example.pro.fragmentadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro.R;
import com.example.pro.fragmentadmin.Adapters.FragmentAdaperHome;
import com.example.pro.fragmentadmin.datas.Details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedStudentFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedStudentFragment_1 extends Fragment {

    View v;
    RecyclerView recyclerView;
    ImageView sd;

    DatabaseReference databaseReference;




    ArrayList<Details> arrayList;
    FragmentAdaperHome fragmentAdaperHome;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedStudentFragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedStudentFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedStudentFragment_1 newInstance(String param1, String param2) {
        FeedStudentFragment_1 fragment = new FeedStudentFragment_1();
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
        v= inflater.inflate(R.layout.fragment_feed_student_1, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.StudentFeedView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference= FirebaseDatabase.getInstance("https://bustracker-45930-default-rtdb.firebaseio.com/").getReference("feedback").child("student");
        arrayList =new ArrayList<>();

        recyclerView.setLayoutManager(new  LinearLayoutManager(getActivity()));

        fragmentAdaperHome=new FragmentAdaperHome(getContext(),arrayList);
        recyclerView.setAdapter(fragmentAdaperHome);
//        sd= (ImageView)v.findViewById(R.id.image_viewfeed);
//        sd.setImageResource(R.drawable.feedstudent);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Details driverDatas=dataSnapshot.getValue(Details.class);
                    arrayList.add(driverDatas);
                }
                fragmentAdaperHome.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}