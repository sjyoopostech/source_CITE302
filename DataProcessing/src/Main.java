import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class Main {

    public static String getEnglish(String str) {
        if (str.charAt(str.length()-1) != ')') return "";
        int stack = 1;
        for (int i = str.length()-2; i >= 0; i--) {
            if (str.charAt(i) == ')') stack++;
            else if (str.charAt(i) == '(') stack--;
            if (stack == 0) return str.substring(0, i);
        }
        return "";
    }

    public static String getKorean(String str) {
        if (str.charAt(str.length()-1) != ')') return "";
        int stack = 1;
        for (int i = str.length()-2; i >= 0; i--) {
            if (str.charAt(i) == ')') stack++;
            else if (str.charAt(i) == '(') stack--;
            if (stack == 0) return str.substring(i+1, str.length()-1);
        }
        return "";
    }

    public static int getTypeMuslimFriendly(String str) {
        switch (str) {
            case "Halal Certified" : return 0;
            case "Self Certified" : return 1;
            case "Muslim Friendly" : return 2;
            case "Pork Free" : return 3;
            default : return -1;
        }
    }

    public static int getTypeFood(String str) {
        switch (str) {
            case "" : return 0;
            case "Arab" : return 1;
            case "Asian" : return 2;
            case "Buffet" : return 3;
            case "Chinese" : return 4;
            case "Egyptian" : return 5;
            case "French" : return 6;
            case "Indian" : return 7;
            case "Japanese" : return 8;
            case "Korean Cuisine" : return 9;
            case "Korean (Temple Food)" : return 10;
            case "Malaysian" : return 11;
            case "Middle Eastern" : return 12;
            case "Moroccan" : return 13;
            case "Nepalese" : return 14;
            case "Pakistani" : return 15;
            case "Russian" : return 16;
            case "Tunisian" : return 17;
            case "Turkish" : return 18;
            case "Uzbekista" : return 19;
            case "Western" : return 20;
            case "Western (Room Service)" : return 21;
            default : return -1;
        }
    }

    public static int getTypePrayerRoom(String str) {
        switch (str) {
            case "Hotel" : return 0;
            case "International Airport" : return 1;
            case "Islamic Mosque" : return 2;
            case "Large Hospital" : return 3;
            case "Mosque" : return 4;
            case "Restaurant" : return 5;
            case "Tour Spot" : return 6;
            case "Tourist Information Center" : return 7;
            case "University" : return 8;
            case "Others" : return 9;
            default : return -1;
        }
    }



    public static void main(String[] args) throws IOException {

        // Restaurant
        /*
        String inputPath = "data/Muslim_Friendly_Restaurants_2021-11-27.xlsx";
        InputStream inputStream = new FileInputStream(inputPath);
        Workbook inputWorkbook = WorkbookFactory.create(inputStream);
        Sheet inputSheet = inputWorkbook.getSheetAt(0);

        String outputPath = "data/Restaurant.csv";
        OutputStream outputStream = new FileOutputStream(outputPath);
        XSSFWorkbook outputWorkbook = new XSSFWorkbook();
        XSSFSheet outputSheet = outputWorkbook.createSheet("Restuarant");

        Iterator<Row> rowIterator = inputSheet.rowIterator();
        Row inputRow = rowIterator.next();
        XSSFRow outputRow;

        int outputRowNum = 0;
        while (rowIterator.hasNext()) {
            inputRow = rowIterator.next();
            outputRow = outputSheet.createRow(outputRowNum);
            outputRowNum++;

            // Restaurant Name (0, 0-1)
            String dataRestaurantName = inputRow.getCell(0).getStringCellValue();
            outputRow.createCell(0).setCellValue(getEnglish(dataRestaurantName));
            outputRow.createCell(1).setCellValue(getKorean(dataRestaurantName));

            // Muslim Friendly (1, 2)
            String dataMuslimFriendly = inputRow.getCell(1).getStringCellValue();
            outputRow.createCell(2).setCellValue(getTypeMuslimFriendly(dataMuslimFriendly));

            // Food Type (3-5, 3-5)
            for (int i = 0; i < 3; i++) {
                String dataFoodType = inputRow.getCell(i+3).getStringCellValue();
                outputRow.createCell(i+3).setCellValue(getTypeFood(dataFoodType));
            }

            // Address (6, 6-7)
            String dataAddress = inputRow.getCell(6).getStringCellValue();
            outputRow.createCell(6).setCellValue(getEnglish(dataAddress));
            outputRow.createCell(7).setCellValue(getKorean(dataAddress));

            // LatLng (6, 8-9)
            try{
                int responseCode = 0;
                URL url = new URL("http://api.vworld.kr/req/address");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");

                String keyword = getKorean(dataAddress);
                String text_content =  URLEncoder.encode(keyword.toString(), "utf-8");

                // post request
                String postParams = "service=address";
                postParams += "&request=getcoord";
                postParams += "&version=2.0";
                postParams += "&crs=EPSG:4326";
                postParams += "&address="+text_content;
                postParams += "&arefine=true";
                postParams += "&simple=false";
                postParams += "&format=json";
                postParams += "&type=road";
                postParams += "&errorFormat=json";
                postParams += "&key=XXXXXXXX";

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                responseCode = con.getResponseCode();
                BufferedReader br;

                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }else{  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                String inputLine;
                String response = new String();

                while ((inputLine = br.readLine()) != null) {
                    response += inputLine;
                }

                JSONParser parser = new JSONParser();
                JSONObject jsonObject;
                jsonObject = (JSONObject)parser.parse(response);
                jsonObject = (JSONObject) jsonObject.get("response");
                jsonObject = (JSONObject) jsonObject.get("result");
                jsonObject = (JSONObject) jsonObject.get("point");
                double latitude = Double.parseDouble(jsonObject.get("x").toString());
                double longitude = Double.parseDouble(jsonObject.get("y").toString());

                outputRow.createCell(8).setCellValue(latitude);
                outputRow.createCell(9).setCellValue(longitude);

                br.close();
                con.disconnect();
            }catch(Exception e){
                e.printStackTrace();

                outputRow.createCell(8).setCellValue(-1);
                outputRow.createCell(9).setCellValue(-1);
            }

            // Phone Number (7, 10)
            String dataPhoneNumber = inputRow.getCell(7).getStringCellValue();
            outputRow.createCell(10).setCellValue(dataPhoneNumber);

            // Operating Hour & Closed Date

            // Seat Number (11, 11)
            int dataSeatNumber = (int) inputRow.getCell(11).getNumericCellValue();
            outputRow.createCell(11).setCellValue(dataSeatNumber);

            // Additional Info (12-19, 12-19)
            for (int i = 0; i < 8; i++) {
                String dataExtra = inputRow.getCell(i+12).getStringCellValue();
                outputRow.createCell(i+12).setCellValue(dataExtra.equals("O"));
            }
        }

        outputWorkbook.write(outputStream);
        inputStream.close();
        outputStream.close();
         */

        // Mosque
        /*
        String inputPath = "data/Mosque_original_2021-11-27.csv";
        InputStream inputStream = new FileInputStream(inputPath);
        Workbook inputWorkbook = WorkbookFactory.create(inputStream);
        Sheet inputSheet = inputWorkbook.getSheetAt(0);

        String outputPath = "data/Mosque.csv";
        OutputStream outputStream = new FileOutputStream(outputPath);
        XSSFWorkbook outputWorkbook = new XSSFWorkbook();
        XSSFSheet outputSheet = outputWorkbook.createSheet("Mosque");

        Iterator<Row> rowIterator = inputSheet.rowIterator();
        Row inputRow = rowIterator.next();
        XSSFRow outputRow;

        int outputRowNum = 0;
        while (rowIterator.hasNext()) {
            inputRow = rowIterator.next();
            outputRow = outputSheet.createRow(outputRowNum);
            outputRowNum++;
            System.out.println(outputRowNum);

            // ID (-, 0)
            outputRow.createCell(0).setCellValue(outputRowNum);

            // Restaurant Name (0, 1)
            String dataPrayerRoomName = inputRow.getCell(0).getStringCellValue();
            outputRow.createCell(1).setCellValue(dataPrayerRoomName);

            // Type (1, 2)
            String dataPrayerRoomType = inputRow.getCell(1).getStringCellValue();
            outputRow.createCell(2).setCellValue(getTypePrayerRoom(dataPrayerRoomType));

            // Address (2, 3)
            String dataEnglishAddress = inputRow.getCell(2).getStringCellValue();
            outputRow.createCell(3).setCellValue(dataEnglishAddress);

            // LatLng (2, 4-5)
            try{
                String EngAddr = dataEnglishAddress.replace(' ', '+');


                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + EngAddr + "&key=XXXXXXXX");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");

                int ResponseCode = con.getResponseCode();
                if (ResponseCode == 400 || ResponseCode == 401 || ResponseCode == 500 ) {
                    System.out.println(ResponseCode + " Error!");
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject;
                    JSONArray jsonArray;

                    jsonObject = (JSONObject)parser.parse(sb.toString());
                    jsonArray = (JSONArray) jsonObject.get("results");
                    jsonObject = (JSONObject) jsonArray.get(0);
                    jsonObject = (JSONObject) jsonObject.get("geometry");
                    jsonObject = (JSONObject) jsonObject.get("location");
                    double latitude = Double.parseDouble(jsonObject.get("lat").toString());
                    double longitude = Double.parseDouble(jsonObject.get("lng").toString());


                    outputRow.createCell(4).setCellValue(latitude);
                    outputRow.createCell(5).setCellValue(longitude);
                }
            }catch(Exception e){
                e.printStackTrace();
                outputRow.createCell(4).setCellValue(-1);
                outputRow.createCell(5).setCellValue(-1);
            }


            // Phone Number (3, 6)
            if (inputRow.getCell(3) != null) {
                String dataPhoneNumber = inputRow.getCell(3).getStringCellValue();
                outputRow.createCell(6).setCellValue(dataPhoneNumber);
            }

            // Operating Hour

            // Additional Info (5-10, 7-12)
            for (int i = 0; i < 6; i++) {
                int dataExtraInfo = (int) inputRow.getCell(5+i).getNumericCellValue();
                outputRow.createCell(7+i).setCellValue(dataExtraInfo);
            }
        }
        outputWorkbook.write(outputStream);
        inputStream.close();
        outputStream.close();

         */
    }
}
