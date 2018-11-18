package com.mbokinala.smartneighbors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
    }

    private Map<String, String> getFields() {
        Map<String, String> postParams = new HashMap<>();

        EditText eventNameView = findViewById(R.id.eventNameView);

        return postParams;
    }
}
