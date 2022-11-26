package com.example.pro.fragmentadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pro.R;
import com.example.pro.fragmentadmin.datas.Details;

import java.util.ArrayList;

public class FragmentAdaperHome extends RecyclerView.Adapter<FragmentAdaperHome.MyViewHolder> {






    /**
     * Created by Mahadi on 3/11/2018.
     */



    Context context;
    ArrayList<Details> list;

        public FragmentAdaperHome(Context context, ArrayList<Details> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v;
            v = LayoutInflater.from(context).inflate(R.layout.content_feedback, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.imageView.setImageResource(R.drawable.feeddrivers);


            Details details=list.get(position);
            holder.phone_num.setText(details.getUsername().toString());
            holder.name.setText(details.getMessage().toString());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView phone_num;
            ImageView imageView;


            public MyViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.text);
                phone_num = (TextView) itemView.findViewById(R.id.name);
                imageView = (ImageView) itemView.findViewById(R.id.image_viewfeed);
            }
        }
    }


