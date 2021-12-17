package com.example.cite302project;

import org.json.JSONException;
import org.json.JSONObject;

public class PrayerroomElement extends Element {
    public int TypePrayerroom;

    public String InfoPhonenumber;
    public boolean InfoPermanent;
    public boolean InfoGenderdivision;
    public boolean InfoQuran;
    public boolean InfoCarpet;
    public boolean InfoKibla;
    public boolean InfoFeetwashing;

    // public String OperatingHour;

    public void fillPrayerroomElement(JSONObject jsonObject) throws JSONException {
        ID = jsonObject.getInt("ID");
        JsonString = jsonObject.toString();

        NameEnglish = jsonObject.getString("name_english");
        // NameKorean

        TypePrayerroom = jsonObject.getInt("type_prayerroom");

        AddressEnglish = jsonObject.getString("address_english");
        // AddressKorean

        Lat = jsonObject.getDouble("lat");
        Lng = jsonObject.getDouble("lng");

        InfoPhonenumber = jsonObject.getString("info_phonenumber");
        InfoPermanent = jsonObject.getInt("info_permanent") == 1;
        InfoGenderdivision = jsonObject.getInt("info_genderdivision") == 1;
        InfoQuran = jsonObject.getInt("info_quran") == 1;
        InfoCarpet = jsonObject.getInt("info_carpet") == 1;
        InfoKibla = jsonObject.getInt("info_kibla") == 1;
        InfoFeetwashing = jsonObject.getInt("info_feetwashing") == 1;

        // OperatingHour
    }

    public String getTypePrayerroom() {
        String[] TypePrayerroomList = {"Hotel", "International Airport", "Islamic Mosque",
                "Hospital", "Mosque", "Restaurant", "Tour Spot", "Tourist Information Center",
                "University", ""};
        return TypePrayerroomList[TypePrayerroom];
    }
}
