package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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
import java.util.ArrayList;

public class SubscriberDetails extends AppCompatActivity {

    ProgressDialog progressDialog;
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
        mSubscriberMonth = findViewById(R.id.subscriber_detail_month_of_analysis);
        mSubscriberBooksActivity = findViewById(R.id.subscriber_detail_books_activity);
        mSubscriberToysActivity = findViewById(R.id.subscriber_detail_toys_activity);
        mSubscriberDailyBookActivity = findViewById(R.id.subscriber_detail_daily_book_activity);
        mSubscriberDailyToysActivity = findViewById(R.id.subscriber_detail_daily_toys_activity);

        mSubscriberPhoto = findViewById(R.id.subscriber_detail_image_view);

        GetSubscriberDetailsAsyncTask getSubscriberDetailsAsyncTask = new GetSubscriberDetailsAsyncTask();
        getSubscriberDetailsAsyncTask.execute(getIntent().getStringExtra("subscriberId"));

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
}
