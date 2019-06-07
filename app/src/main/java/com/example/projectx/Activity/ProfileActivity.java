package com.example.projectx.Activity;

import com.bumptech.glide.Glide;
import com.example.projectx.Adapter.MessageAdapter;
import com.example.projectx.Adapter.UserAdapter;
import com.example.projectx.Model.Chat;
import com.example.projectx.Model.Comment;
import com.example.projectx.Model.Post;
import com.example.projectx.Model.User;
import com.example.projectx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends ProjectxActivity {

    ImageView profilePicture;
    TextView profileName, friendCounter, postCounter, commentsCounter;
    EditText email, name;
    Switch darkModeSwitch;
    Button updateProfileButton;
    ProgressBar loadingProgress;
    Dialog myDialog;
    TextView txtclose, empty;

    private List<User> mUsers;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initFirebase();
        initProperties();
        iniDarkModeSwitch();

        getFriendsCount();
        getPostsCount();
        getPostCommentsCount();

        myDialog = new Dialog(this);
        mUsers = new ArrayList<>();
    }

    private void initProperties(){
        profilePicture = findViewById(R.id.profile_picture);
        profileName = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email_input);
        name = findViewById(R.id.profile_name_input);
        friendCounter = findViewById(R.id.friend_counter);
        postCounter = findViewById(R.id.posts_counter);
        commentsCounter = findViewById(R.id.comments_counter);
        updateProfileButton = findViewById(R.id.update_profile_button);
        loadingProgress = findViewById(R.id.loading_progress);

        Glide.with(this).load(currentUser.getPhotoUrl()).into(profilePicture);

        String profileNameText = currentUser.getDisplayName();
        profileName.setText(profileNameText);

        name.setText(profileNameText);
        email.setText(currentUser.getEmail());
    }

    private void iniDarkModeSwitch(){
        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        if (preferences.loadDarkTheme() == true) {
            darkModeSwitch.setChecked(true);
        }

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.setDarkThemeState(true);
                    updateUIAndFinish(HomeActivity.class);
                } else {
                    preferences.setDarkThemeState(false);
                    updateUIAndFinish(HomeActivity.class);
                }
            }
        });
    }

    private void getFriendsCount(){
        DatabaseReference friendsReference = mDatabase.child("users").child(currentUser.getUid());
        friendsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                if (key.equals("friends")){
                    long count = dataSnapshot.getChildrenCount();
                    friendCounter.setText(String.valueOf(count));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostsCount(){
        DatabaseReference friendsReference = mDatabase.child("posts");
        friendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getUserId().equals(currentUser.getUid())){
                        counter++;
                    }
                }

                postCounter.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostCommentsCount(){
        DatabaseReference friendsReference = mDatabase.child("comment");
        friendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (DataSnapshot vsnapshot : snapshot.getChildren()) {
                        Comment comment = vsnapshot.getValue(Comment.class);
                        if (comment.getUid().equals(currentUser.getUid())){
                            counter++;
                        }
                    }
                }

                commentsCounter.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateProfile(View view){
        showLoading(updateProfileButton, loadingProgress);
        currentUser.updateEmail(email.getText().toString());

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString())
                .build();

        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    showMessage("Profile Updated");
                    stopLoading(updateProfileButton, loadingProgress);
                }else{
                    showMessage("Error");
                    stopLoading(updateProfileButton, loadingProgress);
                }
            }
        });
    }

    public void showFriends(View view){
        myDialog.setContentView(R.layout.activity_discover_friends);
        recyclerView = myDialog.findViewById(R.id.recycler_view);
        empty = myDialog.findViewById(R.id.empty);
        empty.setVisibility(View.INVISIBLE);

//        txtclose = myDialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });

        initFirebase();
        getUsers();

        myDialog.show();
    }

    private void getUsers(){
        DatabaseReference users = mDatabase.child("users").child(currentUser.getUid()).child("friends");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    DatabaseReference user = mDatabase.child("users").child(snapshot.getValue().toString());
                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                            // add all users except the current one
                            if (!user.getId().equals(currentUser.getUid())){
                                mUsers.add(user);
                            }
                            initUsersList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        users.addValueEventListener(postListener);

    }

    private void initUsersList(){
        userAdapter = new UserAdapter(this, mUsers, false);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
