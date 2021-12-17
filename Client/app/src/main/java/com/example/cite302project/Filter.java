package com.example.cite302project;

import java.io.Serializable;

public class Filter implements Serializable {
    public boolean Restaurant;
    public boolean Prayerroom;

    // -1 : No filter
    // 0 : Halal Certified
    // 1 : Self Certified
    // 2 : Muslim Friendly
    // 3 : Pork Free
    public int RestaurantMuslimFriendly;

    // Values :
    //          (_Empty_)   Arab        Asian       Buffet      Chinese
    //          Egyptian    French      Indian      Japanese    Korean Cuisine
    //          [Temple]    Malaysian   M.Eastern   Moroccan   Nepalese
    //          Pakistani   Russian     Tunisian    Turkish     Uzbek
    //          Western     [Room]
    public boolean[] RestaurantType;

    // Values :
    //          Hotel       Int.Airport Isl.Mosque  L.Hospital  Mosque
    //          Restaurant  TourSpot    T.I.Center  University  Others
    public boolean[] PrayerroomType;

    public Filter() {
        Restaurant = true;
        Prayerroom = true;

        RestaurantMuslimFriendly = -1; // 0-3
        RestaurantType = new boolean[22]; // 1-21

        PrayerroomType = new boolean[10]; // 0-9
    }

    public int filteringRestaurantElement(RestaurantElement element) {
        if (!Restaurant) return 0;

        boolean FilterMuslimFriendly = RestaurantMuslimFriendly >= 0;
        boolean FilterType = false;
        for (int i = 1; i < 22; i++) if (RestaurantType[i]) FilterType = true;

        if (!FilterMuslimFriendly && !FilterType) return 1;
        if (FilterMuslimFriendly && RestaurantMuslimFriendly < element.TypeMuslimFriendly) return 1;
        if (FilterType && (!RestaurantType[element.TypeFood1] && !RestaurantType[element.TypeFood2] && !RestaurantType[element.TypeFood3])) return 1;
        return 2;
    }

    public int filteringPrayerroomElement(PrayerroomElement element) {
        if (!Prayerroom) return 0;

        boolean FilterType = false;
        for (int i = 0; i < 10; i++) if (PrayerroomType[i]) FilterType = true;

        if (!FilterType) return 1;
        if (FilterType && (!PrayerroomType[element.TypePrayerroom])) return 1;
        return 2;
    }
}
