package com.mbokinala.smartneighbors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewEventActivity extends AppCompatActivity {

    boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processNewEvent();
            }
        });
    }

    private void processNewEvent(){
        Map<String, String> fields = getFields();

        if(!error) {
            final ScrollView scroller = findViewById(R.id.scroller);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.loading, scroller, false);
            scroller.removeAllViews();
            scroller.addView(row);

            JSONObject jsonBody = new JSONObject(fields);

            JsonObjectRequest newUserRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url) + "/events/" + SaveSharedPreference.getID(getApplicationContext()), jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("LoginReq", "Success with new event");
                            Intent i = new Intent(NewEventActivity.this, ByMeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("LoginReq", "Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG);
                        }
                    }
            );

            newUserRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(getApplicationContext()).add(newUserRequest);
        } else {
            error = false;
        }
    }

    private Map<String, String> getFields() {
        Map<String, String> postParams = new HashMap<>();

        EditText eventNameView = findViewById(R.id.eventNameView);
        String eventName = validateNotEmptyReturnText(eventNameView);

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        String category = categorySpinner.getSelectedItem().toString();

        EditText placeView = findViewById(R.id.placeFieldView);
        String place = validateNotEmptyReturnText(placeView);

        EditText dateView = findViewById(R.id.dateTimeView);
        String date = validateNotEmptyReturnText(dateView);

        EditText notesView = findViewById(R.id.notesView);
        String notes = validateNotEmptyReturnText(notesView);

        if(!error){
            postParams.put("category", category);
            postParams.put("place1", place);
            postParams.put("eventName", eventName);
            postParams.put("date", date);
            postParams.put("notes", notes);
            postParams.put("byName", SaveSharedPreference.getName(getApplicationContext()));
        }

        return postParams;
    }

    private String validateNotEmptyReturnText(EditText view) {
        String text = String.valueOf(view.getText());
        if(text.isEmpty()) {
            error = true;
            view.setError("Cannot be empty");
        }

        return text;
    }

}
