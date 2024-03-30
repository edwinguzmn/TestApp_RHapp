package com.apck.proyectfx.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apck.proyectfx.NavigationDrawerUserDescActivity;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.LogEvents;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class LogEventAdapter extends RecyclerView.Adapter<LogEventAdapter.MyViewHolder> {
    private Context context;
    private List<LogEvents> mlist;


    public LogEventAdapter(Context context, List<LogEvents> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public LogEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.log_users_activity_item, parent, false);
        return new LogEventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogEventAdapter.MyViewHolder holder, int position) {
        LogEvents logEvent = (LogEvents) mlist.get(position);
        holder.textPart1.setText(logEvent.getText1());
        holder.textPart2.setText(logEvent.getText2());
        holder.usernam.setText(logEvent.getusername_LogEvent());
        holder.date.setText(logEvent.getdate_LogEvent());
        holder.usernam.setTextColor(Color.parseColor("#90D487"));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textPart1;
        public TextView textPart2;
        public TextView usernam;
        public TextView date;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textPart1 = itemView.findViewById(R.id.textfirstpart);
            textPart2 = itemView.findViewById(R.id.textSecondPart);
            usernam = itemView.findViewById(R.id.Username);
            date = itemView.findViewById(R.id.textDatelogevent);
        }
    }
}