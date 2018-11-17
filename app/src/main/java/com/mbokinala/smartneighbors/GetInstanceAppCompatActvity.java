package com.mbokinala.smartneighbors;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class GetInstanceAppCompatActvity extends AppCompatActivity {

    public AppCompatActivity getInstance() {
        return this;
    }

    public void recheckLogin() {
        Log.d("LoginReq", "rechecking login status");
    }

}
