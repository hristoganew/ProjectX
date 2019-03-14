package com.example.projectx.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ProjectxPreferences {
    SharedPreferences preferences;

    public ProjectxPreferences(Context context) {
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void setDarkThemeState(Boolean state) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("nightMode", state);
        editor.commit();
    }

    public Boolean loadDarkTheme(){
        Boolean state = preferences.getBoolean("nightMode",false);
        return  state;
    }
}
