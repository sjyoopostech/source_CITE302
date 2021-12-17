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

public class PrayerroomMarkerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PrayerroomElement prayerroomElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayerroom_marker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.prayerroommarkermap);
        mapFragment.getMapAsync(this);

        int ID = getIntent().getIntExtra("ID", -1);

        try {
            DBThread prayerroomThread = new DBThread("prayerroom_one?id=" + ID);
            prayerroomThread.start();
            prayerroomThread.join();
            String jsonString = prayerroomThread.getJsonString();
            JSONArray jsonArray = new JSONArray(jsonString);

            prayerroomElement = new PrayerroomElement();
            prayerroomElement.fillPrayerroomElement(jsonArray.getJSONObject(0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.prayerroomnameenglish)).setText(prayerroomElement.NameEnglish);

        ((TextView) findViewById(R.id.prayerroomtype)).setText(prayerroomElement.getTypePrayerroom());

        ((TextView) findViewById(R.id.prayerroomaddressenglish)).setText("English Address : " + prayerroomElement.AddressEnglish);

        if (!prayerroomElement.InfoPhonenumber.equals("")) ((TextView) findViewById(R.id.prayerroomphonenumber)).setText("Phone Number : " + prayerroomElement.InfoPhonenumber);
        else {
            findViewById(R.id.prayerroomphonebutton).setVisibility(View.INVISIBLE);
            findViewById(R.id.prayerroomphonenumber).setVisibility(View.GONE);
        }

        String additionalInfo = "";
        if (prayerroomElement.InfoPermanent) additionalInfo += "Pernament Prayer Room\n";
        else additionalInfo += "Temporary Prayer Room (Provide Prayer Goods)\n";
        if (prayerroomElement.InfoGenderdivision) additionalInfo += "Prayer room divided by gender\n";
        if (prayerroomElement.InfoQuran || prayerroomElement.InfoCarpet || prayerroomElement.InfoKibla) {
            String providingStuff = "";
            if (prayerroomElement.InfoQuran) providingStuff += "Quran, ";
            if (prayerroomElement.InfoCarpet) providingStuff += "Prayer Carpet, ";
            if (prayerroomElement.InfoKibla) providingStuff += "Kibla, ";
            additionalInfo += providingStuff.substring(0, providingStuff.length() - 2) + " provided\n";
        }
        if (prayerroomElement.InfoFeetwashing) additionalInfo += "Feet Washing Facilty provided\n";
        ((TextView) findViewById(R.id.prayerroomadditionalinformation)).setText(additionalInfo);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlng = new LatLng(prayerroomElement.Lat, prayerroomElement.Lng);
        MarkerOptions mo = new MarkerOptions().position(latlng);
        BitmapDrawable draw;
        if (true) draw = (BitmapDrawable)getResources().getDrawable(R.drawable.prayerroomimportantmarkerimage);
        Bitmap bitmap = Bitmap.createScaledBitmap(draw.getBitmap(), 70, 120, false);
        mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        Marker marker = mMap.addMarker(mo);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
    }

    public void prayerroomphonebuttonclick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+prayerroomElement.InfoPhonenumber));
        startActivity(intent);
    }

    public void backarrowclick(View view) {
        onBackPressed();
    }
}