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
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SortModeAdapter extends RecyclerView.Adapter<SortModeAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<User> mlist;
    private List<User> mListFull;
    private Intent intent;

    public SortModeAdapter(Context context, List<User> mlist) {
        this.context = context;
        this.mlist = mlist;
        mListFull = new ArrayList<>(mlist);
    }

    @NonNull
    @Override
    public SortModeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.usersortmode_item, parent ,false);
        return new SortModeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortModeAdapter.MyViewHolder holder, int position) {
        User user = (User) mlist.get(position);
        holder.textName.setText(user.getUsername());
        holder.textMail.setText(user.getMail());
        holder.userRequest.setText(user.getJob_Requesting());

        if(user.getStatus().equals("Empleado")){
            holder.userStatus.setTextColor(Color.parseColor("#90D487"));
        }
        holder.userStatus.setText(user.getStatus());
        if(user.getStatus().equals("Sin empezar")){
            holder.userStatus.setTextColor(Color.parseColor("#FF6060"));
        }
        if(user.getStatus().equals("En proceso")){
            holder.userStatus.setTextColor(Color.parseColor("#C4AC2A"));
        }
        holder.dateOfJoin.setText(user.getDate_of_join());
        if(user.getImageURL().equals("default")){
            holder.profileimage.setImageResource(R.drawable.user13);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.profileimage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, NavigationDrawerUserDescActivity.class);
                intent.putExtra("userid", user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textName;
        public TextView textMail;
        public TextView userStatus;
        public TextView dateOfJoin;
        public TextView userRequest;
        public ImageView profileimage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.user_name);
            textMail = itemView.findViewById(R.id.Usermaill);
            userStatus = itemView.findViewById(R.id.UserStatus);
            dateOfJoin = itemView.findViewById(R.id.Dateofjoint);
            userRequest = itemView.findViewById(R.id.UserRequest);
            profileimage = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User item : mListFull) {
                    if (item.getUsername().toLowerCase().contains(filterPattern) || item.getStatus().toLowerCase().contains(filterPattern) || item.getJob_Requesting().toLowerCase().contains(filterPattern)
                            || item.getSearch_KEY_DAY().toLowerCase().contains(filterPattern) || item.getSearch_KEY_MONTH().toLowerCase().contains(filterPattern) || item.getSearch_KEY_YEAR().toLowerCase().contains(filterPattern)) {
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