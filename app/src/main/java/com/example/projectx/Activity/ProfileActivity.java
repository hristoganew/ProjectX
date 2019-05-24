package com.example.projectx.Activity;

import com.bumptech.glide.Glide;
import com.example.projectx.Adapter.MessageAdapter;
import com.example.projectx.Model.Chat;
import com.example.projectx.Model.Comment;
import com.example.projectx.Model.Post;
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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

public class ProfileActivity extends ProjectxActivity {

    ImageView profilePicture;
    TextView profileName, friendCounter, postCounter, commentsCounter;
    EditText email, name;
    Switch darkModeSwitch;
    Button updateProfileButton;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initFirebase();
        initProperties();
        iniDarkModeSwitch();

        getFriendsCount();
        getPostsCount();
        getPostCommentsCount();
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
}
