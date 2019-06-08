package com.example.projectx.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectx.Activity.ChatActivity;
import com.example.projectx.Model.Chat;
import com.example.projectx.Model.Comment;
import com.example.projectx.Model.Post;
import com.example.projectx.R;

import com.example.projectx.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Button addFriendButton;
    ProgressBar loadingBar;
    ImageView profilePicture;
    TextView profileName, txtclose, friendCounter, postCounter, commentsCounter, alreadyFollowedMessage;
    DatabaseReference mDatabase;

    Dialog myDialog;
    private Context mContext;
    private List<User> mUsers;
    private Boolean friendsList = false;

    public UserAdapter(Context mContext, List<User> mUsers,Boolean friendsList) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.friendsList = friendsList;

        if (mContext != null){
            myDialog = new Dialog(mContext);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final User user = mUsers.get(i);
        final Boolean friendsListView = this.friendsList;
        viewHolder.name.setText(user.getName());

        Glide.with(mContext).load(user.getPhoto()).apply(RequestOptions.circleCropTransform()).into(viewHolder.image);

        if (friendsListView == true){

            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference chats = mDatabase.child("Chats");
            chats.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(currentUser.getUid()) && chat.getSender().equals(user.getId()) || chat.getReceiver().equals(user.getId()) && chat.getSender().equals(currentUser.getUid())){
                            viewHolder.lastMessage.setText(chat.getMessage());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // show dialog if the user is in Discover Friends Activity else load the Chat activity
                if (friendsListView == true){
                    Intent activity = new Intent(mContext, ChatActivity.class);
                    activity.putExtra("userId", user.getId());
                    mContext.startActivity(activity);
                }else{
                    showDialog(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, lastMessage;
        public ImageView image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text);
            image = itemView.findViewById(R.id.profile_image);
            cardView = itemView.findViewById(R.id.card_view);
            lastMessage = itemView.findViewById(R.id.last_message);
        }
    }

    private void showDialog(final User user) {
        myDialog.setContentView(R.layout.user_popup_dialog);
        txtclose = myDialog.findViewById(R.id.txtclose);
        addFriendButton = myDialog.findViewById(R.id.add_friend_button);
        loadingBar = myDialog.findViewById(R.id.loading_progress);
        profilePicture = myDialog.findViewById(R.id.profile_picture);
        profileName = myDialog.findViewById(R.id.profile_name);

        friendCounter = myDialog.findViewById(R.id.friend_counter);
        postCounter = myDialog.findViewById(R.id.posts_counter);
        commentsCounter = myDialog.findViewById(R.id.comments_counter);

        alreadyFollowedMessage = myDialog.findViewById(R.id.already_followed);
        alreadyFollowedMessage.setVisibility(View.INVISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("friends");

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (user.getId().equals(snapshot.getValue())){
                        alreadyFollowedMessage.setVisibility(View.VISIBLE);
                        addFriendButton.setVisibility(View.INVISIBLE);
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getFriendsCount(user.getId());
        getPostsCount(user.getId());
        getPostCommentsCount(user.getId());


        Glide.with(mContext).load(user.getPhoto()).apply(RequestOptions.circleCropTransform()).into(profilePicture);
        profileName.setText(user.getName());

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(user.getId());
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

    private void addFriend(String id){
        addFriendButton.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        userReference.child("friends").child(id).setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    alreadyFollowedMessage.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "You've added a new friend successfully!", Toast.LENGTH_LONG).show();
                    loadingBar.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    loadingBar.setVisibility(View.INVISIBLE);
                    addFriendButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getFriendsCount(final String id){
        DatabaseReference friendsReference = mDatabase.child("users").child(id);
        friendsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                if (key.equals("friends")){
                    long count = dataSnapshot.getChildrenCount();
                    friendCounter.setText(String.valueOf(count));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostsCount(final String id){
        DatabaseReference friendsReference = mDatabase.child("posts");
        friendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getUserId().equals(id)){
                        counter++;
                    }
                }

                postCounter.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostCommentsCount(final String id){

        DatabaseReference friendsReference = mDatabase.child("comment");
        friendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (DataSnapshot vsnapshot : snapshot.getChildren()) {
                        Comment comment = vsnapshot.getValue(Comment.class);
                        if (comment.getUid().equals(id)){
                            counter++;
                        }
                    }
                }

                commentsCounter.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
