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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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

        isLoggedIn = sharedPreferences.getBoolean(getString(R.string.LOGIN_BOOLEAN), true);

        if(isLoggedIn){

//            mUsername.setText(sharedPreferences.getString(getString(R.string.LOGIN_USERNAME), ""));
//            mPassword.setText(sharedPreferences.getString(getString(R.string.LOGIN_PASSWORD), ""));
            if(sharedPreferences.getString(getString(R.string.LOGIN_USERNAME), "").equals("admin2")){

                Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMain);
                finish();

            } else if(sharedPreferences.getString(getString(R.string.LOGIN_USERNAME), "").equals("admin")){

                Intent toReport = new Intent(this, ReportMainActivity.class);
                toReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toReport);
                finish();

            }

        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mUsername.getText().toString().toLowerCase().equals("admin2")){

                    if(mPassword.getText().toString().toLowerCase().equals("admin2")){

                        editor.putBoolean(getString(R.string.LOGIN_BOOLEAN), true);
                        editor.putString(getString(R.string.LOGIN_USERNAME), mUsername.getText().toString());
                        editor.putString(getString(R.string.LOGIN_PASSWORD), mPassword.getText().toString());
                        editor.commit();

                        Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toMain);

                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                } else if (mUsername.getText().toString().toLowerCase().equals("admin")) {

                    if(mPassword.getText().toString().toLowerCase().equals("admin")){

                        editor.putBoolean(getString(R.string.LOGIN_BOOLEAN), true);
                        editor.putString(getString(R.string.LOGIN_USERNAME), mUsername.getText().toString());
                        editor.putString(getString(R.string.LOGIN_PASSWORD), mPassword.getText().toString());
                        editor.commit();

                        Intent toReport = new Intent(LoginActivity.this, ReportMainActivity.class);
                        toReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toReport);
                        // Toast.makeText(LoginActivity.this, "username and password correct", Toast.LENGTH_SHORT).show();

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
            if(preferences.getString(getString(R.string.LOGIN_USERNAME), "") == "admin2" && preferences.getString(getString(R.string.LOGIN_PASSWORD), "") == "admin2") {

                // this account is to be
                // used by the librarians
                Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toMainActivity);

            } else if(preferences.getString(getString(R.string.LOGIN_USERNAME), "") == "admin" && preferences.getString(getString(R.string.LOGIN_PASSWORD), "") == "admin"){

                // this account is to be used
                // by the regional council chaps
                // TODO : add activity that implements regional protocol

            }

        }

    }

    public String checkCredentials(String username, String password){

        if(username.trim() == "admin" && password.trim() == "admin"){
            return "admin";
        } else if(username.trim() == "admin2" && password.trim() == "admin2"){
            return "admin2";
        } else {
            return "false";
        }

    }

}
