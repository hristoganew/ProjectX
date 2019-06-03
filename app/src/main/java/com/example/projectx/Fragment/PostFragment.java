package com.example.projectx.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectx.Activity.PostAddActivity;
import com.example.projectx.Adapter.PostAdapter;
import com.example.projectx.Model.Post;
import com.example.projectx.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends ProjectXFragment {

    //posts view all
    private List<Post> mPosts;
    private PostAdapter postAdapter;
    private RecyclerView recyclerView;

    FloatingActionButton addPostButton;

    public PostFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post, container, false);

        mPosts = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);

        addPostButton = view.findViewById(R.id.addPostButton);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(getContext(), PostAddActivity.class);
                startActivity(activity);
            }
        });

        initFirebase();
        getPosts();

        return view;

    }


    private void getPosts(){
        DatabaseReference posts = mDatabase.child("posts");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    mPosts.add(post);
                }
                initPostsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        posts.addValueEventListener(postListener);
    }

    private void initPostsList(){
        postAdapter = new PostAdapter(getContext(), mPosts);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
