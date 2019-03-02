package com.example.shaf.sgsports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shaf.sgsports.ui.createevent.CreateEventFragment;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CreateEventFragment.newInstance())
                    .commitNow();
        }
    }
}
