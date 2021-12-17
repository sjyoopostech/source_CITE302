package com.example.cite302project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cite302project.databinding.ActivityMapsBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private LocationManager lm;
    private LocationListener ll;
    private double lat;
    private double lng;

    private ArrayList<String> restaurantJsonList;
    private ArrayList<String> prayerroomJsonList;
    private ArrayList<Marker> markerList;
    private Filter filter;

    public static final int FILTER_REQUEST_CODE = 100001;

    private void updateElement() throws InterruptedException, JSONException {
        // Restaurant
        DBThread restaurantThread = new DBThread("restaurant_all");
        restaurantThread.start();
        restaurantThread.join();
        String restaurantJsonString = restaurantThread.getJsonString();
        JSONArray restaurantJsonArray = new JSONArray(restaurantJsonString);
        restaurantJsonList = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++) {
            JSONObject jsonObject = restaurantJsonArray.getJSONObject(i);
            restaurantJsonList.add(jsonObject.toString());
        }

        // Prayerroom
        DBThread prayerroomThread = new DBThread("prayerroom_all");
        prayerroomThread.start();
        prayerroomThread.join();
        String prayerroomJsonString = prayerroomThread.getJsonString();
        JSONArray prayerroomJsonArray = new JSONArray(prayerroomJsonString);
        prayerroomJsonList = new ArrayList<>();
        for (int i = 0; i < prayerroomJsonArray.length(); i++) {
            JSONObject jsonObject = prayerroomJsonArray.getJSONObject(i);
            prayerroomJsonList.add(jsonObject.toString());
        }
        System.out.println(prayerroomJsonList.size());
    }

    private void drawMarker() throws JSONException {
        if (markerList != null) for (Marker marker : markerList) marker.remove();
        else markerList = new ArrayList<>();

        for (String restaurantJson : restaurantJsonList) {
            RestaurantElement restaurantElement = new RestaurantElement();
            restaurantElement.fillRestaurantElement(new JSONObject(restaurantJson));

            int filterNum = filter.filteringRestaurantElement(restaurantElement);
            if (filterNum == 0) continue;

            MarkerOptions mo = new MarkerOptions().position(new LatLng(restaurantElement.Lat, restaurantElement.Lng));
            BitmapDrawable draw;
            if (filterNum == 2) draw = (BitmapDrawable)getResources().getDrawable(R.drawable.restaurantimportantmarkerimage);
            else draw = (BitmapDrawable)getResources().getDrawable(R.drawable.restaurantdefaultmarkerimage);
            Bitmap bitmap = Bitmap.createScaledBitmap(draw.getBitmap(), 70, 120, false);
            mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            Marker marker = mMap.addMarker(mo);
            marker.setSnippet("Restaurant");
            marker.setTag(restaurantElement.ID);
            markerList.add(marker);
        }

        for (String prayerroomJson : prayerroomJsonList) {
            PrayerroomElement prayerroomElement = new PrayerroomElement();
            prayerroomElement.fillPrayerroomElement(new JSONObject(prayerroomJson));

            int filterNum = filter.filteringPrayerroomElement(prayerroomElement);
            if (filterNum == 0) continue;

            MarkerOptions mo = new MarkerOptions().position(new LatLng(prayerroomElement.Lat, prayerroomElement.Lng));
            BitmapDrawable draw;
            if (filterNum == 2) draw = (BitmapDrawable)getResources().getDrawable(R.drawable.prayerroomimportantmarkerimage);
            else draw = (BitmapDrawable)getResources().getDrawable(R.drawable.prayerroomdefaultmarkerimage);
            Bitmap bitmap = Bitmap.createScaledBitmap(draw.getBitmap(), 70, 120, false);
            mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            Marker marker = mMap.addMarker(mo);
            marker.setSnippet("Prayerroom");
            marker.setTag(prayerroomElement.ID);
            markerList.add(marker);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // GPS
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng seoul = new LatLng(37.566, 126.9784);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 12));

        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getSnippet().equals("Restaurant")) {
                    restaurantmarkerclick((int) marker.getTag());
                }
                else if (marker.getSnippet().equals("Prayerroom")) {
                    prayerroommarkerclick((int) marker.getTag());

                }
                return false;
            }
        });

        filter = new Filter();

        // DB update
        try {
            updateElement();
            drawMarker();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            filter = (Filter) intent.getSerializableExtra("filter");
            try {
                drawMarker();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void restaurantmarkerclick(int ID) {
        Intent intent = new Intent(this, RestaurantMarkerActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void prayerroommarkerclick(int ID) {
        Intent intent = new Intent(this, PrayerroomMarkerActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void filterbuttonclick(View view) {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("filter", filter);
        startActivityForResult(intent, FILTER_REQUEST_CODE);
        overridePendingTransition(0, 0);
    }

    public void listbuttonclick(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("restaurantjsonstringlist", restaurantJsonList);
        intent.putExtra("prayerroomjsonstringlist", prayerroomJsonList);
        intent.putExtra("lat", mMap.getCameraPosition().target.latitude);
        intent.putExtra("lng", mMap.getCameraPosition().target.longitude);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void gpsbuttonclick(View view) {
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) return;
        if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) return;
            else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) return;
            else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        if (lat == 0 && lng == 0) return;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
    }
}