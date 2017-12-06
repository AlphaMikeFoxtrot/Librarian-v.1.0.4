package com.example.anonymous.librarian;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ANONYMOUS on 06-Dec-17.
 */

public class IssueItemCancelProtocol extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        final String CANCEL_PROTOCOL_URL = "https://forlibrariandatabasetwo.000webhostapp.com/librarian/issue_cancel_protocol.php";

        String cancelled_item = strings[0];

        HttpURLConnection httpURLConnection = null;
        BufferedWriter bufferedWriter = null;

        try {

            URL url = new URL(CANCEL_PROTOCOL_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            String dataToWrite = URLEncoder.encode("cancelled_item", "UTF-8") +"="+ URLEncoder.encode(cancelled_item, "UTF-8");

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
