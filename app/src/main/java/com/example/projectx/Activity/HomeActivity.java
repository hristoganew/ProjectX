package com.example.projectx.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectx.R;


public class HomeActivity extends ProjectxActivity {

    ImageView profilePicture;
    TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initFirebase();
        initProperties();
    }

    private void initProperties(){
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        profileName = (TextView) findViewById(R.id.profile_name);

        if (currentUser != null){
            profileName.setText(currentUser.getDisplayName());
            profilePicture.setImageURI(currentUser.getPhotoUrl());
        }
    }

    public void openProfileSetting(View view){
        updateUI(ProfileActivity.class);
    }

    public void openMessaging(View view){
        updateUI(MessagingActivity.class);
    }

    public void openDiscover(View view){

    }

    public void logout(View view){
        mAuth.signOut();
        updateUIAndFinish(LoginActivity.class);
    }
}
