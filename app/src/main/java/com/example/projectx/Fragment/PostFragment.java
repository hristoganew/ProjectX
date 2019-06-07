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
import android.widget.TextView;

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
    TextView empty;

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

        empty = view.findViewById(R.id.empty);

        initFirebase();
        getPosts();

        return view;

    }


    private void getPosts(){
        DatabaseReference posts = mDatabase.child("posts");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPosts.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Post post = snapshot.getValue(Post.class);

                    if (currentUser.getUid().equals(post.getUserId())) {
                        mPosts.add(post);
                    }else{
                        DatabaseReference currentUserFriends = mDatabase.child("users").child(currentUser.getUid()).child("friends");
                        currentUserFriends.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    if (post.getUserId().equals(snapshot.getValue())){
                                        mPosts.add(post);
                                    }
                                }
                                initPostsList();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
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
        if (mPosts.isEmpty()){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else{
            postAdapter = new PostAdapter(getContext(), mPosts);
            recyclerView.setAdapter(postAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            empty.setVisibility(View.INVISIBLE);
        }
    }

}
