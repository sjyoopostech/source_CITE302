package com.example.cite302project;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity {

    private double lat;
    private double lng;

    class Comp implements Comparator<String> {

        @Override
        public int compare(String A, String B) {
            try {
                float[] results = new float[3];
                JSONObject jsonObjectA = new JSONObject(A.substring(4));
                Location.distanceBetween(
                        jsonObjectA.getDouble("lat"),
                        jsonObjectA.getDouble("lng"),
                        lat, lng, results);
                Float distanceA = results[0];

                JSONObject jsonObjectB = new JSONObject(B.substring(4));
                Location.distanceBetween(
                        jsonObjectB.getDouble("lat"),
                        jsonObjectB.getDouble("lng"),
                        lat, lng, results);
                Float distanceB = results[0];

                return distanceA.compareTo(distanceB);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listview = findViewById(R.id.listactivityview);
        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                String jsonString = (String) adapterView.getItemAtPosition(i);

                itemClick(jsonString);
            }
        });

        lat = getIntent().getDoubleExtra("lat", -1);
        lng = getIntent().getDoubleExtra("lng", -1);
        ArrayList<String> restaurantJsonStringList = getIntent().getStringArrayListExtra("restaurantjsonstringlist");
        ArrayList<String> prayerroomJsonStringList = getIntent().getStringArrayListExtra("prayerroomjsonstringlist");

        ArrayList<String> jsonStringList = new ArrayList<>();
        for (String str : restaurantJsonStringList) jsonStringList.add("RES&" + str);
        for (String str : prayerroomJsonStringList) jsonStringList.add("PRA&" + str);

        jsonStringList.sort(new Comp());
        adapter.SetItem(jsonStringList, lat, lng);
    }

    public void itemClick(String jsonString) {
        try {
            if (jsonString.startsWith("RES&")) {
                JSONObject jsonObject = new JSONObject(jsonString.substring(4));
                int ID = jsonObject.getInt("ID");

                Intent intent = new Intent(this, RestaurantMarkerActivity.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            else if (jsonString.startsWith("PRA&")) {
                JSONObject jsonObject = new JSONObject(jsonString.substring(4));
                int ID = jsonObject.getInt("ID");

                Intent intent = new Intent(this, PrayerroomMarkerActivity.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void backarrowclick(View view) {
        onBackPressed();
    }
}