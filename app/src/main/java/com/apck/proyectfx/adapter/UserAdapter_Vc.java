package com.apck.proyectfx.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apck.proyectfx.R;
import com.apck.proyectfx.listeners.UsersListener;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.model.User_VC;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter_Vc extends RecyclerView.Adapter<UserAdapter_Vc.UserViewHolder> {


    private List<User_VC> users_vc;
    private UsersListener usersListener;
    private List<User_VC> mListFull;

    public UserAdapter_Vc(List<User_VC> users_vc, UsersListener usersListener) {
        this.users_vc = users_vc;
        this.usersListener = usersListener;
        mListFull = new ArrayList<>(users_vc);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.user_item_videocall, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users_vc.get(position));

    }

    @Override
    public int getItemCount() {
        return users_vc.size();
    }




    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView texFirstchar, texusername, texmail;
        ImageView cideocall, audiocall;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            texusername = itemView.findViewById(R.id.user_name);
            texmail = itemView.findViewById(R.id.user_mail);
            cideocall = itemView.findViewById(R.id.videocall);
            audiocall = itemView.findViewById(R.id.audiocall);
        }

        void setUserData(User_VC user_vc) {
            texusername.setText(user_vc.user_name);
            texmail.setText(user_vc.email);
            audiocall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usersListener.initiateAudioMeeting(user_vc);
                }
            });
            cideocall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usersListener.initiateVideoMeeting(user_vc);
                }
            });

        }

    }

}