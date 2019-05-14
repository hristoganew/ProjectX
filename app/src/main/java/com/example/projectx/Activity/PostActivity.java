package com.example.projectx.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projectx.R;

public class PostActivity extends ProjectxActivity {

    Dialog myDialog;
    TextView closeTextButton;
    Spinner postTypeDropdown;

    String [] postTypes = new String[]{"Text", "Photo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        myDialog = new Dialog(this);
    }

    public void openPostDialog(View view) {
        myDialog.setContentView(R.layout.post_popup_dialog);

        //post type dropdown
        postTypeDropdown = myDialog.findViewById(R.id.postTypeDropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, postTypes);
        postTypeDropdown.setAdapter(adapter);

        //dialog close button
        closeTextButton = myDialog.findViewById(R.id.closeTextButton);
        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
