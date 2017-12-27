package com.example.anonymous.librarian;

import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username_et);
        mPassword = findViewById(R.id.password_et);

        mLogin = findViewById(R.id.login_btn);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.LOGIN_PREFERENCE), 0);
        editor = sharedPreferences.edit();

        editor.putBoolean(getString(R.string.LOGIN_BOOLEAN), false);
        editor.commit();

//        checkLogin();

        // Toast.makeText(this, "" + sharedPreferences.getBoolean(getString(R.string.LOGIN_BOOLEAN), true), Toast.LENGTH_SHORT).show();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mUsername.getText().toString().toLowerCase().equals("admin2")){

                    if(mPassword.getText().toString().toLowerCase().equals("admin2")){

                        Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toMain);

                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                } else if (mUsername.getText().toString().toLowerCase().equals("admin")) {

                    if(mPassword.getText().toString().toLowerCase().equals("admin")){

                        // TODO :
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
