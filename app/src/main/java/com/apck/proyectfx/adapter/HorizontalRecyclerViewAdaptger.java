package com.apck.proyectfx.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apck.proyectfx.MessageActivity;
import com.apck.proyectfx.NavigationDrawerUserDescActivity;
import com.apck.proyectfx.R;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.utilities.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

public class HorizontalRecyclerViewAdaptger extends RecyclerView.Adapter<HorizontalRecyclerViewAdaptger.MyViewHolder> {

    private Context context;
    private List<User> mlist;
    Intent intent;

    public HorizontalRecyclerViewAdaptger(Context context, List<User> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_user_item, parent ,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = (User) mlist.get(position);
        holder.textName.setText(user.getUsername());
        holder.textMail.setText(user.getMail());
        holder.userID.setText(user.getId());
        holder.userRank.setText(user.getRank());
        if(user.getRank().equals("Director de RH")){
            holder.userRank.setTextColor(Color.parseColor("#90D487"));
        }
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
         public TextView userID;
         public TextView userRank;
         public TextView userStatus;
         public TextView dateOfJoin;
         public ImageView profileimage;


         public MyViewHolder(@NonNull View itemView) {
             super(itemView);

             textName = itemView.findViewById(R.id.user_name);
             textMail = itemView.findViewById(R.id.Usermaill);
             userID = itemView.findViewById(R.id.User_id);
             userRank = itemView.findViewById(R.id.Rank);
             userStatus = itemView.findViewById(R.id.UserStatus);
             dateOfJoin = itemView.findViewById(R.id.Dateofjoint);
             profileimage = itemView.findViewById(R.id.profile_imagen);
         }
     }
}
