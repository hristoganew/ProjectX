package com.example.projectx.Activity;

import android.os.Bundle;
import android.view.View;

import com.example.projectx.R;


public class HomeActivity extends ProjectxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initFirebase();
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
