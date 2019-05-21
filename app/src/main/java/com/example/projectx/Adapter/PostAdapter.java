package com.example.projectx.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    Button addFriendButton;
    ImageView profilePicture;
    TextView profileName, txtclose;

    Dialog myDialog;
    private Context mContext;
    private List<Post> mPosts;

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

        //user picture
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference profileUri = mDatabase.child("users").child(post.getUserId()).child("photo");
        profileUri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Glide.with(mContext).load(dataSnapshot.getValue(String.class)).apply(RequestOptions.circleCropTransform()).into(viewHolder.userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






//        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (friendsListView == true){
//                    Intent activity = new Intent(mContext, ChatActivity.class);
//                    activity.putExtra("userId", user.getId());
//                    mContext.startActivity(activity);
//                }else{
//                    showDialog(user);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, rating;
        public ImageView image;
        public ImageView userImage;
//        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.row_post_title);
            rating = itemView.findViewById(R.id.row_post_rating);
            image = itemView.findViewById(R.id.row_post_img);
            userImage = itemView.findViewById(R.id.row_post_profile_img);
//            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    private void showDialog(final User user) {
        myDialog.setContentView(R.layout.user_popup_dialog);
        txtclose = myDialog.findViewById(R.id.txtclose);
        addFriendButton = myDialog.findViewById(R.id.add_friend_button);
        profilePicture = myDialog.findViewById(R.id.profile_picture);
        profileName = myDialog.findViewById(R.id.profile_name);

        Glide.with(mContext).load(user.getPhoto()).into(profilePicture);
        profileName.setText(user.getName());

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
