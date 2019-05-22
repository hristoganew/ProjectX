package com.example.projectx.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projectx.Adapter.PostAdapter;
import com.example.projectx.Model.Post;
import com.example.projectx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends ProjectxActivity {

    Dialog myDialog;
    TextView closeTextButton;
    Spinner postTypeDropdown;

    String [] postTypes = new String[]{"Text", "Food", "Location"};

    //post photo
    Uri postPhotoUri;
    ImageView postPhoto;
    EditText postTitle;
    RatingBar postRating;

    //posts view all
    private List<Post> mPosts;
    private PostAdapter postAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        myDialog = new Dialog(this);

        mPosts = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        initFirebase();
        getPosts();
    }

    public void openPostDialog(View view) {
        myDialog.setContentView(R.layout.post_popup_dialog);

        registerDialogElements();

        //post type dropdown
        initPostDropdown();

        //dialog close button
        initDialogCloseButton();

        //picture
        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermision(PostActivity.this);
                } else {
                    openGallery();
                }
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void registerDialogElements(){
        //init
        postRating = myDialog.findViewById(R.id.post_rating);
        postPhoto = myDialog.findViewById(R.id.post_photo);
        postTypeDropdown = myDialog.findViewById(R.id.post_dropdown);
        postTitle = myDialog.findViewById(R.id.post_title);
        closeTextButton = myDialog.findViewById(R.id.close_button_text);
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

    private void initDialogCloseButton(){
        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
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

                                Post newPost = new Post(user.getUid(), postTitle.getText().toString(), rating.toString(), imageLink, postTypeDropdown.getSelectedItem().toString());

                                postsTable.push().setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showMessage("Post Created");
                                        } else {
                                            showMessage(task.getException().getMessage());
                                        }
                                    }
                                });

                                updateUI(LoginActivity.class);
                            }
                        });
                    }
                });


    }

    private void getPosts(){
        DatabaseReference posts = mDatabase.child("posts");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    mPosts.add(post);
                }
                initPostsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        posts.addValueEventListener(postListener);
    }

    private void initPostsList(){
        postAdapter = new PostAdapter(this, mPosts);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
