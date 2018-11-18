package com.mbokinala.smartneighbors;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends GetInstanceAppCompatActvity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configButtons();

        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recheckLogin();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(new NavBarItemSelectedListener(R.id.homeMenuItem, mDrawerLayout, this));

    }

    private void configButtons() {
        Button byMeButton = findViewById(R.id.byMeButton);
        byMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ByMeActivity.class));
            }
        });

        Button newEventButton = findViewById(R.id.createEventButton);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewEventActivity.class));
            }
        });

        Button byNeighborsButton = findViewById(R.id.byNeighborsButton);
        byNeighborsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the by neighbors activity
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || mToggle.onOptionsItemSelected(item);
    }

    @Override
    public void recheckLogin() {
        if(SaveSharedPreference.getID(getApplicationContext()).length() == 0) {
            Log.d("AppLogs", "Rerouting to Login");
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        } else {
            Log.d("LoginReq", "setting headers");
            setNavHeader();
        }
    }

    @Override
    protected void onResume() {
        recheckLogin();
        setNavHeader();
        super.onResume();
    }

    private void setNavHeader() {
        if(!TextUtils.isEmpty(SaveSharedPreference.getName(getApplicationContext())) && !TextUtils.isEmpty(SaveSharedPreference.getEmail(getApplicationContext()))) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            TextView navName = headerView.findViewById(R.id.signedInAsView);
            navName.setText(SaveSharedPreference.getName(getApplicationContext()));

            TextView navEmail = headerView.findViewById(R.id.signedEmailView);
            navEmail.setText(SaveSharedPreference.getEmail(getApplicationContext()));
        }
    }
}

