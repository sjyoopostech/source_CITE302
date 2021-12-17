package com.example.cite302project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    private Filter filter;
    private Intent intent;

    private final String[][] restaurantmuslimfriendlystring =
            {{"Halal Certified", "Self Certified", "Muslim Friendly", "Pork\nFree"}};

    private final int[][] restaurantmuslimfriendlyID = {
            {0, 1, 2, 3}
    };


    private final String[][] restauranttypestring = {
            {"Korean Cuisine", "Korean Temple Food", "", ""},
            {"Arab", "Asian", "Chinese", "Egyptian"},
            {"French", "Indian", "Japanese", "Malaysian"},
            {"Moroccan", "Nepalese", "Pakistani", "Russian"},
            {"Tunisian", "Turkish", "Uzbek", ""},
            {"Middle Eastern", "Buffet", "", ""},
            {"Western", "Western (Room Service)", "", ""}
    };

    private final int[][] restauranttypeID = {
            {9, 10, -1, -1},
            {1, 2, 4, 5},
            {6, 7, 8, 11},
            {13, 14, 15, 16},
            {17, 18, 19, -1},
            {12, 3, -1, -1},
            {20, 21, -1, -1}
    };

    private final String[][] prayerroomtypestring = {
            {"Mosque", "Islamic Mosque", "", ""},
            {"Hotel", "Hospital", "Restaurant", "University"},
            {"Tour Spot", "Tour Information Center", "", ""},
            {"International Airport", "Others", "", ""}
    };

    private final int[][] prayerroomtypeID = {
            {4, 2, -1, -1},
            {0, 3, 5, 8},
            {6, 7, -1, -1},
            {1, 9, -1, -1}
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter = (Filter) getIntent().getSerializableExtra("filter");
        if (!filter.Restaurant) {
            LinearLayout linearLayout = findViewById(R.id.restaurantfilterdetail);
            linearLayout.setVisibility(View.GONE);

            ImageView imageView = findViewById(R.id.restaurantfilterimage);
            imageView.setImageResource(R.drawable.filterunselectedimage);
        }
        if (!filter.Prayerroom) {
            LinearLayout linearLayout = findViewById(R.id.prayerroomfilterdetail);
            linearLayout.setVisibility(View.GONE);

            ImageView imageView = findViewById(R.id.prayerroomfilterimage);
            imageView.setImageResource(R.drawable.filterunselectedimage);
        }

        setrestaurantfiltermuslimfriendly();
        setrestaurantfiltertype();
        setprayerroomfiltertype();
        intent = new Intent(this, MapsActivity.class);
    }

    public void restaurantfilterclick(View view) {
        filter.Restaurant = !filter.Restaurant;

        LinearLayout linearLayout = findViewById(R.id.restaurantfilterdetail);
        ImageView imageView = findViewById(R.id.restaurantfilterimage);

        if (filter.Restaurant) {
            linearLayout.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.filterselectedimage);
        }
        else {
            linearLayout.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.filterunselectedimage);
        }

        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
    }

    public void prayerroomfilterclick(View view) {
        filter.Prayerroom = !filter.Prayerroom;

        LinearLayout linearLayout = findViewById(R.id.prayerroomfilterdetail);
        ImageView imageView = findViewById(R.id.prayerroomfilterimage);

        if (filter.Prayerroom) {
            linearLayout.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.filterselectedimage);
        }
        else {
            linearLayout.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.filterunselectedimage);
        }

        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void restaurantfiltermuslimfriendlyclick(int ID) {
        if (filter.RestaurantMuslimFriendly == ID) filter.RestaurantMuslimFriendly = -1;
        else filter.RestaurantMuslimFriendly = ID;

        for (int i = 0; i < 4; i++) {
            LinearLayout linearLayout = findViewById(2100000+4000+i);
            TextView textView = findViewById(2100000+5000+i);

            if (filter.RestaurantMuslimFriendly == i) {
                linearLayout.setBackground(getDrawable(R.drawable.filterselected));
                textView.setTextColor(Color.WHITE);
            }
            else if (filter.RestaurantMuslimFriendly > i) {
                linearLayout.setBackground(getDrawable(R.drawable.filterhalfselected));
                textView.setTextColor(Color.parseColor("#444444"));
            }
            else {
                linearLayout.setBackground(getDrawable(R.drawable.filterunselected));
                textView.setTextColor(Color.parseColor("#444444"));
            }
        }

        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void restaurantfiltertypeclick(int ID) {
        filter.RestaurantType[ID] = !filter.RestaurantType[ID];

        LinearLayout linearLayout = findViewById(2100000+ID);
        TextView textView = findViewById(2100000+1000+ID);

        if (filter.RestaurantType[ID]) {
            linearLayout.setBackground(getDrawable(R.drawable.filterselected));
            textView.setTextColor(Color.WHITE);
        }
        else {
            linearLayout.setBackground(getDrawable(R.drawable.filterunselected));
            textView.setTextColor(Color.parseColor("#444444"));
        }

        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void prayerroomfiltertypeclick(int ID) {
        filter.PrayerroomType[ID] = !filter.PrayerroomType[ID];

        LinearLayout linearLayout = findViewById(2100000+2000+ID);
        TextView textView = findViewById(2100000+3000+ID);

        if (filter.PrayerroomType[ID]) {
            linearLayout.setBackground(getDrawable(R.drawable.filterselected));
            textView.setTextColor(Color.WHITE);
        }
        else {
            linearLayout.setBackground(getDrawable(R.drawable.filterunselected));
            textView.setTextColor(Color.parseColor("#444444"));
        }

        intent.putExtra("filter", filter);
        setResult(RESULT_OK, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setrestaurantfiltermuslimfriendly() {
        LinearLayout filtermuslimfriendly = findViewById(R.id.restaurantfiltermuslimfriendly);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.weight = 1;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        textParams.topMargin = 25;
        textParams.bottomMargin = 25;
        textParams.leftMargin = 5;
        textParams.rightMargin = 5;

        for (int i = 0; i < restaurantmuslimfriendlystring.length; i++) {
            final LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setLayoutParams(layoutParams1);

            for (int j = 0; j < restaurantmuslimfriendlystring[i].length; j++) {
                if (restaurantmuslimfriendlystring[i][j].equals("")) break;

                final LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setLayoutParams(layoutParams2);
                if (filter.RestaurantMuslimFriendly == restaurantmuslimfriendlyID[i][j]) linearLayout2.setBackground(getDrawable(R.drawable.filterselected));
                else linearLayout2.setBackground(getDrawable(R.drawable.filterunselected));
                linearLayout2.setId(2100000+4000+restaurantmuslimfriendlyID[i][j]);
                linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = view.getId();
                        if (id/1000 != 2104) {
                            System.out.println("Wrong ID");
                        }
                        restaurantfiltermuslimfriendlyclick(id-2100000-4000);
                    }
                });

                final TextView textView = new TextView(this);
                textView.setLayoutParams(textParams);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText(restaurantmuslimfriendlystring[i][j]);
                if (filter.RestaurantMuslimFriendly == restaurantmuslimfriendlyID[i][j]) textView.setTextColor(Color.WHITE);
                else textView.setTextColor(Color.parseColor("#444444"));
                textView.setId(2100000+5000+restaurantmuslimfriendlyID[i][j]);

                linearLayout2.addView(textView);
                linearLayout1.addView(linearLayout2);
            }
            filtermuslimfriendly.addView(linearLayout1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setrestaurantfiltertype() {
        LinearLayout filtertype = findViewById(R.id.restaurantfiltertype);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.weight = 1;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        textParams.topMargin = 25;
        textParams.bottomMargin = 25;
        textParams.leftMargin = 5;
        textParams.rightMargin = 5;

        for (int i = 0; i < restauranttypestring.length; i++) {
            final LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setLayoutParams(layoutParams1);

            for (int j = 0; j < restauranttypestring[i].length; j++) {
                if (restauranttypestring[i][j].equals("")) break;

                final LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setLayoutParams(layoutParams2);
                if (filter.RestaurantType[restauranttypeID[i][j]]) linearLayout2.setBackground(getDrawable(R.drawable.filterselected));
                else linearLayout2.setBackground(getDrawable(R.drawable.filterunselected));
                linearLayout2.setId(2100000+restauranttypeID[i][j]);
                linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = view.getId();
                        if (id/1000 != 2100) {
                            System.out.println("Wrong ID");
                        }
                        restaurantfiltertypeclick(id-2100000);
                    }
                });

                final TextView textView = new TextView(this);
                textView.setLayoutParams(textParams);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText(restauranttypestring[i][j]);
                if (filter.RestaurantType[restauranttypeID[i][j]]) textView.setTextColor(Color.WHITE);
                else textView.setTextColor(Color.parseColor("#444444"));
                textView.setId(2100000+1000+restauranttypeID[i][j]);

                linearLayout2.addView(textView);
                linearLayout1.addView(linearLayout2);
            }
            filtertype.addView(linearLayout1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setprayerroomfiltertype() {
        LinearLayout filtertype = findViewById(R.id.prayerroomfiltertype);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.weight = 1;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        textParams.topMargin = 25;
        textParams.bottomMargin = 25;
        textParams.leftMargin = 5;
        textParams.rightMargin = 5;

        for (int i = 0; i < prayerroomtypestring.length; i++) {
            final LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setLayoutParams(layoutParams1);

            for (int j = 0; j < prayerroomtypestring[i].length; j++) {
                if (prayerroomtypestring[i][j].equals("")) break;

                final LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setLayoutParams(layoutParams2);
                if (filter.PrayerroomType[prayerroomtypeID[i][j]]) linearLayout2.setBackground(getDrawable(R.drawable.filterselected));
                else linearLayout2.setBackground(getDrawable(R.drawable.filterunselected));
                linearLayout2.setId(2100000+2000+prayerroomtypeID[i][j]);
                linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = view.getId();
                        if (id/1000 != 2102) {
                            System.out.println("Wrong ID");
                        }
                        prayerroomfiltertypeclick(id-2100000-2000);
                    }
                });

                final TextView textView = new TextView(this);
                textView.setLayoutParams(textParams);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setText(prayerroomtypestring[i][j]);
                if (filter.PrayerroomType[prayerroomtypeID[i][j]]) textView.setTextColor(Color.WHITE);
                else textView.setTextColor(Color.parseColor("#444444"));
                textView.setId(2100000+3000+prayerroomtypeID[i][j]);

                linearLayout2.addView(textView);
                linearLayout1.addView(linearLayout2);
            }
            filtertype.addView(linearLayout1);
        }
    }

    public void moreinformationclick(View view) {
        Intent intent = new Intent(this, MoreInformationActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void backarrowclick(View view) {
        onBackPressed();
    }
}