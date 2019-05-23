package com.example.projectx.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectx.Model.Post;
import com.example.projectx.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class PostDetailsActivity extends ProjectxActivity {

    private Intent intent;
    private String postId, pUserImage, pUserName;
    ImageView postImage, currentUserImage, postUserImage;
    TextView postTitle, postDateName, postDescription;
    EditText postComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        intent = getIntent();
        postId = intent.getStringExtra("postId");
        pUserImage = intent.getStringExtra("postUserImage");
        pUserName = intent.getStringExtra("postUserName");

        postImage = findViewById(R.id.post_detail_img);
        postTitle = findViewById(R.id.post_detail_title);
        postDateName = findViewById(R.id.post_detail_date_name);
        postDescription = findViewById(R.id.post_detail_desc);
        currentUserImage = findViewById(R.id.post_detail_currentuser_img);
        postComment = findViewById(R.id.post_detail_comment);
        postUserImage = findViewById(R.id.post_detail_user_img);


        initFirebase();
        getPost();
    }

    private void getPost() {

        DatabaseReference post = mDatabase.child("posts").child(postId);

        post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                loadData(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadData(Post post) {
        postTitle.setText(post.getTitle());
        postDescription.setText(post.getDescription());
        Glide.with(this).load(post.getPhoto()).into(postImage);
        Glide.with(this).load(pUserImage).apply(RequestOptions.circleCropTransform()).into(postUserImage);
        Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(currentUserImage);

        long timestamp  = (long) post.getDate();
        postDateName.setText(timestampToString(timestamp) + " by " + pUserName);

    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }
}
