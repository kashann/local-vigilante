package com.project.kashan.localvigilante;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PetitionWorker extends AsyncTask<String, Integer, PetitionInfo> {
    @Override
    protected PetitionInfo doInBackground(String... strings) {
        if (strings == null || strings.length == 0 || "".equals(strings[0])) {
            return null;
        }
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try{
            URL url = new URL("https://api.myjson.com/bins/678xr");
            connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            String line = null;
            StringBuilder response = new StringBuilder();

            while((line = reader.readLine()) != null){
                response.append(line);
            }
            String json = response.toString();
            Log.d("JSON", json);

            PetitionInfo info = new PetitionInfo();
            JSONObject responseJSON = new JSONObject(json);
            JSONObject mainJSON = responseJSON.getJSONObject("main");

            int temp = mainJSON.getInt("noSignatures");
            info.noSignatures = temp;
            info.name = mainJSON.getString("name");
            info.body = mainJSON.getString("body");

            return info;
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }
}
