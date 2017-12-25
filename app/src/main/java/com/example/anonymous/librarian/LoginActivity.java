package com.example.anonymous.librarian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText mUsername, mPassword;
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username_et);
        mPassword = findViewById(R.id.password_et);

        mLogin = findViewById(R.id.login_btn);
    }
}
