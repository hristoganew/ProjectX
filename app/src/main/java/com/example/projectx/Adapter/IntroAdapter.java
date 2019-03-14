package com.example.projectx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.projectx.R;

public class IntroAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public IntroAdapter(Context context) {
        this.context = context;
    }

    public int[] images= {
            R.drawable.img1,
            R.drawable.img2
    };

    public String[] headings = {
            "Night Mode",
            "Message"
    };

    public String[] descriptions = {
            "Test testtest testeste teste test test. Test testtest testeste teste test test!",
            "Test testtest testeste teste test test. Test testtest testeste teste test test!"
    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_item, container, false);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView description = (TextView) view.findViewById(R.id.description);

        image.setImageResource(images[position]);
        heading.setText(headings[position]);
        description.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
