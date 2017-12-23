package com.example.anonymous.librarian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class EditSubscriberDetails extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mNewSubscriberEnrolledOn, mNewSubscriberEnrolledFor, mNewSubscriberEnrollmentType, mNewSubscriberDOB, mNewSubscriberPhone;
    Button mSubmit, mCancel, mReset;
    public String oldId;
    public ProgressDialog progressDialog, deleteProgressDialog, getProgressDialog;
    NetworkChangeReceiver receiver;
    Boolean flag = false;
    ArrayList<String> subscribers;
    IntentFilter filter;
    FloatingActionButton editJointAccount;
    TextView currentJointAccount;
    SpinnerDialog spinnerDialog;

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
        setContentView(R.layout.activity_edit_subscriber_details);

        mToolbar = findViewById(R.id.edit_subscriber_detail_toolbar);
        setSupportActionBar(mToolbar);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        mNewSubscriberEnrolledOn = findViewById(R.id.edit_subscriber_detail_enrolled_on);
        mNewSubscriberEnrolledFor = findViewById(R.id.edit_subscriber_detail_enrolled_for);
        mNewSubscriberEnrollmentType = findViewById(R.id.edit_subscriber_detail_enrollment_type);
        mNewSubscriberDOB = findViewById(R.id.edit_subscriber_detail_dob);
        mNewSubscriberPhone = findViewById(R.id.edit_subscriber_detail_phone);

        editJointAccount = findViewById(R.id.fab_edit_subscriber);
        currentJointAccount = findViewById(R.id.edit_subscriber_detail_joint_account);

        mSubmit = findViewById(R.id.edit_subscriber_detail_submit);
        mCancel = findViewById(R.id.edit_subscriber_detail_cancel);
        mReset = findViewById(R.id.edit_subscriber_detail_reset);

        mNewSubscriberEnrolledOn.setText(getIntent().getStringExtra("enrolledOn"));
        mNewSubscriberEnrolledFor.setText(getIntent().getStringExtra("enrolledFor"));
        mNewSubscriberEnrollmentType.setText(getIntent().getStringExtra(("enrollmentType")));
        mNewSubscriberDOB.setText(getIntent().getStringExtra("dob"));
        mNewSubscriberPhone.setText(getIntent().getStringExtra("phone"));

        progressDialog.dismiss();
        new GetSubscribersForJACAsyncTask().execute();
        spinnerDialog = new SpinnerDialog(EditSubscriberDetails.this, subscribers, "Select Subscriber");

        new GetJointAccount().execute();

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
                currentJointAccount.setText(getIntent().getStringExtra("jointAccountEdited"));

            }
        });

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                Toast.makeText(EditSubscriberDetails.this, "" + s, Toast.LENGTH_SHORT).show();
            }
        });

        editJointAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
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
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            deleteSubscriber();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(EditSubscriberDetails.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
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

            final String UPDATE_DATA_URL = "http://fardeenpanjwani.com/librarian/update_subscriber_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL().UPDATE_SUBSCRIBER_DETAILS());

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
            final String DELETE_SUBCRIBER_URL = "http://fardeenpanjwani.com/librarian/delete_subscriber.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL().DELETE_SUBSCRIBER());

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

                Toast.makeText(EditSubscriberDetails.this, "Subscriber successfully deleted", Toast.LENGTH_SHORT).show();
                Intent toMainActivity = new Intent(EditSubscriberDetails.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);
            } else {
                deleteProgressDialog.dismiss();
                Toast.makeText(EditSubscriberDetails.this, "Sorry! Something went wrong.\n" + s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetJointAccount extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EditSubscriberDetails.this);
            progressDialog.setMessage("Getting Subscriber Joint Account Details...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL().GET_JOINT_ACCOUNT());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String data = URLEncoder.encode("subscriberId", "UTF-8") +"="+ URLEncoder.encode(getIntent().getStringExtra("subId"));

                bufferedWriter.write(data);
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
            Toast.makeText(EditSubscriberDetails.this, "" + s, Toast.LENGTH_SHORT).show();
            if(s.isEmpty()){

                Toast.makeText(EditSubscriberDetails.this, "Joint Account Not Available\nEditing of Joint Account has been Disabled.", Toast.LENGTH_SHORT).show();
                CoordinatorLayout linearLayout = findViewById(R.id.linear_layout);
                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) editJointAccount.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                editJointAccount.setLayoutParams(p);
                editJointAccount.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);

            } else {

                currentJointAccount.setText(s);

            }
        }
    }

    public class GetSubscribersForJACAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            // getProgressDialog = new ProgressDialog(EditSubscriberDetails.this);
            // getProgressDialog.setMessage("Loading subscribers..");
            // getProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            // final String GET_SUBSCRIBERS_URL = "http://fardeenpanjwani.com/librarian/get_subscribers_details.php";

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
            // getProgressDialog.dismiss();

            if(s.isEmpty()){
                Toast.makeText(EditSubscriberDetails.this, "The list seems to be empty", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    subscribers = new ArrayList<String>();
                    subscribers.add("NONE");

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){
                        JSONObject nthObject = root.getJSONObject(i);
                        String subscriber_name = nthObject.getString("subscriber_name");
                        subscribers.add(subscriber_name);
                    }

                    spinnerDialog = new SpinnerDialog(EditSubscriberDetails.this, subscribers, "Select Subscriber");
                    spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String s, int i) {
                            LinearLayout linearLayout = findViewById(R.id.joint_account_linear_layout);
                            linearLayout.setVisibility(View.VISIBLE);
                            TextView textView = findViewById(R.id.add_subscriber_jac_selected);
                            textView.setText(s);
                        }
                    });
                    findViewById(R.id.fab_edit_subscriber).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            spinnerDialog.showSpinerDialog();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

