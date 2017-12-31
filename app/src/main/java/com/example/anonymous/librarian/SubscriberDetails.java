package com.example.anonymous.librarian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

public class SubscriberDetails extends AppCompatActivity {

    FloatingActionButton mAddProfilePhoto;
    Bitmap bitmap;
    BarChart barChart;
    Boolean isImageAvailable;
    public String joint_account_raw;
    String mProfilePhotoEncodedString, mProfilePhotoName;
    ProgressDialog progressDialog, progressDialogGetAnalysis, uploadImage, checkImage, getBarGraph;
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
            mSubscriberDailyToysActivity,
            mSubscriberJointAccount;
    ImageView mSubscriberPhoto;
    Button mEditButton;
    SubscriberAnalysisAdapter adapter;
    ListView mListView;
    LinearLayout mLinearLayout;
    Toolbar mToolbar;
    private final int IMAGE_REQUEST = 1;
    public ArrayList<SubscriberAnalysis> analysis = new ArrayList<>();
    JSONArray root;
    NetworkChangeReceiver receiver;
    Boolean flag = false;
    public View view_two;
    IntentFilter filter;

    final String JOINT_ACCOUNT = "JOINT ACCOUNT WITH ";
    final String JOINT_ACCOUNT_NULL = "DOES\'NT HAVE A JOINT ACCOUNT";

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
        toPreviousActivity.putExtra("previousAct", getIntent().getStringExtra("previousAct"));
        startActivity(toPreviousActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_details);
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mToolbar = findViewById(R.id.subscriber_detail_toolbar);
        setSupportActionBar(mToolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mAddProfilePhoto = findViewById(R.id.fab);

        barChart = findViewById(R.id.daily_analysis_bar_graph);

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
        mSubscriberJointAccount = findViewById(R.id.subscriber_detail_joint_account);

        mSubscriberPhoto = findViewById(R.id.subscriber_detail_image_view);

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

        // Toast.makeText(this, "" + getIntent().getStringExtra("subscriberId"), Toast.LENGTH_SHORT).show();
        GetSubscriberDetailsAsyncTask getSubscriberDetailsAsyncTask = new GetSubscriberDetailsAsyncTask();
        getSubscriberDetailsAsyncTask.execute(getIntent().getStringExtra("subscriberId"));

        GetAnalysisAsyncTask getAnalysisAsyncTask = new GetAnalysisAsyncTask();
        getAnalysisAsyncTask.execute(getIntent().getStringExtra("subscriberId"));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // TODO
                Intent toGraphView = new Intent(SubscriberDetails.this, SubscriberAnalysisGraphView.class);
                toGraphView.putExtra("month", analysis.get(i).getmMonthOfAnalysis());
                toGraphView.putExtra("subscriberId", mSubscriberId.getText().toString());
                toGraphView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toGraphView);

            }
        });

        mAddProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);

            }
        });

        new GetDailyAnalysisAsyncTask().execute();

        // Toast.makeText(this, "" + mSubscriberJointAccount.getText().toString(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, ""  + joint_account_raw, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String imageName = mSubscriberName.getText().toString().toLowerCase().replace(" ", "_") + ".jpg";

                new UploadImageAsyncTask().execute(encoded, imageName);

                mSubscriberPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_edit){

            TextView textView = findViewById(R.id.subscriber_detail_joint_account);

            // Toast.makeText(this, "" + textView.getText().toString(), Toast.LENGTH_SHORT).show();

            Intent toEdit = new Intent(SubscriberDetails.this, EditSubscriberDetails.class);
            toEdit.putExtra("subId", mSubscriberId.getText().toString());
            toEdit.putExtra("subName", mSubscriberName.getText().toString());
            toEdit.putExtra("enrolledOn", mSubscriberEnrolledOn.getText().toString());
            toEdit.putExtra("enrolledFor", mSubscriberEnrolledFor.getText().toString());
            toEdit.putExtra("enrollmentType", mSubscriberEnrollmentType.getText().toString());
            toEdit.putExtra("dob", mSubscriberDOB.getText().toString());
            toEdit.putExtra("phone", mSubscriberPhone.getText().toString());
            toEdit.putExtra("jointAccountRaw", textView.getText().toString());
            toEdit.putExtra("jointAccountEdited", JOINT_ACCOUNT_NULL + joint_account_raw);
            toEdit.putExtra("previousAct", getIntent().getStringExtra("previousAct"));
            toEdit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toEdit);

        }
        return true;
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

            final String GET_SUBSCRIBER_URL = "http://fardeenpanjwani.com/librarian/get_individual_subscriber_details.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(SubscriberDetails.this).GET_INDIVIDUAL_SUBSCRIBER_DETAILS());
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
                    String joint_account = root.getString("subscriber_joint_account");

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

                    if(joint_account.toLowerCase().contains("none")){

                        mSubscriberJointAccount.setText("Does\'nt have a joint account.");

                    } else {

                        mSubscriberJointAccount.setText(JOINT_ACCOUNT + joint_account);
                        joint_account_raw = joint_account;

                    }

                    new CheckProfilePhotoAsyncTask().execute(subscriber_name.toLowerCase().replace(" ", "_") + ".jpg");

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(SubscriberDetails.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
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

            final String GET_ANALYSIS_URL = "http://fardeenpanjwani.com/librarian/get_subscriber_analysis.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(SubscriberDetails.this).GET_SUBSCRIBER_ANALYSIS());

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

    public class UploadImageAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            uploadImage = new ProgressDialog(SubscriberDetails.this);
            uploadImage.setMessage("uploading image...");
            uploadImage.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String encodedImageString = strings[0];
            String imageName = strings[1];

            final String UPLOAD_IMAGE_URL = "http://fardeenpanjwani.com/librarian/upload_subscriber_profile_image_enhanced.php";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                URL url = new URL(new ServerScriptsURL(SubscriberDetails.this).UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("encoded_string", "UTF-8") +"="+ URLEncoder.encode(encodedImageString, "UTF-8") +"&"+
                        URLEncoder.encode("image_name", "UTF-8") +"="+ URLEncoder.encode(imageName, "UTF-8");

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
            uploadImage.dismiss();
            if(s.contains("success")){
                Toast.makeText(SubscriberDetails.this, "Profile photo successfully updated\nChanges will be applies once the app is restarted.", Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(SubscriberDetails.this, "An error occurred when updating profile photo\n" + s, Toast.LENGTH_LONG).show();
                Toast.makeText(SubscriberDetails.this, "Profile photo cannot be changed more than once", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CheckProfilePhotoAsyncTask extends AsyncTask<String, Void, String>{

        public String imageName;

        @Override
        protected void onPreExecute() {
            checkImage = new ProgressDialog(SubscriberDetails.this);
            checkImage.setMessage("getting profile photo");
            checkImage.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            imageName = strings[0];
            // String rootJsonResponse = strings[1];

            final String CHECK_URL = "http://www.fardeenpanjwani.com/librarian/check_profile_photo.php";

            HttpURLConnection httpURLConnection = null;
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {

                URL url = new URL(new ServerScriptsURL(SubscriberDetails.this).CHECK_PROFILE_PHOTO());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String dataToWrite = URLEncoder.encode("image_name", "UTF-8") +"="+ URLEncoder.encode(imageName, "UTF-8");

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
                return "0";
            } catch (IOException e) {
                e.printStackTrace();
                return "0";
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
            checkImage.dismiss();
            if(s.contains("true")){

                String stringUrl = "http://www.fardeenpanjwani.com/librarian/subscriber_profile_photo/" + getIntent().getStringExtra("subscriberName").toLowerCase().replace(" ", "_") + ".jpg";
                new GetProfilePhoto().execute(stringUrl);

            } else {

                mSubscriberPhoto.setImageResource(R.drawable.no_image);

            }
        }
    }

    public class GetDailyAnalysisAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            getBarGraph = new ProgressDialog(SubscriberDetails.this);
            getBarGraph.setMessage("Getting analysis...");
            getBarGraph.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            return "";

        }

        @Override
        protected void onPostExecute(String s) {
            getBarGraph.dismiss();
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, Float.parseFloat(mSubscriberDailyBookActivity.getText().toString())));
            entries.add(new BarEntry(1, Float.parseFloat(mSubscriberDailyToysActivity.getText().toString())));

            BarDataSet dataSet = new BarDataSet(entries, "");

            BarData data = new BarData(dataSet);
            Description description = new Description();
            description.setText(" ");
            barChart.setDescription(description);
            int bookColor = android.R.color.holo_red_dark;
            int toyColor = android.R.color.black;
            int[] colors = new int[]{bookColor, toyColor};
            dataSet.setColors(colors, SubscriberDetails.this);
            barChart.setData(data);
            barChart.setDrawGridBackground(false);
            barChart.animateY(2000);
        }
    }

    public class GetProfilePhoto extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            String imageUrl = strings[0];

            Bitmap bitmap;

            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {    //When you change the drawable
            mSubscriberPhoto.setImageBitmap(bitmap);
        }
    }

    public class SetAlertDialogProfilePhoto extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            String imageUrl = strings[0];

            Bitmap bitmap;

            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {    //When you change the drawable
            ImageView imageView = view_two.findViewById(R.id.dialog_imageview);
            imageView.setImageBitmap(bitmap);

        }
    }
}
