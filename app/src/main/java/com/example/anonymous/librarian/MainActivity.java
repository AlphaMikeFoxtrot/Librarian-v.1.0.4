package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private MainActivityAdapter adapter;
    public boolean isLastDay;
    ProgressDialog lastDayProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String actualDate = dateFormat.format(date);
        String enhancedDate = actualDate.replace("/", " ");
        String[] splitDate = enhancedDate.split(" ");

        isLastDay = checkLastDay(splitDate[2]);

        if(isLastDay){
            lastDayProtocol();
        }

        mListView = findViewById(R.id.main_activity_list_view);
        ArrayList<MainActivityListViewItems> listItems = new ArrayList<>();

        listItems.add(new MainActivityListViewItems("Issue a Book", R.drawable.issue_book));
        listItems.add(new MainActivityListViewItems("Register Returned Book", R.drawable.return_book));
        listItems.add(new MainActivityListViewItems("View subscribers Details", R.drawable.subscribers));

        adapter = new MainActivityAdapter(getApplicationContext(), listItems);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){

                    case 0:
                        // issue a book clicked
                        issueBook();
                        break;

                    case 1:
                        // return book clicked
                        returnBook();
                        break;

                    case 2:
                        // view subscribers clicked
                        viewSubscribers();
                        break;

                }

            }
        });

    }

    private void viewSubscribers() {
        // TODO : initiate intent
        Intent toViewSubscribers = new Intent(MainActivity.this, ViewSubscribers.class);
        startActivity(toViewSubscribers);
    }

    private void lastDayProtocol() {

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        String month = monthFormat .format(new Date());

        // TODO : add method to insert activities of all the subscribers into table
        LastDayProtocolAsyncTask lastDayProtocolAsyncTask = new LastDayProtocolAsyncTask();
        lastDayProtocolAsyncTask.execute(month);

    }

    private void returnBook() {
        // TODO : create and execute intent
        Intent toReturnBook = new Intent(MainActivity.this, ReturnBook.class);
        startActivity(toReturnBook);
    }

    private void issueBook() {
        // TODO : create and execute intent
        Intent toIssueBookPhaseOne = new Intent(MainActivity.this, IssueBookPhaseOne.class);
        startActivity(toIssueBookPhaseOne);
    }

    public boolean checkLastDay(String date){

        int monthLength = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        if (String.valueOf(monthLength).equalsIgnoreCase(date)){
            return true;
        } else {
            return false;
        }

    }

    public class LastDayProtocolAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            lastDayProgressBar = new ProgressDialog(MainActivity.this);
            lastDayProgressBar.setMessage("Initiating \"last day of the month\" protocol...");
            lastDayProgressBar.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String month = strings[0];

            final String LAST_DAY_PROTOCOL_URL = "https://suppliant-fives.000webhostapp.com/librarian/last_day_protocol.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(LAST_DAY_PROTOCOL_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("month", "UTF-8") +"="+ URLEncoder.encode(month, "UTF-8");

                bufferedWriter.write(dataToWrite);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
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
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
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
                lastDayProgressBar.dismiss();
            } else {
                lastDayProgressBar.dismiss();
            }
        }
    }

}
