package com.example.anonymous.librarian;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ANONYMOUS on 18-Dec-17.
 */

public class IDsHandler {

    private String mIdToUpdate; // toy, book and subscriber
    private String mUpdatedId;  // incremented or decremented id
    private final String GET_URL = new ServerScriptsURL().GET_LAST_UPDATED_IDS();
    private final String UPDATE_URL = new ServerScriptsURL().UPDATE_EXISTING_IDS();
    public String response_id;

    public IDsHandler(String mIdToUpdate, String mUpdatedId) {
        this.mIdToUpdate = mIdToUpdate;
        this.mUpdatedId = mUpdatedId;
    }

    private String getExistingIDs(){

        switch (mIdToUpdate){

            case "toy":
                break;

            case "book":
                break;

            case "subscriber":
                break;
        }

        return response_id;

    }

    public class GetIDS extends AsyncTask<String, Void, String>{

        String type;

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(GET_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){

                    response.append(line);

                }

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "";
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            } finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {

            String toy, book, subscriber;

            if(!(s.isEmpty() || s.length() < 0)){

                try {

                    JSONObject root = new JSONObject(s);
                    toy = root.getString("last_updated_toy_id");
                    book = root.getString("last_updated_book_id");
                    subscriber = root.getString("last_updated_subscriber_id");

                    switch (type){

                        case "toy":
                            break;

                        case "book":
                            break;

                        case "subscriber":
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
