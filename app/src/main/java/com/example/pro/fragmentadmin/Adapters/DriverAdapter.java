package com.example.pro.fragmentadmin.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro.R;
import com.example.pro.fragmentadmin.DriverFragmentdetails;
import com.example.pro.fragmentadmin.datas.DriverData;

import java.util.ArrayList;

public class DriverAdapter  extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {

    Context context;
    ArrayList<DriverData> list;
String username;
    public DriverAdapter(Context context, ArrayList<DriverData> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.parameters_contents, parent, false);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, Driver_Home.class);
//                context.startActivity(intent);
//
//            }
//        });
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAdapter.MyViewHolder holder, int position) {
//        holder.name.setText(list.get(position).getName());
//        holder.phone_num.setText(list.get(position).getBusno());
        //   holder.imageView.setImageResource(contactList.get(position).getPhoto());


        holder.imageView.setImageResource(R.drawable.driver);


        DriverData driverData=list.get(position);
        holder.name.setText(driverData.getFullname().toString());
        holder.phone_num.setText(driverData.getPhoneno().toString());
        holder.Busid.setText(driverData.getBusno().toString());
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=driverData.getUsername().toString();
                Bundle bb = new Bundle();
                bb.putString("username", username);

                AppCompatActivity appCompatActivity=(AppCompatActivity)v.getContext();

                FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                DriverFragmentdetails driverFragmentdetails=new DriverFragmentdetails();
                driverFragmentdetails.setArguments(bb);
                fragmentTransaction.replace(R.id.fragment_container,driverFragmentdetails).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView phone_num;
        TextView Busid;
        TextView moreBtn;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moreBtn=(TextView)itemView.findViewById(R.id.MoreButton);
            name = (TextView) itemView.findViewById(R.id.name);
            phone_num = (TextView) itemView.findViewById(R.id.text);
            Busid = (TextView) itemView.findViewById(R.id.busid);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
