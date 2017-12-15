package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddSubscriber extends AppCompatActivity {

    public EditText newName, newDOB, newPhone, newGender, newREB, newLEB, newCenter, newEnrolledFor, newEnrollmentType;
    TextView newId;
    Button mSubmit, mCancel, mReset;
    ProgressDialog progressDialog, generateIdProgressDialog;
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
    public void onBackPressed() {
        Intent toPreviousActivity = new Intent(this, ViewSubscribers.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscriber);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        newName = findViewById(R.id.add_subscriber_name);
        newId = findViewById(R.id.add_subscriber_id);
        newPhone = findViewById(R.id.add_subscriber_phone);
        newDOB = findViewById(R.id.add_subscriber_dob);
        newGender = findViewById(R.id.add_subscriber_gender);
        newREB = findViewById(R.id.add_subscriber_reb);
        newLEB = findViewById(R.id.add_subscriber_leb);
        newCenter = findViewById(R.id.add_subscriber_center);
        newEnrolledFor = findViewById(R.id.add_subscriber_enrolled_for);
        newEnrollmentType = findViewById(R.id.add_subscriber_enrollment_type);

        new GenerateSubscriberId().execute();

        mSubmit = findViewById(R.id.add_subscriber_submit);
        mCancel = findViewById(R.id.add_subscriber_cancel);
        mReset = findViewById(R.id.add_subscriber_reset);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toSubscriberList = new Intent(AddSubscriber.this, ViewSubscribers.class);
                toSubscriberList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toSubscriberList);

            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newName.setText("");
                // newId.setText("");
                newPhone.setText("");
                newDOB.setText("");
                newGender.setText("");
                newREB.setText("");
                newLEB.setText("");
                newCenter.setText("");
                newEnrolledFor.setText("");
                newEnrollmentType.setText("");

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddSubscriberAsyncTask addSubscriberAsyncTask = new AddSubscriberAsyncTask();
                addSubscriberAsyncTask.execute();

            }
        });
    }

    public class AddSubscriberAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddSubscriber.this);
            progressDialog.setMessage("Adding Subscriber");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            final String ADD_SUBSCRIBER_URL = "http://fardeenpanjwani.com/librarian/add_subscriber.php";

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().ADD_SUBSCRIBER());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                String dataToWrite = URLEncoder.encode("name", "UTF-8") +"="+ URLEncoder.encode(newName.getText().toString()) +"&"+
                        URLEncoder.encode("enrolledFor", "UTF-8") +"="+ URLEncoder.encode(newEnrolledFor.getText().toString()) +"&"+
                        URLEncoder.encode("id", "UTF-8") +"="+ URLEncoder.encode(newId.getText().toString()) +"&"+
                        URLEncoder.encode("phone", "UTF-8") +"="+ URLEncoder.encode(newPhone.getText().toString()) +"&"+
                        URLEncoder.encode("dob", "UTF-8") +"="+ URLEncoder.encode(newDOB.getText().toString()) +"&"+
                        URLEncoder.encode("reb", "UTF-8") +"="+ URLEncoder.encode(newREB.getText().toString()) +"&"+
                        URLEncoder.encode("leb", "UTF-8") +"="+ URLEncoder.encode(newLEB.getText().toString()) +"&"+
                        URLEncoder.encode("center", "UTF-8") +"="+ URLEncoder.encode(newCenter.getText().toString()) +"&"+
                        URLEncoder.encode("gender", "UTF-8") +"="+ URLEncoder.encode(newGender.getText().toString()) +"&"+
                        URLEncoder.encode("enrollmentType", "UTF-8") +"="+ URLEncoder.encode(newEnrollmentType.getText().toString()) +"&"+
                        URLEncoder.encode("enrolledOn", "UTF-8") +"="+ URLEncoder.encode(currentDateTimeString);

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
            if(s.contains("success")){
                progressDialog.dismiss();
                Toast.makeText(AddSubscriber.this, "new Subscriber successfully added", Toast.LENGTH_SHORT).show();
                Intent toSubscriberList = new Intent(AddSubscriber.this, ViewSubscribers.class);
                toSubscriberList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toSubscriberList);
            } else {
                progressDialog.dismiss();
                Toast.makeText(AddSubscriber.this, "Something Went Wrong\n" + s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GenerateSubscriberId extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            generateIdProgressDialog = new ProgressDialog(AddSubscriber.this);
            generateIdProgressDialog.setMessage("Generating Id...");
            generateIdProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String GET_SUBSCRIBERS = "http://www.fardeenpanjwani.com/librarian/get_subscribers_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_SUBSCRIBERS_DETAILS());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

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
                return null; // "MalformedURLException";
            } catch (IOException e) {
                e.printStackTrace();
                newId.setText(e.toString());
                return null; // "IOException\n" + e.toString();
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
            generateIdProgressDialog.dismiss();

            // Toast.makeText(AddSubscriber.this, "" + s, Toast.LENGTH_SHORT).show();

            if(s == null){

                // Toast.makeText(AddSubscriber.this, "s == null", Toast.LENGTH_SHORT).show();
                // TODO :
                // Toast.makeText(AddSubscriber.this, "", Toast.LENGTH_SHORT).show();

            } else {

                try {

                    JSONArray root = new JSONArray(s);
                    JSONObject lastObject = root.getJSONObject(root.length() - 1);
                    String subscriber_id = lastObject.getString("subscriber_id");
                    String[] ids = subscriber_id.split("/");
                    int incrementId = Integer.parseInt(ids[ids.length - 1]) + 1;
                    String generated_id = "SB/Lib/" + String.valueOf(incrementId);
                    newId.setText(generated_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
