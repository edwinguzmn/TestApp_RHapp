package com.apck.proyectfx.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apck.proyectfx.DrawerUserSortModeActivity;
import com.apck.proyectfx.Front_MainActivity;
import com.apck.proyectfx.R;
import com.apck.proyectfx.actyvity_vc.OutgoinginvitationActivity;
import com.apck.proyectfx.adapter.HorizontalRecyclerViewAdaptger;
import com.apck.proyectfx.adapter.UserAdapter;
import com.apck.proyectfx.listeners.UsersListener;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.model.User_VC;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerUsersFragment extends Fragment implements UsersListener {

    int id;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private HorizontalRecyclerViewAdaptger userAdapter;
    private List<User> mlist;
    CircleImageView profile_image;
    TextView username;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton;

    public TextView textName;
    public TextView textMail;
    public TextView userID;
    public TextView userRank;
    public TextView userStatus;
    public TextView dateOfJoin;
    public ImageView profileimage;

    public DrawerUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawer_users, container, false);


        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.ReciclerviewHorizontal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = view.findViewById(R.id.ShortModeUsers);

        mlist = new ArrayList<>();

        readUsers();

        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager1);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DrawerUserSortModeActivity.class));
            }
        });

        textName = view.findViewById(R.id.username);
        textMail = view.findViewById(R.id.Usermaill);
        userID = view.findViewById(R.id.User_id);
        userRank = view.findViewById(R.id.Rank);
        userStatus = view.findViewById(R.id.userStatus);
        dateOfJoin = view.findViewById(R.id.Dateofjoint);
        profileimage = view.findViewById(R.id.profile_imagen);


        return view;
    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        mlist.add(user);
                    }else{
                        textName.setText(user.getUsername());
                        textMail.setText(user.getMail());
                        userID.setText(user.getId());
                        userRank.setText(user.getRank());
                        userStatus.setText(user.getStatus());
                        dateOfJoin.setText(user.getDate_of_join());
                        if(user.getRank().equals("Director de RH")){
                            userRank.setTextColor(Color.parseColor("#90D487"));
                        }
                        if(user.getStatus().equals("Empleado")){
                            userStatus.setTextColor(Color.parseColor("#90D487"));
                        }
                        if(user.getStatus().equals("Sin empezar")){
                            userStatus.setTextColor(Color.parseColor("#FF6060"));
                        }
                        if(user.getStatus().equals("En proceso")){
                            userStatus.setTextColor(Color.parseColor("#FFE660"));
                        }
                        if(user.getImageURL().equals("default")){
                            profileimage.setImageResource(R.drawable.user13);
                        }else{
                            Glide.with(Objects.requireNonNull(getContext())).load(user.getImageURL()).into(profileimage);
                        }

                    }
                }
                userAdapter = new HorizontalRecyclerViewAdaptger(getContext(), mlist);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void initiateVideoMeeting(User_VC user_vc) {
        if(user_vc.token == null || user_vc.token.trim().isEmpty()){
            Toast.makeText(getContext(),
                    user_vc.user_name+"  "+ "its not aviable",
                    Toast.LENGTH_SHORT
            ).show();

        }else{
            Intent intent = new Intent(getContext(), OutgoinginvitationActivity.class);
            intent.putExtra("user", user_vc);
            intent.putExtra("type", "video");
            startActivity(intent);
        }

    }


    @Override
    public void initiateAudioMeeting(User_VC user_vc) {

    }
}