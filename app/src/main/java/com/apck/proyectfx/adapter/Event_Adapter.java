package com.apck.proyectfx.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apck.proyectfx.MessageActivity;
import com.apck.proyectfx.Modifi_Event_Activity;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.Event_model;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.ViewHolder> implements Filterable {
    private Context mcontex;
    private List<Event_model> mlist;
    private List<Event_model> mListFull;
    private Intent intent;

    public Event_Adapter(Context mcontex, List<Event_model> mlist){
        LayoutInflater layoutInflater = LayoutInflater.from(mcontex);
        this.mcontex = mcontex;
        this.mlist = mlist;
        mListFull = new ArrayList<>(mlist);
    }


    @NonNull
    @Override
    public Event_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontex).inflate(R.layout.event_item, parent, false);
        return new Event_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_Adapter.ViewHolder holder, int position) {
        Event_model event_model = (Event_model) mlist.get(position);
        holder.textDay.setText(event_model.getDay());
        String month = event_model.getMonth();
        if (month.equals("1")){
            holder.textMonth.setText("Ene");
        }
        if (month.equals("2")){
            holder.textMonth.setText("Feb");
        }
        if (month.equals("3")){
            holder.textMonth.setText("Mar");
        }
        if (month.equals("4")){
            holder.textMonth.setText("Abr");
        }
        if (month.equals("5")){
            holder.textMonth.setText("May");
        }
        if (month.equals("6")){
            holder.textMonth.setText("Jun");
        }
        if (month.equals("7")){
            holder.textMonth.setText("Jul");
        }
        if (month.equals("8")){
            holder.textMonth.setText("Ago");
        }
        if (month.equals("9")){
            holder.textMonth.setText("Sep");
        }
        if (month.equals("10")){
            holder.textMonth.setText("Oct");
        }
        if (month.equals("11")){
            holder.textMonth.setText("Nov");
        }
        if (month.equals("12")){
            holder.textMonth.setText("Dic");
        }
        holder.textHourStar.setText(event_model.getEvent_hourStart());
        holder.textHourFinish.setText(event_model.getEvent_hourFinish());
        if (event_model.getEvent_hourFinish().equals("")){
            holder.simpleText.setText("");
        }
        holder.textEventName.setText(event_model.getEvent_name());
        holder.textEventDes.setText(event_model.getEvent_desc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(mcontex, Modifi_Event_Activity.class);
                intent.putExtra("eventid", event_model.getEventid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setItems(List<Event_model> items){ mlist = items; }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textMonth;
        public TextView textDay;
        public TextView simpleText;
        public TextView textHourStar;
        public TextView textHourFinish;
        public TextView textEventName;
        public TextView textEventDes;

        ViewHolder(View itemView){
            super(itemView);

            textDay = itemView.findViewById(R.id.DateDay);
            textMonth = itemView.findViewById(R.id.DateMonth);
            textHourStar = itemView.findViewById(R.id.DateHourStart);
            simpleText = itemView.findViewById(R.id.simpleText);
            textHourFinish = itemView.findViewById(R.id.DateHourFinish);
            textEventName = itemView.findViewById(R.id.EventName);
            textEventDes = itemView.findViewById(R.id.EventDesc);
        }
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event_model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Event_model item : mListFull) {
                    if (item.getMonth().toLowerCase().contains(filterPattern) || item.getEvent_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mlist.clear();
            mlist.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
