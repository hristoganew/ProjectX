package com.example.projectx.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    private void initProperties() {
        profilePicture = findViewById(R.id.profile_picture);
        profileName = findViewById(R.id.profile_name);

        if (currentUser != null) {
            profileName.setText(currentUser.getDisplayName());
            Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profilePicture);
        }
    }

    public void openProfileSetting(View view) {
        updateUI(ProfileActivity.class);
    }

    public void openMessaging(View view) {
        updateUI(MessagingActivity.class);
    }

    public void openDiscover(View view) {
        updateUI(DiscoverFriendsActivity.class);
    }

    public void openPosts(View view) {
        updateUI(PostActivity.class);
    }

    public void logout(View view) {
        mAuth.signOut();
        updateUIAndFinish(LoginActivity.class);
    }
}
