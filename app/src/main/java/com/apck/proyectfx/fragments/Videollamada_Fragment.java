package com.apck.proyectfx.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.apck.proyectfx.R;
import com.apck.proyectfx.actyvity_vc.OutgoinginvitationActivity;
import com.apck.proyectfx.adapter.UserAdapter;
import com.apck.proyectfx.adapter.UserAdapter_Vc;
import com.apck.proyectfx.listeners.UsersListener;
import com.apck.proyectfx.model.User;
import com.apck.proyectfx.model.User_VC;
import com.apck.proyectfx.utilities.Constants;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Videollamada_Fragment extends Fragment implements UsersListener {

    private List<User_VC> user_vcs;
    private UserAdapter_Vc userAdapter_vc;
    private SearchView searchView;

    PreferenceManager preferenceManager;
    private RecyclerView userRecycleView;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videollamada_, container, false);
        preferenceManager = new PreferenceManager(getContext());

        userRecycleView = view.findViewById(R.id.recycler_view_VC);

        user_vcs = new ArrayList<>();
        userAdapter_vc = new UserAdapter_Vc(user_vcs, this);

        userRecycleView.setAdapter(userAdapter_vc);
        userRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this::getusers);

        getusers();

        return view;
    }

    private void getusers(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTIONS_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        swipeRefreshLayout.setRefreshing(false);
                        String myuserid = preferenceManager.getString(Constants.KEY_USER_ID);
                        if(task.isSuccessful() && task.getResult() != null){
                            user_vcs.clear();
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if(myuserid.equals(documentSnapshot.getId())){
                                    continue;
                                }
                                User_VC user_vc = new User_VC();
                                user_vc.user_name = documentSnapshot.getString(Constants.KEY_USER_NAME);
                                user_vc.email = documentSnapshot.getString(Constants.KEY_USER_MAIL);
                                user_vc.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                user_vcs.add(user_vc);
                            }
                            if(user_vcs.size()>0){
                                userAdapter_vc.notifyDataSetChanged();
                            }else{

                            }

                        }else{

                        }

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
        if(user_vc.token == null || user_vc.token.trim().isEmpty()){
            Toast.makeText(getContext(),
                    user_vc.user_name+"  "+ "its not aviable",
                    Toast.LENGTH_SHORT
            ).show();

        }else{
            Toast.makeText(getContext(),
                    "Audiomeeting whit  "+user_vc.user_name,
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
}