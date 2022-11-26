package com.example.pro.fragmentadmin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragmentdetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragmentdetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference referenceLogin,Driverdelete,StudentStatus;
    FirebaseDatabase rootNode;
    TextView t1,t2,t3,t4;
    View v;
    ImageView deleteStudent,RevokedImage;
    String myValue="123";

    public StudentFragmentdetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragmentdetails.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragmentdetails newInstance(String param1, String param2) {
        StudentFragmentdetails fragment = new StudentFragmentdetails();
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

        referenceLogin = rootNode.getReference("student");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_student_fragmentdetails, container, false);

        t1 = v.findViewById(R.id.fullNO);
        t2 = v.findViewById(R.id.editTextTextPersonEmail);
        t3 = v.findViewById(R.id.PhoneNo);
        t4 = v.findViewById(R.id.BusNOOO);
        Bundle bundle = getArguments();
        myValue = bundle.getString("username");


        deleteStudent=v.findViewById(R.id.DeleteId);

        RevokedImage=v.findViewById(R.id.Revoked);
        StudentStatus= rootNode.getReference("login");
        StudentStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(myValue)){

                    String Studestatus=snapshot.child(myValue).child("status").getValue(String.class);
                    if (Studestatus.equals("1")){
                        RevokedImage.setVisibility(View.GONE);
                        deleteStudent.setVisibility(View.VISIBLE);
                    }else{
                        RevokedImage.setVisibility(View.VISIBLE);
                        deleteStudent.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDataStudent();
            }
        });















        if (myValue.isEmpty()) {
            Toast.makeText(getContext(), "ooppsps", Toast.LENGTH_SHORT).show();
        } else{

            referenceLogin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(myValue)) {
                        final String name = snapshot.child(myValue).child("fullname").getValue(String.class);
                        final String email = snapshot.child(myValue).child("email").getValue(String.class);
                        final String phno = snapshot.child(myValue).child("phoneno").getValue(String.class);
                        final String busno = snapshot.child(myValue).child("busno").getValue(String.class);
                        t1.setText(name);
                        t2.setText(email);
                        t3.setText(phno);
                        t4.setText(busno);


                    } else {
                        Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
        //setupOnBackPressed();
        return  v;
    }

    private void DeleteDataStudent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to revoke Student?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                Driverdelete= rootNode.getReference("login");
                Driverdelete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(myValue)){
                            Driverdelete.child(myValue).child("status").setValue("0");
                            Toast.makeText(getContext(), "Disabled Login Successfully", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.fragment_container,new StudentFragment());
                            fr.commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        }).setNegativeButton("No",null);
        builder.show();
    }

//    private void setupOnBackPressed() {
//        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (isEnabled()){
//                    Toast.makeText(getContext(), "Back off", Toast.LENGTH_SHORT).show();
//                    setEnabled(false);
//                    requireActivity().onBackPressed();
//                }
//            }
//        });
//    }


}