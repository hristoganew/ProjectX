package com.example.projectx.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectx.R;

import com.example.projectx.Model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ImageView profilePicture;
    TextView profileName, email, phone, txtclose;

    Dialog myDialog;
    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;

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

        viewHolder.name.setText(user.getName());

        Glide.with(mContext).load(user.getPhoto()).into(viewHolder.image);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(user);
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

    private void showDialog(User user) {
        myDialog.setContentView(R.layout.user_popup_dialog);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);

        profilePicture = (ImageView) myDialog.findViewById(R.id.profile_picture);
        profileName = (TextView) myDialog.findViewById(R.id.profile_name);

        Glide.with(mContext).load(user.getPhoto()).into(profilePicture);
        profileName.setText(user.getName());

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
