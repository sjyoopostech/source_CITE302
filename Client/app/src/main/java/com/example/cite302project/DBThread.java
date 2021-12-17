package com.example.cite302project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DBThread extends Thread {
    public String postfix;
    private String line;

    public DBThread(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public void run() {
        try {
            // localhost of android : 10.0.2.2
            URL url = new URL("http://10.0.2.2:3001/" + postfix);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            line = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getJsonString() {
        return line;
    }
}
