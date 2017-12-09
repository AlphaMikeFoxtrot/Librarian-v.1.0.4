package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.librarian.CurrentlyIssuedToysAdapter.CurrentlyIssuedToysAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IssuedToyDetail extends AppCompatActivity {

    TextView mIssuedToyId, mIssuedToyName, mIssuedToyToId, mIssuedToyToName, mIssuedToyOn, mIssuedToyDueDate;
    Button mBack, mReturnToy;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        Intent toPreviousActivity = new Intent(this, ViewCurrentlyIssuedToys.class);
        toPreviousActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_toy_detail);

        mIssuedToyName = findViewById(R.id.issued_toy_details_toy_name);
        mIssuedToyId = findViewById(R.id.issued_toy_details_toy_id);
        mIssuedToyToId = findViewById(R.id.issued_toy_details_issued_to_id);
        mIssuedToyToName = findViewById(R.id.issued_toy_details_issued_to_name);
        mIssuedToyOn = findViewById(R.id.issued_toy_details_issued_on);
        mIssuedToyDueDate = findViewById(R.id.issued_toy_details_due_date);

        mReturnToy = findViewById(R.id.issued_toy_details_return_button);
        mBack = findViewById(R.id.issued_toy_details_back_button);

        mIssuedToyName.setText(getIntent().getStringExtra("toyName"));
        mIssuedToyId.setText(getIntent().getStringExtra("toyId"));
        mIssuedToyToName.setText(getIntent().getStringExtra("issuedToName"));
        mIssuedToyToId.setText(getIntent().getStringExtra("issuedToId"));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final Date startDate, startDateTwo;
        try {
            startDate = df.parse(getIntent().getStringExtra("issuedOn"));
            // mIssuedToyDueDate.setText(startDate.toString());
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(startDate);
            String dueDateOne = cal.getTime().toString();
            String[] dueDatesOne = dueDateOne.split(" ");
            mIssuedToyOn.setText(dueDatesOne[0] + " " + dueDatesOne[1] + " " + dueDatesOne[2] + " " + dueDatesOne[dueDatesOne.length - 1]);
            cal.add(Calendar.DAY_OF_MONTH, 15);
            // mIssuedToyDueDate.setText(cal.getTime().toString());
            String dueDate = cal.getTime().toString();
            String[] dueDates = dueDate.split(" ");
            mIssuedToyDueDate.setText(dueDates[0] + " " + dueDates[1] + " " + dueDates[2] + " " + dueDates[dueDates.length - 1]);
        } catch (ParseException e) {
            e.printStackTrace();
            mIssuedToyDueDate.setText("Unavailable");
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toIssuedToys = new Intent(IssuedToyDetail.this, ViewCurrentlyIssuedToys.class);
                toIssuedToys.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIssuedToys);

            }
        });

        mReturnToy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReturnToyAsyncTask returnToyAsyncTask = new ReturnToyAsyncTask();
                returnToyAsyncTask.execute(getIntent().getStringExtra("toyId"));

            }
        });
    }

    public class ReturnToyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(IssuedToyDetail.this);
            progressDialog.setMessage("Running return protocol....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String returnedToyId = strings[0];

            final String RETURN_TOY_URL = "http://fardeenpanjwani.com/librarian/return_toy.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(RETURN_TOY_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("toyId", "UTF-8") +"="+ URLEncoder.encode(returnedToyId, "UTF-8");

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
            if(s != null && s.contains("success")){

                Toast.makeText(IssuedToyDetail.this, "Toy Successfully returned!", Toast.LENGTH_SHORT).show();
                Intent toIssuedToys = new Intent(IssuedToyDetail.this, ViewCurrentlyIssuedToys.class);
                toIssuedToys.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIssuedToys);

            } else {

                // Toast.makeText(IssuedToyDetail.this, "There seems to be a problem with the server. Please try again after sometime!", Toast.LENGTH_SHORT).show();
                Toast.makeText(IssuedToyDetail.this, "" + s, Toast.LENGTH_SHORT).show();
                Intent toIssuedToys = new Intent(IssuedToyDetail.this, ViewCurrentlyIssuedToys.class);
                toIssuedToys.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIssuedToys);

            }
        }
    }
}
