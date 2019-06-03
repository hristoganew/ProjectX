package com.example.projectx.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectx.Adapter.UserAdapter;
import com.example.projectx.Model.User;
import com.example.projectx.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageUsersFragment extends ProjectXFragment {

    private List<User> mUsers;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;

    private TextView empty;

    public MessageUsersFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_messaging, container, false);

        mUsers = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        empty = view.findViewById(R.id.empty);
        initFirebase();
        getUsers();

        return view;

    }

    private void getUsers(){
        DatabaseReference users = mDatabase.child("users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    // add all users except the current one
                    if (!user.getId().equals(currentUser.getUid())){
                        mUsers.add(user);
                    }
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
        if (mUsers.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            userAdapter = new UserAdapter(getContext(), mUsers, false);
            recyclerView.setAdapter(userAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            empty.setVisibility(View.INVISIBLE);
        }



    }
}
