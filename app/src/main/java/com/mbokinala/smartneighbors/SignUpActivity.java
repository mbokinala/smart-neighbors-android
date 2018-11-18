package com.mbokinala.smartneighbors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends GetInstanceAppCompatActvity {

    private boolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        configButtons();
    }

    private void configButtons() {
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                final Map<String, String> fields = getFields();

                if(!error) {
                    Log.d("LoginReq", "No Error");
                    final ScrollView scroller = findViewById(R.id.scroller);
                    scroller.removeAllViews();

                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = inflater.inflate(R.layout.loading, scroller, false);
                    scroller.removeAllViews();
                    scroller.addView(row);


                    ScrollView buttonView = findViewById(R.id.buttonView);
                    buttonView.removeAllViews();

                    JSONObject jsonBody = new JSONObject(fields);

                    JsonObjectRequest newUserRequest = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url) + "/users", jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("LoginReq", "Success with sigup");
                                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                    scroller.removeAllViews();
                                    i.putExtra("email", fields.get("email"));
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("LoginReq", "Error: " + error.getMessage());
                                }
                            }
                    );

                    newUserRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(newUserRequest);
                } else {
                    Log.d("LoginReq", "Error in the fields");
                    error = false;
                }
            }
        });
    }

    private Map<String, String> getFields() {
        Map<String, String> postParams = new HashMap<>();

        //Get name
        EditText firstNameView = findViewById(R.id.firstNameView);
        String firstName = firstNameView.getText().toString();
        if (!isValidName(firstName)) {
            error = true;
            firstNameView.setError("Invalid first name");
            Log.d("LoginReq", "invalid first naem");
        }

        EditText lastNameView = findViewById(R.id.lastNameView);
        String lastName = lastNameView.getText().toString();
        if (!isValidName(lastName)) {
            error = true;
            lastNameView.setError("Invalid last name");
            Log.d("LoginReq", "invalid last naem");
        }

        String name = "";

        if (!error) {
            name = firstName + " " + lastName;
        }

        //Get phone
        EditText phoneView = findViewById(R.id.phoneView);
        String phone = phoneView.getText().toString();
        if (!isValidPhone(phone)) {
            error = true;
            phoneView.setError("Invalid phone number");
            Log.d("LoginReq", "invalid phone");
        }


        //Get email
        EditText emailView = findViewById(R.id.emailView);
        String email = emailView.getText().toString();
        if (!isValidEmail(email)) {
            error = true;
            emailView.setError("Invalid email address");
            Log.d("LoginReq", "invalid email");
        }

        //Get password
        EditText passwordView = (EditText) findViewById(R.id.passwordView);
        String password = passwordView.getText().toString();
        List<String> errorList = new ArrayList<>();
        if (!isValidPassword(password, errorList)) {
            String errors = "";
            for (String error : errorList) {
                errors = errors + error + "\n";
            }

            passwordView.setError(errors);
            Log.d("LoginReq", "invalid  pswdw");
        }


        //Get address
        EditText streetAddressView = findViewById(R.id.streetAddressView);
        String streetAddress = streetAddressView.getText().toString();

        EditText cityView = findViewById(R.id.cityView);
        String city = cityView.getText().toString();


        EditText stateView = findViewById(R.id.stateView);
        String state = stateView.getText().toString();

        EditText zipCodeView = findViewById(R.id.zipCodeView);
        String zipCode = zipCodeView.getText().toString();

        String address = streetAddress + ", " + city + ", " + state + " " + zipCode;


        postParams.put("name", name);
        postParams.put("email", email);
        postParams.put("address", address);
        postParams.put("phone", phone);
        postParams.put("password", password);

        if (!error) {
            clearAll();
        }

        error = false;

        return postParams;
    }

    private void clearAll() {
        ((EditText) findViewById(R.id.firstNameView)).setText("");
        ((EditText) findViewById(R.id.lastNameView)).setText("");
        ((EditText) findViewById(R.id.emailView)).setText("");
        ((EditText) findViewById(R.id.phoneView)).setText("");
        ((EditText) findViewById(R.id.passwordView)).setText("");
        ((EditText) findViewById(R.id.streetAddressView)).setText("");
        ((EditText) findViewById(R.id.cityView)).setText("");
        ((EditText) findViewById(R.id.stateView)).setText("");
        ((EditText) findViewById(R.id.zipCodeView)).setText("");

    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidName(String target) {
        Pattern namePattern = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE);
        return (!TextUtils.isEmpty(target) && namePattern.matcher(target).matches());
    }

    public static boolean isValidPhone(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }

    public static boolean isValidPassword(String target, List<String> errorList) {

        Pattern specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern uppercasePattern = Pattern.compile("[A-Z ]");
        Pattern lowercasePattern = Pattern.compile("[a-z ]");
        Pattern digitCasePattern = Pattern.compile("[0-9 ]");
        errorList.clear();

        boolean flag = true;

        if (target.length() < 8) {
            errorList.add("Password length must have at least 8 character !!");
            flag = false;
        }
        if (!specialCharPattern.matcher(target).find()) {
            errorList.add("Password must have at least one special character !!");
            flag = false;
        }
        if (!uppercasePattern.matcher(target).find()) {
            errorList.add("Password must have at least one uppercase character !!");
            flag = false;
        }
        if (!lowercasePattern.matcher(target).find()) {
            errorList.add("Password must have at least one lowercase character !!");
            flag = false;
        }
        if (!digitCasePattern.matcher(target).find()) {
            errorList.add("Password must have at least one digit character !!");
            flag = false;
        }

        return flag;

    }
}


