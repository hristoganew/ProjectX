package com.example.projectx.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectx.Activity.PostDetailsActivity;
import com.example.projectx.Model.Post;
import com.example.projectx.R;

import com.example.projectx.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Dialog myDialog;
    private Context mContext;
    private List<Post> mPosts;

    private int postTypeDrawable;
    private String postUserImage, postUserName;


    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;

        myDialog = new Dialog(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Post post = mPosts.get(i);

        viewHolder.title.setText(post.getTitle());
        viewHolder.rating.setText(post.getRating());
        Glide.with(mContext).load(post.getPhoto()).into(viewHolder.image);
        switch (post.getType()) {
            case "Text":
                postTypeDrawable = R.drawable.ic_text_format_black_24dp;
                break;
            case "Food":
                postTypeDrawable = R.drawable.ic_restaurant_black_24dp;
                break;
            case "Location":
                postTypeDrawable = R.drawable.ic_location_on_black_24dp;
                break;
            default:
                postTypeDrawable = R.drawable.ic_text_format_black_24dp;
                break;
        }

        viewHolder.postTypeImage.setBackgroundResource(postTypeDrawable);

        //user picture
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference profileUri = mDatabase.child("users").child(post.getUserId());
        profileUri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                postUserName = user.getName();
                postUserImage = user.getPhoto();
                Glide.with(mContext).load(postUserImage).apply(RequestOptions.circleCropTransform()).into(viewHolder.userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(mContext, PostDetailsActivity.class);
                activity.putExtra("postId", post.getId());
                activity.putExtra("postUserImage", postUserImage);
                activity.putExtra("postUserName", postUserName);

                mContext.startActivity(activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, rating;
        public ImageView image, userImage, postTypeImage;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.row_post_title);
            rating = itemView.findViewById(R.id.row_post_rating);
            image = itemView.findViewById(R.id.row_post_img);
            userImage = itemView.findViewById(R.id.row_post_profile_img);
            postTypeImage = itemView.findViewById(R.id.row_post_type_image);
        }
    }

}
