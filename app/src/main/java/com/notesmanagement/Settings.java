package com.notesmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }

    }
}
