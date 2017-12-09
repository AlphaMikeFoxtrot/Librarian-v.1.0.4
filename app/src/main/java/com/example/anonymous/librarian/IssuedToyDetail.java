package com.example.anonymous.librarian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IssuedToyDetail extends AppCompatActivity {

    TextView debugger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_toy_detail);

        debugger =  findViewById(R.id.issued_toy_details_debugger);

        debugger.setText(getIntent().getStringExtra("clickedItem"));
    }
}
