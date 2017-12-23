package com.example.anonymous.librarian.ServerProtocols;

import android.content.Context;
import android.os.AsyncTask;

import com.example.anonymous.librarian.ServerScriptsURL;
import com.example.anonymous.librarian.SubscriberAnalysis;
import com.example.anonymous.librarian.Subscribers;
import com.example.anonymous.librarian.ViewSubscriberListViewAdapter;
import com.example.anonymous.librarian.ViewSubscribers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * THIS CLASS USES ASYNC TASK TO
 * RECEIVE AND POST SUBSCRIBERS
 * INFORMATION TO THE SERVER
 *
 * @author ANONYMOUS
 * @version 1.0.0
 */
public class SubscriberProtocol {

    public static ArrayList<Subscribers> subscribers;
    public static Hashtable subscriberDetailDetail;
    public static ArrayList<SubscriberAnalysis> subscriberAnalysisArrayList;
    public static String deleteSubscriberResponse, updateSubscriberResponse, addSubscriberResponse;
    Context context;

    public SubscriberProtocol(Context context) {
        this.context = context;
    }

    /*
    * LIST OF PROTOCOLS:
    * ------------------------
    * Get Subscribers           //
    * Get Subscriber Analysis   //
    * Get Subscriber Details    //
    * Update Subscriber         //
    * Delete Subscriber         //
    * Add Subscriber
    * */

    /**
     * This method gets the list of subscribers from server
     *
     * @return ArrayList : list of subscribers
     */
    public static ArrayList<Subscribers> getSubscribers(){

        new GetSubscribers().execute();

        return subscribers;

    }

    /**
     * This method gets individual subscriber details from the server
     *
     * @param subscriberId : java.lang.String
     * @return Hashtable : return key value pair for details
     */
    public static Hashtable getSubscriberDetails(String subscriberId){

        return null;

    }

    /**
     * This method gets the subscriber analysis
     *
     * @param subscriberId : java.lang.String
     * @return ArrayList : returns list with the analysis
     */
    public static ArrayList<SubscriberAnalysis> getSubscriberAnalysis(String subscriberId){

        return null;

    }

    /**
     * This method deletes the subscriber from the server
     *
     * @param subscriberId : java.lang.String
     * @return java.lang.String : returns success or fail
     */
    public static String deleteSubscriber(String subscriberId){

        return null;

    }

    /**
     * This method updates the subscriber
     * with the values passed in as parameters
     *
     * @param subscriberId : java.lang.String
     * @param enrolledFor : java.lang.String
     * @param enrolledOn : java.lang.String
     * @param enrollmentType : java.lang.String
     * @param phone : java.lang.String
     * @param dob : java.lang.String
     * @return java.lang.String : returns success or fail
     */
    public static String updateSubscriber(String subscriberId, String enrolledFor, String enrolledOn, String enrollmentType, String phone, String dob){

        return null;

    }

    /**
     * This method adds subscriber to database
     * based on the data passed in as parameters
     *
     * @param name java.lang.String
     * @param enrolledFor java.lang.String
     * @param id java.lang.String
     * @param phone java.lang.String
     * @param dob java.lang.String
     * @param reb java.lang.String
     * @param leb java.lang.String
     * @param center java.lang.String
     * @param gender java.lang.String
     * @param enrollmentType java.lang.String
     * @param jointAccountWith java.lang.String
     * @param enrolledOn java.lang.String
     * @return java.lang.String : returns success or fail
     */
    public static String addSubscriber(String name, String enrolledFor, String id, String phone, String dob, String reb, String leb, String center, String gender, String enrollmentType, String jointAccountWith, String enrolledOn){

        return null;

    }

    // ------------------------------------------------------------------------------------- //
    // ------------------------ ASYNC TASKS ------------------------------------------------ //
    // ------------------------------------------------------------------------------------- //

    public static class GetSubscribers extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_SUBSCRIBERS_DETAILS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

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

            try {

                subscribers = new ArrayList<>();

                JSONArray root = new JSONArray(s);
                for(int i = 0; i < root.length(); i++){
                    JSONObject nthObject = root.getJSONObject(i);
                    String subscriber_name = nthObject.getString("subscriber_name");
                    String subscriber_id = nthObject.getString("subscriber_id");

                    Subscribers subscriber = new Subscribers();
                    subscriber.setmSubscriberId(subscriber_id);
                    subscriber.setmSubscriberName(subscriber_name);

                    subscribers.add(subscriber);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static class GetSubscriberAnalysis extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static class GetSubscriberDetails extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static class UpdateSubscriber extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static class AddSubscriber extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static class DeleteSubscriber extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
