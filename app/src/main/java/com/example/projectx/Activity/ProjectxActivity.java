package com.example.projectx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectx.R;
import com.example.projectx.SharedPreferences.ProjectxPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProjectxActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    protected DatabaseReference mDatabase;
    protected StorageReference mStorage;

    ProjectxPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new ProjectxPreferences(this);

        if (preferences.loadDarkTheme() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    public void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateUI(Class activityClass) {
        Intent activity = new Intent(getApplicationContext(), activityClass);
        startActivity(activity);
    }

    public void updateUIAndFinish(Class activityClass) {
        Intent activity = new Intent(getApplicationContext(), activityClass);
        startActivity(activity);
        finish();
    }

    public void showLoading(Button button, ProgressBar bar) {
        button.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
    }

    public void stopLoading(Button button, ProgressBar bar) {
        button.setVisibility(View.VISIBLE);
        bar.setVisibility(View.INVISIBLE);
    }
}
