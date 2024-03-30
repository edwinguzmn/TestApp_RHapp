package com.apck.proyectfx.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apck.proyectfx.MessageActivity;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    private Context mcontex;
    private List mlist;
    private List<User> mListFull;

    Intent intent;

    public UserAdapter(Context mcontex, List<User> mlist){
        this.mcontex = mcontex;
        this.mlist = mlist;
        mListFull = new ArrayList<>(mlist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontex).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = (User) mlist.get(position);
        holder.username.setText(user.getUsername());
        holder.user_mail.setText(user.getMail());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.user13);
        }else{
            Glide.with(mcontex).load(user.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(mcontex, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        public TextView user_mail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_name);
            profile_image = itemView.findViewById(R.id.profile_image);
            user_mail = itemView.findViewById(R.id.user_mail);
        }
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
