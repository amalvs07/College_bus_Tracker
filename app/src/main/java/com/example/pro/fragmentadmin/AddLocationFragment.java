package com.example.pro.fragmentadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pro.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLocationFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Double latitude,longitude;






    ArrayList<Double> cricketersList = new ArrayList<>();

    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;

    public AddLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddLocationFragment newInstance(String param1, String param2) {
        AddLocationFragment fragment = new AddLocationFragment();
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
        v= inflater.inflate(R.layout.fragment_add_location, container, false);
        layoutList = v.findViewById(R.id.ListLayout);
        buttonAdd = v.findViewById(R.id.AddEditTrxt);
        buttonAdd.setOnClickListener( this);

        buttonSubmitList=v.findViewById(R.id.AddLocations);

        buttonSubmitList.setOnClickListener(this);


        return  v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.AddEditTrxt:
                addView();
                break;
            case R.id.AddLocations:



//                if(checkIfValidAndRead()){
//
////                    Intent intent = new Intent(MainActivity.this,ActivityCricketers.class);
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("list",cricketersList);
////                    intent.putExtras(bundle);
//
//
//                }
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new AdminMapView());
                fr.commit();
                Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private boolean checkIfValidAndRead() {
        cricketersList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View cricketerView = layoutList.getChildAt(i);

            EditText editTextLatitude = (EditText)cricketerView.findViewById(R.id.latitude);
            EditText editTextLongitude = (EditText)cricketerView.findViewById(R.id.longitude);

//            Cricketer cricketer = new Cricketer();
//
//            if(!editTextLatitude.getText().toString().equals("")){
//                cricketer.setCricketerName(editTextName.getText().toString());
//            }else {
//                result = false;
//                break;
//            }
//
//
//
//            cricketersList.add(cricketer);

        }

        if(cricketersList.size()==0){
            result = false;
            Toast.makeText(getContext(), "Add Cricketers First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(getContext(), "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }


    private void addView() {
        View add=getLayoutInflater().inflate(R.layout.add_longi_latitude_input,null,false);
        EditText editTextLatitude = (EditText)add.findViewById(R.id.latitude);
        EditText editTextLongitude = (EditText)add.findViewById(R.id.longitude);

        ImageView imageClose = (ImageView)add.findViewById(R.id.image_remove);

        ImageView Addlocation = (ImageView)add.findViewById(R.id.AddLocatio);
        //new code
        Bundle bundle = getArguments();
//       String latitud = bundle.getString("latitude");
//
//      String  longitud = bundle.getString("longitude");
//        editTextLatitude.setText(latitud);
//        editTextLongitude.setText(longitud);
        Addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new AdminMapView());
                fr.commit();
            }
        });

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(add);
            }
        });
    layoutList.addView(add);
    }
    private void removeView(View view){

        layoutList.removeView(view);

    }
}