package com.mbokinala.smartneighbors;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends GetInstanceAppCompatActvity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailView = findViewById(R.id.emailView);

        if (getIntent().hasExtra("email")) {
            emailView.setText(getIntent().getExtras().getString("email"));
            Log.d("AppLogs", "Should toast");
            Toast.makeText(getApplicationContext(), "Successfully Signed up", Toast.LENGTH_SHORT).show();
        }

        configButtons();

        emailView.requestFocus();
    }

    private void configButtons() {
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });

        Button noAccountButton = findViewById(R.id.noAccountButton);
        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void processLogin() {
        final Map<String, String> fields = getFields();
        JSONObject jsonBody = new JSONObject(fields);

        Log.d("AppLogs", fields.get("password"));

        ConstraintLayout loginView = findViewById(R.id.loginView);
        loginView.removeAllViewsInLayout();
        ConstraintSet set = new ConstraintSet();

        ProgressBar bar = new ProgressBar(getApplicationContext());
        bar.setId(View.generateViewId());

        loginView.addView(bar, 0);
        set.clone(loginView);
        set.connect(bar.getId(), ConstraintSet.TOP, loginView.getId(), ConstraintSet.TOP, 8);
        set.connect(bar.getId(), ConstraintSet.BOTTOM, loginView.getId(), ConstraintSet.BOTTOM, 8);
        set.connect(bar.getId(), ConstraintSet.START, loginView.getId(), ConstraintSet.START, 8);
        set.connect(bar.getId(), ConstraintSet.END, loginView.getId(), ConstraintSet.END, 8);

        JsonObjectRequest AppLogsuest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url) + "/users/login", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("AppLogs", "response.get(status) = " + response.get("status"));
                            if (response.get("status").equals("success")) {
                                SaveSharedPreference.setID(getApplicationContext(), String.valueOf(response.get("id")));
                                Log.d("AppLogs", String.valueOf(response.get("id")));
                                SaveSharedPreference.setName(getApplicationContext(), String.valueOf(response.get("name")));
                                SaveSharedPreference.setEmail(getApplicationContext(), String.valueOf(response.get("email")));

                                finish();
                            } else {
                                Log.d("AppLogs", "Invalid");
                                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                                Thread thread = new Thread() {
                                    public void run() {
                                        try {
                                            Thread.sleep(Toast.LENGTH_LONG);
                                            LoginActivity.this.finish();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                thread.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AppLogs", "Error: " + error.getMessage());
                    }
                }
        );

        AppLogsuest.setShouldCache(false);
        AppLogsuest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(AppLogsuest);
    }

    private Map<String, String> getFields() {
        EditText emailView = findViewById(R.id.emailView);
        String email = emailView.getText().toString();
        emailView.setText("");

        EditText passwordView = findViewById(R.id.passwordView);
        String password = passwordView.getText().toString();
        passwordView.setText("");

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("email", email);
        fields.put("password", password);

        return fields;
    }
}
