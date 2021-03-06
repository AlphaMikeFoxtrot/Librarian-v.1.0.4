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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    IntentFilter filter;
    public boolean isLastDay;
    ProgressDialog lastDayProgressBar;
    NetworkChangeReceiver receiver;
    Boolean flag = false;

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
        exit();
    }

    public void exit(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you wish to exit the app?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFERENCE), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Toolbar mToolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mToolbar);

        if(!(isNetworkConnected())){
            Toast.makeText(MainActivity.this, "No Internet connection!", Toast.LENGTH_LONG).show();
            finish();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String actualDate = dateFormat.format(date);
        String enhancedDate = actualDate.replace("/", " ");
        String[] splitDate = enhancedDate.split(" ");
        Toast.makeText(this, "" + getSharedPreferences(getString(R.string.URL_PREFERENCE), MODE_PRIVATE).getString(getString(R.string.ADD_BOOK), ""), Toast.LENGTH_SHORT).show();


        isLastDay = checkLastDay(splitDate[2]);

        if (isLastDay) {
            lastDayProtocol();
        }

        TextView textView = findViewById(R.id.ma_header);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.URL_PREFERENCE), MODE_PRIVATE);
        if(preferences.getString(getString(R.string.CENTER), "").toLowerCase().contains("kompally")){

            textView.setText("RFC Kompally");

        } else {

            textView.setText("RFC Secunderabad");

        }

        ArrayList<MainActivityListViewItems> listItems = new ArrayList<>();

        listItems.add(new MainActivityListViewItems("Issue a Book", R.drawable.issue_book));
        listItems.add(new MainActivityListViewItems("Issue a Toy", R.drawable.toys));
        listItems.add(new MainActivityListViewItems("View Currently Issued Toys", R.drawable.issued_toys));
        listItems.add(new MainActivityListViewItems("View Currently Issued Books", R.drawable.issued_book_shelf));
        listItems.add(new MainActivityListViewItems("View subscribers Details", R.drawable.subscribers));
        listItems.add(new MainActivityListViewItems("View Books", R.drawable.books));
        listItems.add(new MainActivityListViewItems("View Toys", R.drawable.view_toys));
        listItems.add(new MainActivityListViewItems("View Report", R.drawable.report));
        listItems.add(new MainActivityListViewItems("Logout", R.drawable.logout));

        MainActivityBaseAdapter adapter = new MainActivityBaseAdapter(getApplicationContext(), listItems);
        GridView gridView = findViewById(R.id.main_activity_grid_view);
        gridView.setAdapter(adapter);
    }

    private void lastDayProtocol() {

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        String month = monthFormat .format(new Date());

        LastDayProtocolAsyncTask lastDayProtocolAsyncTask = new LastDayProtocolAsyncTask();
        lastDayProtocolAsyncTask.execute(month);

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

            final String LAST_DAY_PROTOCOL_URL = "http://fardeenpanjwani.com/librarian/last_day_protocol.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(MainActivity.this).LAST_DAY_PROTOCOL());
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
