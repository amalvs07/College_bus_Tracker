package com.example.pro.fragmentadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro.R;
import com.example.pro.fragmentadmin.datas.DemoData;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.MyViewHolder> {
Context context;

    public DemoAdapter(Context context, ArrayList<DemoData> list) {
        this.context = context;
        this.list = list;
    }

    ArrayList<DemoData> list;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.bus_parameters, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            DemoData demoData=list.get(position);
            holder.busplateno.setText(demoData.getBusplate().toString());
        holder.busroute.setText(demoData.getRoutename().toString());
        holder.busrouteid.setText(demoData.getBusno().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView busplateno,busroute,busrouteid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            busplateno = (TextView) itemView.findViewById(R.id.name);
            busroute = (TextView) itemView.findViewById(R.id.busid);
            busrouteid = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
