package com.tarsier.marianz.tarsierscape.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tarsier.marianz.tarsierscape.R;

public class CreditsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setSubtitle(R.string.subtitle_activity_credits);
                getSupportActionBar().setElevation(0);
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }
}
