package com.mbokinala.smartneighbors;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class NavBarItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private int contextId;
    private DrawerLayout drawer;
    private GetInstanceAppCompatActvity contextActivity;

    public NavBarItemSelectedListener(int contextId, DrawerLayout drawer, GetInstanceAppCompatActvity contextActivity) {
        this.contextId = contextId;
        this.drawer = drawer;
        this.contextActivity = contextActivity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == contextId) {
            //close navigation drawer
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        switch(item.getItemId()) {
            case R.id.homeMenuItem: {
                contextActivity.finish();
                break;
            } case R.id.byMeMenuIem: {
                contextActivity.startActivity(new Intent(contextActivity.getInstance(), ByMeActivity.class));
                break;
            } case R.id.logoutMenuItem: {
                Log.d("LoginReq", "Logging out");
                SaveSharedPreference.setID(contextActivity.getApplicationContext(), "");
                drawer.closeDrawer(GravityCompat.START);
                if(contextId != R.id.homeMenuItem) {
                    contextActivity.finish();
                } else {
                    contextActivity.recheckLogin();
                }
                return true;
            }
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
