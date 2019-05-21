package com.example.projectx.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectx.Activity.ChatActivity;
import com.example.projectx.R;

import com.example.projectx.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Button addFriendButton;
    ImageView profilePicture;
    TextView profileName, txtclose;

    Dialog myDialog;
    private Context mContext;
    private List<User> mUsers;
    private Boolean friendsList = false;

    public UserAdapter(Context mContext, List<User> mUsers,Boolean friendsList) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.friendsList = friendsList;

        myDialog = new Dialog(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUsers.get(i);
        final Boolean friendsListView = this.friendsList;

        viewHolder.name.setText(user.getName());

        Glide.with(mContext).load(user.getPhoto()).apply(RequestOptions.circleCropTransform()).into(viewHolder.image);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        public TextView name;
        public ImageView image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text);
            image = itemView.findViewById(R.id.profile_image);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    private void showDialog(final User user) {
        myDialog.setContentView(R.layout.user_popup_dialog);
        txtclose = myDialog.findViewById(R.id.txtclose);
        addFriendButton = myDialog.findViewById(R.id.add_friend_button);
        profilePicture = myDialog.findViewById(R.id.profile_picture);
        profileName = myDialog.findViewById(R.id.profile_name);

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
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        userReference.child("friends").child(id).setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else{

                }
            }
        });
    }
}
