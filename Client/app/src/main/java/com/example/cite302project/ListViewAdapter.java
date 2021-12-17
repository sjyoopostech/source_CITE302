package com.example.cite302project;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<String> jsonStringList = new ArrayList<>();
    public double lat;
    public double lng;

    @Override
    public int getCount() {
        return jsonStringList.size();
    }

    @Override
    public Object getItem(int i) {
        return jsonStringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list, viewGroup, false);
        }

        TextView nameTextView = view.findViewById(R.id.listitemname);
        TextView detailTextView = view.findViewById(R.id.listitemdetail);
        TextView distanceTextView = view.findViewById(R.id.listitemdistance);

        String jsonString = jsonStringList.get(i);

        if (jsonString.startsWith("RES&")) {
            RestaurantElement restaurantElement = new RestaurantElement();
            try {
                restaurantElement.fillRestaurantElement(new JSONObject(jsonString.substring(4)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            nameTextView.setText(restaurantElement.NameEnglish);

            String detail = "Restaurant ";
            detail += "(" + restaurantElement.getTypeMuslimFriendly();
            if (restaurantElement.TypeFood1 > 0) detail += ", " + restaurantElement.getTypeFood(1);
            if (restaurantElement.TypeFood2 > 0) detail += ", " + restaurantElement.getTypeFood(2);
            if (restaurantElement.TypeFood3 > 0) detail += ", " + restaurantElement.getTypeFood(3);
            detail += ")";
            detailTextView.setText(detail);

            String distance = getDistance(restaurantElement.Lat, restaurantElement.Lng) + "km";
            distanceTextView.setText(distance);
        }
        else if (jsonString.startsWith("PRA&")) {
            PrayerroomElement prayerroomElement = new PrayerroomElement();
            try {
                prayerroomElement.fillPrayerroomElement(new JSONObject(jsonString.substring(4)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            nameTextView.setText(prayerroomElement.NameEnglish);
            String detail = "Prayer Room ";
            detail += "(" + prayerroomElement.getTypePrayerroom() + ")";
            detailTextView.setText(detail);

            String distance = getDistance(prayerroomElement.Lat, prayerroomElement.Lng) + "km";
            distanceTextView.setText(distance);
        }
        return view;
    }

    private double getDistance(double latTarget, double lngTarget) {
        float[] results = new float[3];
        Location.distanceBetween(latTarget, lngTarget, lat, lng, results);
        Float distance = results[0] / 100;
        return distance.intValue() / 10.0;
    }

    public void SetItem(ArrayList<String> stringList, double latitude, double longitude) {
        jsonStringList = stringList;
        lat = latitude;
        lng = longitude;
    }
}
