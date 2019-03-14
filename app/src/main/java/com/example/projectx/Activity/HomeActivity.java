package com.example.projectx.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectx.R;


public class HomeActivity extends ProjectxActivity {

    ImageView profilePicture;
    TextView profileName;
    Switch darkModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initFirebase();
        initProperties();
    }

    private void initProperties() {
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        profileName = (TextView) findViewById(R.id.profile_name);
        darkModeSwitch = (Switch) findViewById(R.id.dark_mode_switch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeSwitch.setChecked(true);
        }

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    updateUIAndFinish(HomeActivity.class);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    updateUIAndFinish(HomeActivity.class);
                }
            }
        });


        if (currentUser != null) {
            profileName.setText(currentUser.getDisplayName());
            Glide.with(this).load(currentUser.getPhotoUrl()).into(profilePicture);
        }
    }

    public void openProfileSetting(View view) {
        updateUI(ProfileActivity.class);
    }

    public void openMessaging(View view) {
        updateUI(MessagingActivity.class);
    }

    public void openDiscover(View view) {

    }

    public void logout(View view) {
        mAuth.signOut();
        updateUIAndFinish(LoginActivity.class);
    }
}
