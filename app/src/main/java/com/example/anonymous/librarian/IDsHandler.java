package com.example.anonymous.librarian;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ANONYMOUS on 18-Dec-17.
 */

public class IDsHandler {

    private String mIdToUpdate; // toy, book and subscriber
    private String mUpdatedId;  // incremented or decremented id
    private final String GET_URL = new ServerScriptsURL().GET_LAST_UPDATED_IDS();
    private final String UPDATE_URL = new ServerScriptsURL().UPDATE_EXISTING_IDS();
    public String response_id;

    public IDsHandler(String mIdToUpdate) {
        this.mIdToUpdate = mIdToUpdate;
    }

    public String getExistingIDs(){

        switch (mIdToUpdate){

            case "toy":
                new GetIDS().execute(mIdToUpdate);
                break;

            case "book":
                new GetIDS().execute(mIdToUpdate);
                break;

            case "subscriber":
                new GetIDS().execute(mIdToUpdate);
                break;
        }

        return response_id;

    }

    public void UpdateIds(String updatedId){

        switch (mIdToUpdate){

            case "toy":
                new UpdateIDs().execute(mIdToUpdate, updatedId);
                break;

            case "book":
                new UpdateIDs().execute(mIdToUpdate, updatedId);
                break;

            case "subscriber":
                new UpdateIDs().execute(mIdToUpdate, updatedId);
                break;
        }

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
                            response_id = toy;
                            break;

                        case "book":
                            response_id = book;
                            break;

                        case "subscriber":
                            response_id = subscriber;
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class UpdateIDs extends AsyncTask<String, Void, String>{

        String type, updatedId;

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];
            updatedId = strings[1];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(UPDATE_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));

                String dataToWrite = URLEncoder.encode("idToUpdate", "UTF-8") +"="+ URLEncoder.encode(type, "UTF-8") +"&"+
                        URLEncoder.encode("updateId", "UTF-8") +"="+ URLEncoder.encode(updatedId, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
            }

            return null;

        }
    }
}
