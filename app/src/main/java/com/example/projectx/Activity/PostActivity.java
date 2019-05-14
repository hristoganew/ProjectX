package com.example.projectx.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.projectx.R;

public class PostActivity extends ProjectxActivity {

    Dialog myDialog;
    TextView closeTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        myDialog = new Dialog(this);
    }

    public void openPostDialog(View view) {
        myDialog.setContentView(R.layout.post_popup_dialog);

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
