package com.mbokinala.smartneighbors;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ByMeActivity extends GetInstanceAppCompatActvity {

    ListView list;
    EventAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_me);

        list = findViewById(R.id.byMeListView);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);

        adapter = new EventAdapter(this);
        list.setAdapter(adapter);

        reload();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    //Refreshes data from RESTful service
    public void reload() {
        String url = getResources().getString(R.string.api_url) + "/events/by/" + SaveSharedPreference.getID(getApplicationContext());

        JsonArrayRequest request;
        request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                adapter.list.clear();
                Log.d("AppLogs", "received response");
                Log.d("AppLogs", "length: " + response.length());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("eventName");
                        Log.d("AppLogs", "name: " + name);

                        String place = jsonObject.getString("place1");
                        String neighborID = jsonObject.getString("hostId");
                        String status = jsonObject.getString("status");
                        String neighborName = jsonObject.getString("byName");
                        String date = jsonObject.getString("date");
                        String category = jsonObject.getString("category");
                        String notes = jsonObject.getString("notes");

                        adapter.list.add(new Event(name, place, neighborID, status, neighborName, date, category, notes));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("AppLogs", e.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AppLogs", error.getMessage());
                Log.d("AppLogs", error.getMessage());
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    @Override
    protected void onResume() {
        reload();
        super.onResume();
    }

    class EventAdapter extends BaseAdapter {

        private ArrayList<Event> list;
        private Context context;

        public EventAdapter(Context context) {
            list = new ArrayList<>();
            list.add(new Event(null, null, null, null, null, null, null, null));
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("AppLogs", "getting view " + position);
            Event event = list.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (event.name != null) {
                Log.d("AppLogs", "items in list: " + event.name);
                View row = inflater.inflate(R.layout.single_event, parent, false);
                TextView titleView = (TextView) row.findViewById(R.id.titleView);
                TextView dateView = (TextView) row.findViewById(R.id.dateView);
                TextView statusView = (TextView) row.findViewById(R.id.statusView);
                TextView neighborView = (TextView) row.findViewById(R.id.neighborView);
                TextView placeView = (TextView) row.findViewById(R.id.placeView);

                if (position % 2 == 0) {
                    row.setBackgroundColor(Color.parseColor("#d2ecf7"));
                } else {
                    row.setBackgroundColor(Color.parseColor("#e5e5e5"));
                }

                titleView.setText(event.name);
                dateView.setText(event.date);
                statusView.setText(event.status);
                neighborView.setText(event.neighborName);
                placeView.setText(event.place);

                return row;
            } else {
                Log.d("AppLogs", "No items in list");
                View row = inflater.inflate(R.layout.loading_items, parent, false);
                return row;
            }
        }
    }
}