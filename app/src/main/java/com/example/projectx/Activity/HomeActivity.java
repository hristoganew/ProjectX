package com.example.projectx.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectx.Adapter.ViewPagerAdapter;
import com.example.projectx.Fragment.DiscoverFriendsFragment;
import com.example.projectx.Fragment.MessageUsersFragment;
import com.example.projectx.Fragment.PostFragment;
import com.example.projectx.R;


public class HomeActivity extends ProjectxActivity {

    ImageView profilePicture;
    TextView profileName;

    private TabLayout tablayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);

        tablayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiscoverFriendsFragment(), "Find a Friend");
        adapter.addFragment(new PostFragment(), "Posts");
        adapter.addFragment(new MessageUsersFragment(), "Messages");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1, false);
        tablayout.setupWithViewPager(viewPager);


        initFirebase();
        initProperties();
    }

    private void initProperties() {
        profilePicture = findViewById(R.id.profile_picture);
        profileName = findViewById(R.id.profile_name);

        if (currentUser != null) {
            profileName.setText(currentUser.getDisplayName());
            Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profilePicture);
        }
    }

    public void openProfileSetting(View view) {
        updateUI(ProfileActivity.class);
    }

    public void openMessaging(View view) {
        updateUI(MessagingActivity.class);
    }

    public void openDiscover(View view) {
        updateUI(DiscoverFriendsActivity.class);
    }

    public void openPosts(View view) {
        updateUI(PostActivity.class);
    }

    public void logout(View view) {
        mAuth.signOut();
        updateUIAndFinish(LoginActivity.class);
    }
}
