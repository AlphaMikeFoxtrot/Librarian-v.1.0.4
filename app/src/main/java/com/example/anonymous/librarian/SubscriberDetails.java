package com.example.anonymous.librarian;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;

public class SubscriberDetails extends AppCompatActivity {

    ProgressDialog progressDialog, progressDialogGetAnalysis;
    TextView mSubscriberName,
            mSubscriberId,
            mSubscriberJkCenter,
            mSubscriberREB,
            mSubscriberLEB,
            mSubscriberEnrollmentType,
            mSubscriberEnrolledFor,
            mSubscriberEnrolledOn,
            mSubscriberPhone,
            mSubscriberGender,
            mSubscriberDOB,
            mSubscriberMonth,
            mSubscriberBooksActivity,
            mSubscriberToysActivity,
            mSubscriberDailyBookActivity,
            mSubscriberDailyToysActivity;

    ImageView mSubscriberPhoto;

    Button mEditButton;

    SubscriberAnalysisAdapter adapter;

    ListView mListView;

    LinearLayout mLinearLayout;

    public ArrayList<SubscriberAnalysis> analysis = new ArrayList<>();

    JSONArray root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_details);

        mSubscriberName = findViewById(R.id.subscriber_detail_name);
        mSubscriberId = findViewById(R.id.subscriber_detail_id);
        mSubscriberEnrolledOn = findViewById(R.id.subscriber_detail_enrolled_on);
        mSubscriberEnrolledFor = findViewById(R.id.subscriber_detail_enrolled_for);
        mSubscriberEnrollmentType = findViewById(R.id.subscriber_detail_enrollment_type);
        mSubscriberLEB = findViewById(R.id.subscriber_detail_leb);
        mSubscriberREB = findViewById(R.id.subscriber_detail_reb);
        mSubscriberJkCenter = findViewById(R.id.subscriber_detail_center);
        mSubscriberPhone = findViewById(R.id.subscriber_detail_phone);
        mSubscriberGender = findViewById(R.id.subscriber_detail_gender);
        mSubscriberDOB = findViewById(R.id.subscriber_detail_dob);
        mSubscriberDailyBookActivity = findViewById(R.id.subscriber_detail_daily_book_activity);
        mSubscriberDailyToysActivity = findViewById(R.id.subscriber_detail_daily_toys_activity);

        mSubscriberPhoto = findViewById(R.id.subscriber_detail_image_view);

        mEditButton = findViewById(R.id.edit_button);

        mListView = findViewById(R.id.subscriber_detail_list_view);

        mLinearLayout = findViewById(R.id.subscriber_detail_analysis_layout);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        GetSubscriberDetailsAsyncTask getSubscriberDetailsAsyncTask = new GetSubscriberDetailsAsyncTask();
        getSubscriberDetailsAsyncTask.execute(getIntent().getStringExtra("subscriberId"));

        GetAnalysisAsyncTask getAnalysisAsyncTask = new GetAnalysisAsyncTask();
        getAnalysisAsyncTask.execute(getIntent().getStringExtra("subscriberId"));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // TODO

            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent toEdit = new Intent(SubscriberDetails.this, EditSubscriberDetails.class);
            toEdit.putExtra("subId", mSubscriberId.getText().toString());
            toEdit.putExtra("enrolledOn", mSubscriberEnrolledOn.getText().toString());
            toEdit.putExtra("enrolledFor", mSubscriberEnrolledFor.getText().toString());
            toEdit.putExtra("enrollmentType", mSubscriberEnrollmentType.getText().toString());
            toEdit.putExtra("dob", mSubscriberDOB.getText().toString());
            toEdit.putExtra("phone", mSubscriberPhone.getText().toString());
            startActivity(toEdit);


            }
        });

    }

    public class GetSubscriberDetailsAsyncTask extends AsyncTask<String, Void, String>{

        public String subscriberId;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SubscriberDetails.this);
            progressDialog.setMessage("Getting subscriber details");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            subscriberId = strings[0];

            final String GET_SUBSCRIBER_URL = "https://suppliant-fives.000webhostapp.com/librarian/get_individual_subscriber_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(GET_SUBSCRIBER_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("subscriberId", "UTF-8") +"="+ URLEncoder.encode(subscriberId, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

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
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
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
            if(s.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(SubscriberDetails.this, "Sorry! There seems to be a problem with the server", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                try {

                    JSONObject root = new JSONObject(s);
                    String subscriber_reb = root.getString("subscriber_regional_education_board");
                    String subscriber_leb = root.getString("subscriber_local_education_board");
                    String subscriber_center = root.getString("subscriber_center");
                    String subscriber_enrollment_type = root.getString("subscriber_enrollement_type");
                    String subscriber_enrolled_for = root.getString("subscriber_enrolled_for");
                    String subscriber_enrolled_on = root.getString("subscriber_enrolled_on");
                    String subscriber_date_of_birth = root.getString("subscriber_date_of_birth");
                    String subscriber_gender = root.getString("subscriber_gender");
                    String subscriber_phone = root.getString("subscriber_phone");
                    String subscriber_book_activity = root.getString("books_activity");
                    String subscriber_toys_activity = root.getString("toys_activity");
                    String subscriber_name = root.getString("subscriber_name");

                    mSubscriberName.setText(subscriber_name);
                    mSubscriberId.setText(subscriberId);
                    mSubscriberEnrolledOn.setText(subscriber_enrolled_on);
                    mSubscriberEnrolledFor.setText(subscriber_enrolled_for);
                    mSubscriberEnrollmentType.setText(subscriber_enrollment_type);
                    mSubscriberLEB.setText(subscriber_leb);
                    mSubscriberREB.setText(subscriber_reb);
                    mSubscriberJkCenter.setText(subscriber_center);
                    mSubscriberPhone.setText(subscriber_phone);
                    mSubscriberGender.setText(subscriber_gender);
                    mSubscriberDOB.setText(subscriber_date_of_birth);
                    mSubscriberDailyBookActivity.setText(subscriber_book_activity);
                    mSubscriberDailyToysActivity.setText(subscriber_toys_activity);

                    mSubscriberPhoto.setImageResource(R.drawable.no_image);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubscriberDetails.this, "Sorry! The server seems to be down at the moment\nPlease try again after some time.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class GetAnalysisAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialogGetAnalysis = new ProgressDialog(SubscriberDetails.this);
            progressDialogGetAnalysis.setMessage("Getting Analysis..");
            progressDialogGetAnalysis.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String subscriberId = strings[0];

            final String GET_ANALYSIS_URL = "https://suppliant-fives.000webhostapp.com/librarian/get_subscriber_analysis.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(GET_ANALYSIS_URL);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");

                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("subscriberId", "UTF-8") +"="+ URLEncoder.encode(subscriberId, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

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
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                mLinearLayout.setVisibility(View.GONE);
                return null;
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
            if(s.isEmpty() || s == null || s.contains("empty")){
                progressDialogGetAnalysis.dismiss();
                mLinearLayout.setVisibility(View.GONE);
                LinearLayout linearLayout = findViewById(R.id.empty_list_linear_layout);
                TextView textView = findViewById(R.id.empty_list_message);
                textView.setText("Sorry! Monthly Analysis is not available at the moment");
                linearLayout.setVisibility(View.VISIBLE);
            } else {

                progressDialogGetAnalysis.dismiss();

                try {

                    root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++) {
                        JSONObject nthObject = root.getJSONObject(i);
                        String month = nthObject.getString("month");
                        String book_activity = nthObject.getString("book_activity");
                        String toy_activity = nthObject.getString("toy_activity");

                        SubscriberAnalysis subscriberAnalysis = new SubscriberAnalysis();
                        subscriberAnalysis.setmMonthOfAnalysis(month);
                        subscriberAnalysis.setmNumberOfBooks(book_activity);
                        subscriberAnalysis.setmNumberOfToys(toy_activity);

                        analysis.add(subscriberAnalysis);
                    }

                    adapter = new SubscriberAnalysisAdapter(SubscriberDetails.this, analysis);
                    mListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
