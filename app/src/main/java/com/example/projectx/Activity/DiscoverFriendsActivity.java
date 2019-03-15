package com.example.projectx.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.projectx.Adapter.UserAdapter;
import com.example.projectx.Model.User;
import com.example.projectx.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DiscoverFriendsActivity extends ProjectxActivity {

    private List<User> mUsers;

    private UserAdapter userAdapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_friends);

        mUsers = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        initFirebase();
        getUsers();
    }

    private void getUsers(){
        DatabaseReference users = mDatabase.child("users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    mUsers.add(user);
                }
                initUsersList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        users.addValueEventListener(postListener);

    }

    private void initUsersList(){
        userAdapter = new UserAdapter(this, mUsers);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
