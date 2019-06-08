package com.example.projectx.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projectx.Model.Post;
import com.example.projectx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PostAddActivity extends ProjectxActivity {

    TextView closeTextButton;
    Spinner postTypeDropdown;

    String [] postTypes = new String[]{"Text", "Food", "Location"};

    //post photo
    Uri postPhotoUri;
    ImageView postPhoto;
    EditText postTitle, postDescription;
    RatingBar postRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);

        initFirebase();


        registerElements();
        initPostDropdown();

        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermision(PostAddActivity.this);
                } else {
                    openGallery();
                }
            }
        });
    }

    private void registerElements(){
        //init
        postRating = findViewById(R.id.post_rating);
        postRating.setRating(5);
        postPhoto = findViewById(R.id.post_photo);
        postTypeDropdown = findViewById(R.id.post_dropdown);
        postTitle = findViewById(R.id.post_title);
        postDescription = findViewById(R.id.post_description);
        closeTextButton = findViewById(R.id.close_button_text);
    }

    private void initPostDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, postTypes);
        postTypeDropdown.setAdapter(adapter);

        postTypeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                if (item.equals("Text")) {

                }

                if (item.equals("Photo")) {

                }

                if (item.equals("Location")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            postPhotoUri = data.getData();

            postPhoto.setImageURI(postPhotoUri);
        }
    }

    public void createPost(View view){
        final FirebaseUser user = mAuth.getCurrentUser();
        final DatabaseReference postsTable = mDatabase.child("posts");

        StorageReference postPhotosStorage = mStorage.child("post_photos");
        final StorageReference imageFilePath = postPhotosStorage.child(postPhotoUri.getLastPathSegment());

        imageFilePath.putFile(postPhotoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String imageLink = uri.toString();

                                Number rating = postRating.getRating();

                                DatabaseReference key = postsTable.push();
                                Post newPost = new Post(key.getKey(), user.getUid(), postTitle.getText().toString(), rating.toString(), imageLink, postTypeDropdown.getSelectedItem().toString(), postDescription.getText().toString());

                                key.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showMessage("Post Created");
                                        } else {
                                            showMessage(task.getException().getMessage());
                                        }
                                    }
                                });

                            }
                        });
                    }
                });


    }

}
