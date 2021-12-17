package com.example.cite302project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.cite302project.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantMarkerActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private RestaurantElement restaurantElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_marker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.restaurantmarkermap);
        mapFragment.getMapAsync(this);

        int ID = getIntent().getIntExtra("ID", -1);

        try {
            DBThread restaurantThread = new DBThread("restaurant_one?id=" + ID);
            restaurantThread.start();
            restaurantThread.join();
            String jsonString = restaurantThread.getJsonString();
            JSONArray jsonArray = new JSONArray(jsonString);

            restaurantElement = new RestaurantElement();
            restaurantElement.fillRestaurantElement(jsonArray.getJSONObject(0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.restaurantnameenglish)).setText(restaurantElement.NameEnglish);
        ((TextView) findViewById(R.id.restaurantnamekorean)).setText(restaurantElement.NameKorean);

        if (restaurantElement.TypeMuslimFriendly == 0) ((ImageView) findViewById(R.id.restaurantmuslimfriendlyimage)).setImageResource(R.drawable.muslimlevel0);
        else if (restaurantElement.TypeMuslimFriendly == 1) ((ImageView) findViewById(R.id.restaurantmuslimfriendlyimage)).setImageResource(R.drawable.muslimlevel1);
        else if (restaurantElement.TypeMuslimFriendly == 2) ((ImageView) findViewById(R.id.restaurantmuslimfriendlyimage)).setImageResource(R.drawable.muslimlevel2);
        else if (restaurantElement.TypeMuslimFriendly == 3) ((ImageView) findViewById(R.id.restaurantmuslimfriendlyimage)).setImageResource(R.drawable.muslimlevel3);

        String typeFood = restaurantElement.getTypeFood(1);
        if (restaurantElement.TypeFood2 > 0) typeFood += ", " + restaurantElement.getTypeFood(2);
        if (restaurantElement.TypeFood3 > 0) typeFood += ", " + restaurantElement.getTypeFood(3);
        ((TextView) findViewById(R.id.restauranttypefood)).setText(typeFood);

        ((TextView) findViewById(R.id.restaurantaddressenglish)).setText("English Address : " + restaurantElement.AddressEnglish);
        ((TextView) findViewById(R.id.restaurantaddresskorean)).setText("Korean Address : " + restaurantElement.AddressKorean);

        if (!restaurantElement.InfoPhonenumber.equals("")) ((TextView) findViewById(R.id.restaurantphonenumber)).setText("Phone Number : " + restaurantElement.InfoPhonenumber);
        else {
            findViewById(R.id.restaurantphonebutton).setVisibility(View.INVISIBLE);
            findViewById(R.id.restaurantphonenumber).setVisibility(View.GONE);
        }


        String additionalInfo = "";
        if (restaurantElement.InfoSeatnumber > 0) additionalInfo += "Restaurant has " + restaurantElement.InfoSeatnumber + " seats\n";
        if (restaurantElement.InfoParking) additionalInfo += "Parking available\n";
        else additionalInfo += "Parking impossible\n";
        if (restaurantElement.InfoAlcoholfree) additionalInfo += "Alcohol Free\n";
        if (restaurantElement.InfoWudu) additionalInfo += "Wudu washbasin available\n";
        if (restaurantElement.InfoGenderwashroom) additionalInfo += "Washrooms are separated by gender\n";
        if (restaurantElement.InfoRug || restaurantElement.InfoHookah) {
            String providingStuff = "";
            if (restaurantElement.InfoRug) providingStuff += "Prayer Rug, ";
            if (restaurantElement.InfoHookah) providingStuff += "Hookah, ";
            additionalInfo += providingStuff.substring(0, providingStuff.length() - 2) + " provided\n";
        }
        if (restaurantElement.InfoMuslimcooks) additionalInfo += "Cooked by a muslim chef\n";
        if (restaurantElement.InfoHalalingredient) additionalInfo += "Halal certified ingredients used\n";
        ((TextView) findViewById(R.id.restaurantadditionalinformation)).setText(additionalInfo);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlng = new LatLng(restaurantElement.Lat, restaurantElement.Lng);
        MarkerOptions mo = new MarkerOptions().position(latlng);
        BitmapDrawable draw;
        if (true) draw = (BitmapDrawable)getResources().getDrawable(R.drawable.restaurantimportantmarkerimage);
        Bitmap bitmap = Bitmap.createScaledBitmap(draw.getBitmap(), 70, 120, false);
        mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        Marker marker = mMap.addMarker(mo);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
    }

    public void restaurantphonebuttonclick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+restaurantElement.InfoPhonenumber));
        startActivity(intent);
    }

    public void backarrowclick(View view) {
        onBackPressed();
    }
}