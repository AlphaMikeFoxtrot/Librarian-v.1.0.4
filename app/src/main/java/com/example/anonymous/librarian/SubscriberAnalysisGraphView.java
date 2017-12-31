package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SubscriberAnalysisGraphView extends AppCompatActivity {

    ProgressDialog progressDialog, progressDialogTwo;
    BarChart barChart;
    TextView monthlyToy, monthlyBook, totalToy, totalBook;
    public String MONTHLY_BOOK_ACTIVITY, MONTHLY_TOY_ACTIVITY, TOTAL_TOY_ACTIVITY, TOTAL_BOOK_ACTIVITY;
    NetworkChangeReceiver receiver;
    Boolean flag = false;
    IntentFilter filter;

    @Override
    protected void onStop() {
        super.onStop();
        if(flag) {
            unregisterReceiver(receiver);
            flag = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flag) {
            unregisterReceiver(receiver);
            flag = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_analysis_graph_view);
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        TextView toolbarTextView = findViewById(R.id.toolbar_text_view);
        toolbarTextView.setText("MONTHLY ANALYSIS : " + getIntent().getStringExtra("month").toUpperCase());

        Toolbar mToolbar = findViewById(R.id.analysis_toolbar);
        setSupportActionBar(mToolbar);

        barChart = findViewById(R.id.analysis_bar_graph);
        monthlyBook = findViewById(R.id.month_book_activity);
        monthlyToy = findViewById(R.id.month_toy_activity);
        totalBook = findViewById(R.id.total_book_activity);
        totalToy = findViewById(R.id.total_toy_activity);

        MONTHLY_TOY_ACTIVITY = "Books Taken in the month of " + getIntent().getStringExtra("month") + " : ";
        MONTHLY_BOOK_ACTIVITY = "Toys Taken in the month of " + getIntent().getStringExtra("month") + " : ";
        TOTAL_TOY_ACTIVITY = "Total number of toys taken : ";
        TOTAL_BOOK_ACTIVITY = "Total number of books taken : ";

        new GetAnalysisAsyncTask().execute();

        // HorizontalBarChart barChart= (HorizontalBarChart) findViewById(R.id.chart);

//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0, 4f));
//        entries.add(new BarEntry(1, 8f));

//        BarDataSet dataset = new BarDataSet(entries, "Books Toys");
//
//        final ArrayList<String> labels = new ArrayList<String>();
//        labels.add("Books");
//        labels.add("Toys");
//
//        BarData data = new BarData(dataset);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        barChart.setData(data);
//        barChart.animateY(1000);
    }

    public class GetAnalysisAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SubscriberAnalysisGraphView.this);
            progressDialog.setMessage("Getting analysis...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String subscriberId = getIntent().getStringExtra("subscriberId");
            String month = getIntent().getStringExtra("month");

            final String GET_ANALYSIS_URL = "http://fardeenpanjwani.com/librarian/get_individual_analysis.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(SubscriberAnalysisGraphView.this).GET_INDIVIDUAL_ANALYSIS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("month", "UTF-8") +"="+ URLEncoder.encode(month, "UTF-8") +"&"+
                        URLEncoder.encode("subscriber_id", "UTF-8") +"="+ URLEncoder.encode(subscriberId, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
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
            progressDialog.dismiss();
            if(s.length() > 0){

                try {

                    JSONArray root = new JSONArray(s);
                    JSONObject object = root.getJSONObject(0);
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(0, Float.parseFloat(object.getString("book_activity"))));
                    entries.add(new BarEntry(1, Float.parseFloat(object.getString("toy_activity"))));

                    BarDataSet dataSet = new BarDataSet(entries, "");

                    BarData data = new BarData(dataSet);
                    // int[] colors = new int[]{android.R.color.holo_red_dark, android.R.color.black};
                    int bookColor = android.R.color.holo_red_dark;
                    int toyColor = android.R.color.black;
                    int[] colors = new int[]{bookColor, toyColor};
                    dataSet.setColors(colors, SubscriberAnalysisGraphView.this);
                    barChart.setData(data);
                    barChart.setDrawGridBackground(false);
                    barChart.animateY(2000);

                    monthlyBook.setText(MONTHLY_BOOK_ACTIVITY + object.getString("book_activity"));
                    monthlyToy.setText(MONTHLY_TOY_ACTIVITY + object.getString("toy_activity"));

                    new TotalAnalysisAsyncTask().execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class TotalAnalysisAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialogTwo = new ProgressDialog(SubscriberAnalysisGraphView.this);
            progressDialogTwo.setMessage("Getting analysis...");
            progressDialogTwo.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String subscriber_id = getIntent().getStringExtra("subscriberId");

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            final String GET_TOTAL_URL = "http://www.fardeenpanjwani.com/librarian/get_total_analysis.php";

            try {

                URL url = new URL(new ServerScriptsURL(SubscriberAnalysisGraphView.this).GET_TOTAL_ANALYSIS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("subscriber_id", "UTF-8") +"="+ URLEncoder.encode(subscriber_id, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
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
            progressDialogTwo.dismiss();
            if(s.length() > 0){

                try {

                    JSONArray root = new JSONArray(s);
                    JSONObject object = root.getJSONObject(0);

                    totalBook.setText(TOTAL_BOOK_ACTIVITY + object.getString("books_activity"));
                    totalToy.setText(TOTAL_TOY_ACTIVITY + object.getString("toys_activity"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
