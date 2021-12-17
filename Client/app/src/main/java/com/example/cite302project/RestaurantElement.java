package com.example.cite302project;

import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantElement extends Element {
    public int TypeMuslimFriendly;
    public int TypeFood1;
    public int TypeFood2;
    public int TypeFood3;

    public String InfoPhonenumber;
    public int InfoSeatnumber;
    public boolean InfoParking;
    public boolean InfoWudu;
    public boolean InfoRug;
    public boolean InfoGenderwashroom;
    public boolean InfoHookah;
    public boolean InfoAlcoholfree;
    public boolean InfoMuslimcooks;
    public boolean InfoHalalingredient;

    // public String OperatingHour;
    // public String CloseDate;

    public void fillRestaurantElement(JSONObject jsonObject) throws JSONException {
        ID = jsonObject.getInt("ID");
        JsonString = jsonObject.toString();

        NameEnglish = jsonObject.getString("name_english");
        NameKorean = jsonObject.getString("name_korean");

        TypeMuslimFriendly = jsonObject.getInt("type_muslimfriendly");
        TypeFood1 = jsonObject.getInt("type_food1");
        TypeFood2 = jsonObject.getInt("type_food2");
        TypeFood3 = jsonObject.getInt("type_food3");

        AddressEnglish = jsonObject.getString("address_english");
        AddressKorean = jsonObject.getString("address_korean");

        Lat = jsonObject.getDouble("lat");
        Lng = jsonObject.getDouble("lng");

        InfoPhonenumber = jsonObject.getString("info_phonenumber");
        InfoSeatnumber = jsonObject.getInt("info_seatnumber");
        InfoParking = jsonObject.getInt("info_parking") == 1;
        InfoWudu = jsonObject.getInt("info_wudu") == 1;
        InfoRug = jsonObject.getInt("info_rug") == 1;
        InfoGenderwashroom = jsonObject.getInt("info_genderwashroom") == 1;
        InfoHookah = jsonObject.getInt("info_hookah") == 1;
        InfoAlcoholfree = jsonObject.getInt("info_alcoholfree") == 1;
        InfoMuslimcooks = jsonObject.getInt("info_muslimcooks") == 1;
        InfoHalalingredient = jsonObject.getInt("info_halalingredient") == 1;

        // OperatingHour
        // CloseDate
    }

    public String getTypeFood(int num) {
        String[] TypeFoodList = {"", "Arab", "Asian", "Buffet", "Chinese", "Egyptian",
                "French", "Indian", "Japanese", "Korean Cuisine", "Korean (Temple Food)",
                "Malaysian", "Middle Eastern", "Moroccan", "Nepalese", "Pakistani",
                "Russian", "Tunisian", "Turkish", "Uzbek", "Western", "Western, Room Service"};
        if (num == 1) return TypeFoodList[TypeFood1];
        else if (num == 2) return TypeFoodList[TypeFood2];
        else return TypeFoodList[TypeFood3];
    }

    public String getTypeMuslimFriendly() {
        String[] TypeMuslimFriendlyList = {"Halal Certified", "Self Certified",
                "Muslim Friendly", "Pork Free"};
        return TypeMuslimFriendlyList[TypeMuslimFriendly];
    }
}
