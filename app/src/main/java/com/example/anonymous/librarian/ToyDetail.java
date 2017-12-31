package com.example.anonymous.librarian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class ToyDetail extends AppCompatActivity {

    TextView mToyName, mToyId, mToyAddeOn;
    Button mBack, mDelete;
    Toolbar mToolbar;
    ProgressDialog progressDialog;
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
        Intent toPreviousActivity = new Intent(ToyDetail.this, ViewToys.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_detail);

        mToolbar = findViewById(R.id.toy_detail_toolbar);
        setSupportActionBar(mToolbar);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mBack = findViewById(R.id.toy_detail_back_button);
        mDelete = findViewById(R.id.toy_detail_delete_button);

        mToyName = findViewById(R.id.toy_detail_toy_name);
        mToyId = findViewById(R.id.toy_detail_toy_id);
        mToyAddeOn = findViewById(R.id.toy_detail_toy_added_on);

        mToyName.setText(getIntent().getStringExtra("toyName"));
        mToyId.setText(getIntent().getStringExtra("toyId"));
        // mToyAddeOn.setText(getIntent().getStringExtra("addedOn"));

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = dateFormat.parse(getIntent().getStringExtra("addedOn"));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            String dateTwo = calendar.getTime().toString();
            String[] dates = dateTwo.split(" ");
            mToyAddeOn.setText(dates[0] + " " + dates[1] + " " + dates[2] + " " + dates[dates.length - 1]);
        } catch (ParseException e) {
            e.printStackTrace();
            mToyAddeOn.setText(getIntent().getStringExtra("addedOn"));
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toViewToys = new Intent(ToyDetail.this, ViewToys.class);
                toViewToys.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toViewToys);

            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                new DeleteToyProtocol().execute(mToyId.getText().toString());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ToyDetail.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
    }

    private class DeleteToyProtocol extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ToyDetail.this);
            progressDialog.setMessage("deleting toy");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String DELETE_BOOK_URL = "http://www.fardeenpanjwani.com/librarian/delete_toy.php";

            String toyId = strings[0];

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(ToyDetail.this).DELETE_TOY());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("toyId", "UTF-8") +"="+ URLEncoder.encode(toyId, "UTF-8");

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
            progressDialog.dismiss();
            if(s == null || s.contains("fail")){
                Toast.makeText(ToyDetail.this, "something went wrong when deleting the toy.\nPlease try again after sometime", Toast.LENGTH_LONG).show();
            } else if(s.contains("success")) {

                Toast.makeText(ToyDetail.this, "Toy successfully deleted.", Toast.LENGTH_SHORT).show();
                Intent toList = new Intent(ToyDetail.this, ViewToys.class);
                toList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toList);

            }
        }
    }
}
