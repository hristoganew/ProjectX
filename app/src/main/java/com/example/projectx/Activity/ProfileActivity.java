package com.example.projectx.Activity;

import com.bumptech.glide.Glide;
import com.example.projectx.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends ProjectxActivity {

    ImageView profilePicture;
    TextView profileName, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initFirebase();
        initProperties();
    }

    private void initProperties(){
        profilePicture = findViewById(R.id.profile_picture);
        profileName = findViewById(R.id.profile_name);
        email = findViewById(R.id.email_address);
        phone = findViewById(R.id.phone);

        Glide.with(this).load(currentUser.getPhotoUrl()).into(profilePicture);
        profileName.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());
    }
}
