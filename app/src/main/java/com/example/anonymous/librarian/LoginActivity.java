package com.example.anonymous.librarian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText mUsername, mPassword;
    Button mLogin;
    SharedPreferences sharedPreferences, sharedPreferencesCenter;
    SharedPreferences.Editor editor, editorCenter;
    Boolean isLoggedIn;


    ProgressDialog lastDayProgressBar;
    IntentFilter filter;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Do you wish to exit the app?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        flag = true;

        mUsername = findViewById(R.id.username_et);
        mPassword = findViewById(R.id.password_et);

        mLogin = findViewById(R.id.login_btn);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.LOGIN_PREFERENCE), 0);
        editor = sharedPreferences.edit();

        sharedPreferencesCenter = getApplicationContext().getSharedPreferences(getString(R.string.URL_PREFERENCE), MODE_PRIVATE);
        editorCenter = sharedPreferencesCenter.edit();

        isLoggedIn = sharedPreferences.getBoolean(getString(R.string.LOGIN_BOOLEAN), true);

        if(isLoggedIn){

            //*************//
            // SECUNDERABAD//
            //*************//
            if(sharedPreferences.getString(getString(R.string.LOGIN_USERNAME), "").equals(getString(R.string.ADMIN_LOCAL))){

                Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMain);
                finish();

            } else if(sharedPreferences.getString(getString(R.string.LOGIN_USERNAME), "").equals(getString(R.string.ADMIN_REGIONAL))) {

                Intent toReport = new Intent(this, ReportMainActivity.class);
                toReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toReport);
                finish();

            }

        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mUsername.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_LOCAL))){

                    if(mPassword.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_LOCAL))){

                        editor.putBoolean(getString(R.string.LOGIN_BOOLEAN), true);
                        editor.putString(getString(R.string.LOGIN_USERNAME), mUsername.getText().toString());
                        editor.putString(getString(R.string.LOGIN_PASSWORD), mPassword.getText().toString());
                        editor.commit();

                        editorCenter.clear();
                        editorCenter.commit();

                        Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toMain);

                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                } else if (mUsername.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_REGIONAL))) {

                    if(mPassword.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_REGIONAL))){

                        editor.putBoolean(getString(R.string.LOGIN_BOOLEAN), true);
                        editor.putString(getString(R.string.LOGIN_USERNAME), mUsername.getText().toString());
                        editor.putString(getString(R.string.LOGIN_PASSWORD), mPassword.getText().toString());
                        editor.commit();

                        editorCenter.clear();
                        editorCenter.commit();

                        Intent toReport = new Intent(LoginActivity.this, ReportMainActivity.class);
                        toReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toReport);
                        // Toast.makeText(LoginActivity.this, "username and password correct", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                    }

                } else if(mUsername.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_LOCAL_KOMPALLY))){

                    if(mPassword.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_LOCAL_KOMPALLY))){

                        editorCenter.putString(getString(R.string.CENTER), getString(R.string.KOMPALLY_CENTER));
                        editorCenter.putString(getString(R.string.ADD_BOOK), "http://www.fardeenpanjwani.com/kompally/add_book.php");
                        editorCenter.putString(getString(R.string.ADD_SUBSCRIBER), "http://www.fardeenpanjwani.com/kompally/add_subscriber.php");
                        editorCenter.putString(getString(R.string.ADD_TOY), "http://www.fardeenpanjwani.com/kompally/add_toy.php");
                        editorCenter.putString(getString(R.string.CHECK_PROFILE_PHOTO), "http://www.fardeenpanjwani.com/kompally/check_profile_photo.php");
                        editorCenter.putString(getString(R.string.DELETE_BOOK), "http://www.fardeenpanjwani.com/kompally/delete_book.php");
                        editorCenter.putString(getString(R.string.DELETE_SUBSCRIBER), "http://www.fardeenpanjwani.com/kompally/delete_subscriber.php");
                        editorCenter.putString(getString(R.string.DELETE_TOY), "http://www.fardeenpanjwani.com/kompally/delete_toy.php");
                        editorCenter.putString(getString(R.string.GET_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_book_details.php");
                        editorCenter.putString(getString(R.string.GET_INDIVIDUAL_ANALYSIS), "http://www.fardeenpanjwani.com/kompally/get_individual_analysis.php");
                        editorCenter.putString(getString(R.string.GET_INDIVIDUAL_SUBSCRIBER_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_individual_subscriber_details.php");
                        editorCenter.putString(getString(R.string.GET_ISSUED_BOOKS), "http://www.fardeenpanjwani.com/kompally/get_issued_books.php");
                        editorCenter.putString(getString(R.string.GET_SINGLE_ISSUED_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_single_issued_book_details.php");
                        editorCenter.putString(getString(R.string.GET_SUBSCRIBERS_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_subscribers_details.php");
                        editorCenter.putString(getString(R.string.GET_SUBSCRIBER_ANALYSIS), "http://www.fardeenpanjwani.com/kompally/get_subscriber_analysis.php");
                        editorCenter.putString(getString(R.string.GET_TEMP_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_temp_book_details.php");
                        editorCenter.putString(getString(R.string.GET_TEMP_TOY_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_temp_toy_details.php");
                        editorCenter.putString(getString(R.string.GET_TOTAL_ANALYSIS), "http://www.fardeenpanjwani.com/kompally/get_total_analysis.php");
                        editorCenter.putString(getString(R.string.GET_TOY_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_toy_details.php");
                        editorCenter.putString(getString(R.string.INSERT_TEMP_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/insert_temp_book_details.php");
                        editorCenter.putString(getString(R.string.INSERT_TEMP_TOY_DETAILS), "http://www.fardeenpanjwani.com/kompally/insert_temp_toy_details.php");
                        editorCenter.putString(getString(R.string.ISSUE_BOOK), "http://www.fardeenpanjwani.com/kompally/issue_book.php");
                        editorCenter.putString(getString(R.string.ISSUE_TOY), "http://www.fardeenpanjwani.com/kompally/issue_toy.php");
                        editorCenter.putString(getString(R.string.LAST_DAY_PROTOCOL), "http://www.fardeenpanjwani.com/kompally/last_day_protocol.php");
                        editorCenter.putString(getString(R.string.RETURN_BOOK), "http://www.fardeenpanjwani.com/kompally/return_book.php");
                        editorCenter.putString(getString(R.string.RETURN_TOY), "http://www.fardeenpanjwani.com/kompally/return_toy.php");
                        editorCenter.putString(getString(R.string.UPDATE_SUBSCRIBER_DETAILS), "http://www.fardeenpanjwani.com/kompally/update_subscriber_details.php");
                        editorCenter.putString(getString(R.string.UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED), "http://www.fardeenpanjwani.com/kompally/upload_subscriber_profile_image_enhanced.php");
                        editorCenter.putString(getString(R.string.UPLOAD_SUBSCRIBER_PROFILE_PHOTO), "http://www.fardeenpanjwani.com/kompally/upload_subscriber_profile_photo.php");
                        editorCenter.putString(getString(R.string.VIEW_CURRENTLY_ISSUED_TOYS), "http://www.fardeenpanjwani.com/kompally/view_currently_issued_toys.php");
                        editorCenter.putString(getString(R.string.CANCEL_ISSUE_BOOK_PROTOCOL), "http://www.fardeenpanjwani.com/kompally/cancel_issue_book_protocol.php");
                        editorCenter.putString(getString(R.string.CANCEL_ISSUE_TOY_PROTOCOL), "http://www.fardeenpanjwani.com/kompally/cancel_issue_toy_protocol.php");
                        editorCenter.putString(getString(R.string.GET_ISSUED_BOOK_TO_ID), "http://www.fardeenpanjwani.com/kompally/get_issued_book_to_id.php");
                        editorCenter.putString(getString(R.string.GET_LAST_UPDATED_IDS), "http://www.fardeenpanjwani.com/kompally/get_last_updated_ids.php");
                        editorCenter.putString(getString(R.string.UPDATE_EXISTING_IDS), "http://www.fardeenpanjwani.com/kompally/update_existing_ids.php");
                        editorCenter.putString(getString(R.string.GET_JOINT_ACCOUNT), "http://www.fardeenpanjwani.com/kompally/get_joint_account.php");
                        editorCenter.putString(getString(R.string.UPDATE_JOINT_ACCOUNT), "http://www.fardeenpanjwani.com/kompally/update_joint_account.php");
                        editorCenter.commit();

                        Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toMain);

                    } else {

                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                    }

                } else if(mUsername.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_REGIONAL_KOMPALLY))){

                    if(mPassword.getText().toString().toLowerCase().equals(getString(R.string.ADMIN_REGIONAL_KOMPALLY))) {
                        editorCenter.putString(getString(R.string.CENTER), getString(R.string.KOMPALLY_CENTER));
                        editorCenter.putString(getString(R.string.ADD_BOOK), "http://www.fardeenpanjwani.com/kompally/add_book.php");
                        editorCenter.putString(getString(R.string.ADD_SUBSCRIBER), "http://www.fardeenpanjwani.com/kompally/add_subscriber.php");
                        editorCenter.putString(getString(R.string.ADD_TOY), "http://www.fardeenpanjwani.com/kompally/add_toy.php");
                        editorCenter.putString(getString(R.string.CHECK_PROFILE_PHOTO), "http://www.fardeenpanjwani.com/kompally/check_profile_photo.php");
                        editorCenter.putString(getString(R.string.DELETE_BOOK), "http://www.fardeenpanjwani.com/kompally/delete_book.php");
                        editorCenter.putString(getString(R.string.DELETE_SUBSCRIBER), "http://www.fardeenpanjwani.com/kompally/delete_subscriber.php");
                        editorCenter.putString(getString(R.string.DELETE_TOY), "http://www.fardeenpanjwani.com/kompally/delete_toy.php");
                        editorCenter.putString(getString(R.string.GET_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_book_details.php");
                        editorCenter.putString(getString(R.string.GET_INDIVIDUAL_ANALYSIS), "http://www.fardeenpanjwani.com/kompally/get_individual_analysis.php");
                        editorCenter.putString(getString(R.string.GET_INDIVIDUAL_SUBSCRIBER_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_individual_subscriber_details.php");
                        editorCenter.putString(getString(R.string.GET_ISSUED_BOOKS), "http://www.fardeenpanjwani.com/kompally/get_issued_books.php");
                        editorCenter.putString(getString(R.string.GET_SINGLE_ISSUED_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_single_issued_book_details.php");
                        editorCenter.putString(getString(R.string.GET_SUBSCRIBERS_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_subscribers_details.php");
                        editorCenter.putString(getString(R.string.GET_SUBSCRIBER_ANALYSIS), "http://www.fardeenpanjwani.com/kompally/get_subscriber_analysis.php");
                        editorCenter.putString(getString(R.string.GET_TEMP_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_temp_book_details.php");
                        editorCenter.putString(getString(R.string.GET_TEMP_TOY_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_temp_toy_details.php");
                        editorCenter.putString(getString(R.string.GET_TOTAL_ANALYSIS), "http://www.fardeenpanjwani.com/kompally/get_total_analysis.php");
                        editorCenter.putString(getString(R.string.GET_TOY_DETAILS), "http://www.fardeenpanjwani.com/kompally/get_toy_details.php");
                        editorCenter.putString(getString(R.string.INSERT_TEMP_BOOK_DETAILS), "http://www.fardeenpanjwani.com/kompally/insert_temp_book_details.php");
                        editorCenter.putString(getString(R.string.INSERT_TEMP_TOY_DETAILS), "http://www.fardeenpanjwani.com/kompally/insert_temp_toy_details.php");
                        editorCenter.putString(getString(R.string.ISSUE_BOOK), "http://www.fardeenpanjwani.com/kompally/issue_book.php");
                        editorCenter.putString(getString(R.string.ISSUE_TOY), "http://www.fardeenpanjwani.com/kompally/issue_toy.php");
                        editorCenter.putString(getString(R.string.LAST_DAY_PROTOCOL), "http://www.fardeenpanjwani.com/kompally/last_day_protocol.php");
                        editorCenter.putString(getString(R.string.RETURN_BOOK), "http://www.fardeenpanjwani.com/kompally/return_book.php");
                        editorCenter.putString(getString(R.string.RETURN_TOY), "http://www.fardeenpanjwani.com/kompally/return_toy.php");
                        editorCenter.putString(getString(R.string.UPDATE_SUBSCRIBER_DETAILS), "http://www.fardeenpanjwani.com/kompally/update_subscriber_details.php");
                        editorCenter.putString(getString(R.string.UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED), "http://www.fardeenpanjwani.com/kompally/upload_subscriber_profile_image_enhanced.php");
                        editorCenter.putString(getString(R.string.UPLOAD_SUBSCRIBER_PROFILE_PHOTO), "http://www.fardeenpanjwani.com/kompally/upload_subscriber_profile_photo.php");
                        editorCenter.putString(getString(R.string.VIEW_CURRENTLY_ISSUED_TOYS), "http://www.fardeenpanjwani.com/kompally/view_currently_issued_toys.php");
                        editorCenter.putString(getString(R.string.CANCEL_ISSUE_BOOK_PROTOCOL), "http://www.fardeenpanjwani.com/kompally/cancel_issue_book_protocol.php");
                        editorCenter.putString(getString(R.string.CANCEL_ISSUE_TOY_PROTOCOL), "http://www.fardeenpanjwani.com/kompally/cancel_issue_toy_protocol.php");
                        editorCenter.putString(getString(R.string.GET_ISSUED_BOOK_TO_ID), "http://www.fardeenpanjwani.com/kompally/get_issued_book_to_id.php");
                        editorCenter.putString(getString(R.string.GET_LAST_UPDATED_IDS), "http://www.fardeenpanjwani.com/kompally/get_last_updated_ids.php");
                        editorCenter.putString(getString(R.string.UPDATE_EXISTING_IDS), "http://www.fardeenpanjwani.com/kompally/update_existing_ids.php");
                        editorCenter.putString(getString(R.string.GET_JOINT_ACCOUNT), "http://www.fardeenpanjwani.com/kompally/get_joint_account.php");
                        editorCenter.putString(getString(R.string.UPDATE_JOINT_ACCOUNT), "http://www.fardeenpanjwani.com/kompally/update_joint_account.php");
                        editorCenter.commit();

                        Intent toReport = new Intent(LoginActivity.this, ReportMainActivity.class);
                        toReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toReport);

                    } else {

                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(LoginActivity.this, "Username and password do not match", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    /**
     * checks whether the user is
     * already logged in
     */
    public void checkLogin(){

        SharedPreferences preferences = getSharedPreferences(getString(R.string.LOGIN_PREFERENCE), 0);
        if(preferences.getBoolean(getString(R.string.LOGIN_BOOLEAN), false)){

            // this means that the user has signed in earlier
            // and needs to be taken to the main activity
            if(preferences.getString(getString(R.string.LOGIN_USERNAME), "") == getString(R.string.ADMIN_LOCAL) && preferences.getString(getString(R.string.LOGIN_PASSWORD), "") == getString(R.string.ADMIN_LOCAL)) {

                // this account is to be
                // used by the librarians
                Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);

            } else if(preferences.getString(getString(R.string.LOGIN_USERNAME), "") == getString(R.string.ADMIN_REGIONAL) && preferences.getString(getString(R.string.LOGIN_PASSWORD), "") == getString(R.string.ADMIN_REGIONAL)){

                // this account is to be used
                // by the regional council chaps
                // TODO : add activity that implements regional protocol

            }

        }

    }

    public String checkCredentials(String username, String password){

        if(username.trim() == getString(R.string.ADMIN_REGIONAL) && password.trim() == getString(R.string.ADMIN_REGIONAL)){
            return getString(R.string.ADMIN_REGIONAL);
        } else if(username.trim() == getString(R.string.ADMIN_LOCAL) && password.trim() == getString(R.string.ADMIN_LOCAL)){
            return getString(R.string.ADMIN_LOCAL);
        } else {
            return "false";
        }

    }

}
