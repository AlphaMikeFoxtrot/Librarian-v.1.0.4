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
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AddSubscriber extends AppCompatActivity {

    public EditText newName, newDOB, newPhone, newGender, newEnrolledFor, newEnrollmentType;
    TextView newId, newREB, newLEB, newCenter, textView;
    Button mSubmit, mCancel, mReset, mShow;
    // Spinner subscriberNamesForJAC;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog, generateIdProgressDialog;
    NetworkChangeReceiver receiver;
    ArrayList<String> subscribers;
    SpinnerDialog spinnerDialog;
    public String oldId;
    ArrayAdapter<String> adapter;
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
        editor.putString("subscriber_id", oldId);
        editor.commit();
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
        textView = findViewById(R.id.add_subscriber_jac_selected);

        sharedPreferences = getSharedPreferences("last_added_book_id", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        new GenerateSubscriberId().execute();

        mSubmit = findViewById(R.id.add_subscriber_submit);
        mCancel = findViewById(R.id.add_subscriber_cancel);
        mReset = findViewById(R.id.add_subscriber_reset);

        new GetSubscribersForJACAsyncTask().execute();
        spinnerDialog = new SpinnerDialog(AddSubscriber.this, subscribers, "Select Subscriber", R.style.DialogAnimations_SmileWindow);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("subscriber_id", oldId);
                editor.commit();
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
                newEnrolledFor.setText("");
                newEnrollmentType.setText("");

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar dob = Calendar.getInstance();
                    dob.setTime(sdf.parse(newDOB.getText().toString()));
                    if(getAge(dob) > 8 && (newEnrolledFor.getText().toString().toUpperCase().contains("TOYLIB") || newEnrolledFor.getText().toString().toUpperCase().contains("TL"))){

                        // no eligible for Toy Library
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        newEnrolledFor.setBackgroundResource(R.drawable.round_et_error);
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddSubscriber.this);
                        builder.setMessage("New subscriber is not eligible for Toy Library. Please change the enrollment type.").setPositiveButton("Okay", dialogClickListener)
                                .show();

                    } else {

                        AddSubscriberAsyncTask addSubscriberAsyncTask = new AddSubscriberAsyncTask();
                        addSubscriberAsyncTask.execute();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                Toast.makeText(AddSubscriber.this, "" + s, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });
    }

    public static int getAge(Calendar dob) throws Exception {
        Calendar today = Calendar.getInstance();

        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);

        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }

        return age;
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
                        URLEncoder.encode("jointAccountWith", "UTF-8") +"="+ URLEncoder.encode(textView.getText().toString()) +"&"+
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
                editor.putString("subscriber_id", oldId);
                editor.commit();
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
                    String subscriber_id = sharedPreferences.getString("subscriber_id", ""); // lastObject.getString("subscriber_id");
                    oldId = subscriber_id;
                    String[] ids = subscriber_id.split("/");
                    int incrementId = Integer.parseInt(ids[ids.length - 1]) + 1;
                    String generated_id = "SB/Lib/" + String.valueOf(incrementId);
                    newId.setText(generated_id);
                    editor.putString("subscriber_id", generated_id);
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                    editor.putString("subscriber_id", oldId);
                    editor.commit();
                }

            }
        }
    }

    public class GetSubscribersForJACAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddSubscriber.this);
            progressDialog.setMessage("Loading subscribers..");
            progressDialog.show();
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

            // Toast.makeText(ViewSubscribers.this, "" + s, Toast.LENGTH_LONG).show();
            // Snackbar.make(new CoordinatorLayout(ViewSubscribers.this), s, Snackbar.LENGTH_LONG).show();

            if(s.isEmpty()){
                progressDialog.dismiss();
                Toast.makeText(AddSubscriber.this, "The list seems to be empty", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                try {

                    subscribers = new ArrayList<String>();
                    subscribers.add("");
                    subscribers.add("NONE");

                    JSONArray root = new JSONArray(s);
                    for(int i = 0; i < root.length(); i++){
                        JSONObject nthObject = root.getJSONObject(i);
                        String subscriber_name = nthObject.getString("subscriber_name");
                        subscribers.add(subscriber_name);
                        // String subscriber_id = nthObject.getString("subscriber_id");

                        // Subscribers subscriber = new Subscribers();
                        // subscriber.setmSubscriberId(subscriber_id);
                        // subscriber.setmSubscriberName(subscriber_name);

                        // subscribers.add(subscriber);
                    }

                    // adapter = new ArrayAdapter<String>(AddSubscriber.this, android.R.layout.simple_dropdown_item_1line, subscribers);
                    // subscriberNamesForJAC.setAdapter(adapter);
                    spinnerDialog = new SpinnerDialog(AddSubscriber.this, subscribers, "Select Subscriber", R.style.DialogAnimations_SmileWindow);
                    // mListView.setAdapter(adapter);
                    spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String s, int i) {
                            // Toast.makeText(AddSubscriber.this, "" + s + "has been selected", Toast.LENGTH_LONG).show();
                            LinearLayout linearLayout = findViewById(R.id.joint_account_linear_layout);
                            linearLayout.setVisibility(View.VISIBLE);
                            // TextView textView = findViewById(R.id.add_subscriber_jac_selected);
                            textView.setText(s);
                        }
                    });
                    findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
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
