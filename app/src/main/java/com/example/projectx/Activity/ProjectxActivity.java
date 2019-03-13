package com.example.projectx.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProjectxActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected DatabaseReference mDatabase;

    public void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateUI(Class activityClass){
        Intent activity = new Intent(getApplicationContext(), activityClass);
        startActivity(activity);
    }

    public void updateUIAndFinish(Class activityClass){
        Intent activity = new Intent(getApplicationContext(), activityClass);
        startActivity(activity);
        finish();
    }

    public void showLoading(Button button, ProgressBar bar){
        button.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
    }

    public void showButton(Button button, ProgressBar bar){
        button.setVisibility(View.VISIBLE);
        bar.setVisibility(View.INVISIBLE);
    }
}
