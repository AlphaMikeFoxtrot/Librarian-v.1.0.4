package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class EditSubscriberDetails extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mNewSubscriberEnrolledOn, mNewSubscriberEnrolledFor, mNewSubscriberEnrollmentType, mNewSubscriberDOB, mNewSubscriberPhone;
    Button mSubmit, mCancel, mReset;
    public ProgressDialog progressDialog, deleteProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscriber_details);

        mToolbar = findViewById(R.id.edit_subscriber_detail_toolbar);
        setSupportActionBar(mToolbar);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        mNewSubscriberEnrolledOn = findViewById(R.id.edit_subscriber_detail_enrolled_on);
        mNewSubscriberEnrolledFor = findViewById(R.id.edit_subscriber_detail_enrolled_for);
        mNewSubscriberEnrollmentType = findViewById(R.id.edit_subscriber_detail_enrollment_type);
        mNewSubscriberDOB = findViewById(R.id.edit_subscriber_detail_dob);
        mNewSubscriberPhone = findViewById(R.id.edit_subscriber_detail_phone);

        mSubmit = findViewById(R.id.edit_subscriber_detail_submit);
        mCancel = findViewById(R.id.edit_subscriber_detail_cancel);
        mReset = findViewById(R.id.edit_subscriber_detail_reset);

        mNewSubscriberEnrolledOn.setText(getIntent().getStringExtra("enrolledOn"));
        mNewSubscriberEnrolledFor.setText(getIntent().getStringExtra("enrolledFor"));
        mNewSubscriberEnrollmentType.setText(getIntent().getStringExtra(("enrollmentType")));
        mNewSubscriberDOB.setText(getIntent().getStringExtra("dob"));
        mNewSubscriberPhone.setText(getIntent().getStringExtra("phone"));

        progressDialog.dismiss();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitClicked();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent back = new Intent(EditSubscriberDetails.this, ViewSubscribers.class);
                back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(back);

            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mNewSubscriberEnrolledOn.setText("");
                mNewSubscriberEnrolledFor.setText("");
                mNewSubscriberEnrollmentType.setText("");
                mNewSubscriberDOB.setText("");
                mNewSubscriberPhone.setText("");

                mNewSubscriberEnrolledOn.setText(getIntent().getStringExtra("enrolledOn"));
                mNewSubscriberEnrolledFor.setText(getIntent().getStringExtra("enrolledFor"));
                mNewSubscriberEnrollmentType.setText(getIntent().getStringExtra(("enrollmentType")));
                mNewSubscriberDOB.setText(getIntent().getStringExtra("dob"));
                mNewSubscriberPhone.setText(getIntent().getStringExtra("phone"));

            }
        });


    }

    private void submitClicked() {
        UpdateSusbscriberAsyncTask updateSusbscriberAsyncTask = new UpdateSusbscriberAsyncTask();
        updateSusbscriberAsyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_subscriber_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_delete){
            deleteSubscriber();
            // Toast.makeText(this, "Delete Protocol under development", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    private void deleteSubscriber() {
        DeleteSubscriberAsyncTask deleteSubscriberAsyncTask = new DeleteSubscriberAsyncTask();
        deleteSubscriberAsyncTask.execute();
    }

    public class UpdateSusbscriberAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EditSubscriberDetails.this);
            progressDialog.setMessage("Updating data");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String subscriberId = getIntent().getStringExtra("subId");

            final String UPDATE_DATA_URL = "https://suppliant-fives.000webhostapp.com/librarian/update_subscriber_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(UPDATE_DATA_URL);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                /*
                * $new_enrolled_for = $_POST["enrolledFor"];
                * $new_enrollment_type = $_POST["enrollmentType"];
                * $new_phone_number = $_POST["phone"];
                * $new_date_of_birth = $_POST["dob"];
                * $new_enrolled_on = $_POST["enrolledOn"];
                * */

                String dataToWrite = URLEncoder.encode("enrolledFor", "UTF-8") +"="+ URLEncoder.encode(mNewSubscriberEnrolledFor.getText().toString()) +"&"+
                        URLEncoder.encode("enrolledOn", "UTF-8") +"="+ URLEncoder.encode(mNewSubscriberEnrolledOn.getText().toString()) +"&"+
                        URLEncoder.encode("enrollmentType", "UTF-8") +"="+ URLEncoder.encode(mNewSubscriberEnrollmentType.getText().toString()) +"&"+
                        URLEncoder.encode("phone", "UTF-8") +"="+ URLEncoder.encode(mNewSubscriberPhone.getText().toString()) +"&"+
                        URLEncoder.encode("dob", "UTF-8") +"="+ URLEncoder.encode(mNewSubscriberDOB.getText().toString()) +"&"+
                        URLEncoder.encode("id", "UTF-8") +"="+ URLEncoder.encode(subscriberId);

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null){

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
                Toast.makeText(EditSubscriberDetails.this, "Subscriber Detail successfully updated", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(EditSubscriberDetails.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);

            } else {

                progressDialog.dismiss();
                Toast.makeText(EditSubscriberDetails.this, "Sorry! There seems to be a problem with the server!\n" + s, Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(EditSubscriberDetails.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);

            }
        }
    }

    public class DeleteSubscriberAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            deleteProgressDialog = new ProgressDialog(EditSubscriberDetails.this);
            deleteProgressDialog.setMessage("Deleting Subscriber");
            deleteProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String subId = getIntent().getStringExtra("subId");
            final String DELETE_SUBCRIBER_URL = "https://suppliant-fives.000webhostapp.com/librarian/delete_subscriber.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(DELETE_SUBCRIBER_URL);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("id", "UTF-8") +"="+ URLEncoder.encode(subId, "UTF-8");

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
                deleteProgressDialog.dismiss();
                Toast.makeText(EditSubscriberDetails.this, "Subscriber successfully delted", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(EditSubscriberDetails.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            } else {
                deleteProgressDialog.dismiss();
                Toast.makeText(EditSubscriberDetails.this, "Sorry! Something went wrong.\n" + s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
