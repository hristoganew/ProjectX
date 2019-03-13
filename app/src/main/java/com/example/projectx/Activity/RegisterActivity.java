package com.example.projectx.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projectx.Model.User;
import com.example.projectx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

public class RegisterActivity extends ProjectxActivity {

    EditText email, password, rePassword, name;
    String emailText, passwordText, rePasswordText, nameText;
    ImageView userPhoto;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri userImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFirebase();
        initProperties();
    }

    private void initProperties() {
        //user photo
        userPhoto = (ImageView) findViewById(R.id.user_photo);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermision();
                } else {
                    openGallery();
                }
            }
        });

        name = (EditText)findViewById(R.id.full_name_input);
        email = (EditText)findViewById(R.id.email_input);
        password = (EditText)findViewById(R.id.password_input);
        rePassword = (EditText)findViewById(R.id.password_again_input);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);

    }

    private void checkAndRequestForPermision() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessage("Permission needed to procede!");
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            userImageUri = data.getData();

            userPhoto.setImageURI(userImageUri);
        }
    }

    public void register(View view){
        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        rePasswordText = rePassword.getText().toString();

        if ((emailText.isEmpty() || passwordText.isEmpty()) && !password.equals(rePasswordText)){
            showMessage("Please make sure you have an email or password!");
        }else {
            createUserAccount();
        }

    }

    private void createUserAccount(){
        mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateUserInfo();
                }else{
                    showMessage("Account Creation Failed " + task.getException().getMessage());
                }
            }
        });
    }

    private void updateUserInfo(){
        final FirebaseUser user = mAuth.getCurrentUser();
        final DatabaseReference reference = mDatabase.child("users");

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nameText).build();

        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            User newUser = new User(user.getUid(), nameText, emailText);

                            reference.child(user.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        showMessage("Account Created");
                                    }else{
                                        showMessage(task.getException().getMessage());
                                    }
                                }
                            });
                        }else{
                            showMessage(task.getException().getMessage());
                        }
                    }
                });

        updateUI(LoginActivity.class);
    }
}
