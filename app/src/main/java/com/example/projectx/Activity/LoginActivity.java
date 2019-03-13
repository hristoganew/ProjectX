package com.example.projectx.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.projectx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends ProjectxActivity {

    EditText email, password;
    Button loadingButton;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFirebase();
        initProperties();
    }

    private void initProperties(){
        email = (EditText)findViewById(R.id.email_input);
        password = (EditText)findViewById(R.id.password_input);
        loadingButton = (Button)findViewById(R.id.login_button);
        loadingBar = (ProgressBar) findViewById(R.id.login_progress_bar);
    }

    public void login(View view){
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (emailText.isEmpty() || passwordText.isEmpty()){
            showMessage("Email or Password Incorrect!");
        }else {
            signInUser(emailText, passwordText);
        }
    }

    private void signInUser(String email, String password){
        showLoading(loadingButton, loadingBar);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateUI(HomeActivity.class);
                }else{
                    showMessage("Login Failed " + task.getException().getMessage());
                }
            }
        });
    }

    public void register(View view){
        updateUI(RegisterActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            updateUI(HomeActivity.class);
        }
    }
}
