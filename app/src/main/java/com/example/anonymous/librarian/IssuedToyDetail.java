package com.example.anonymous.librarian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IssuedToyDetail extends AppCompatActivity {

    TextView mIssuedToyId, mIssuedToyName, mIssuedToyToId, mIssuedToyToName, mIssuedToyOn, mIssuedToyDueDate;

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

        mIssuedToyName.setText(getIntent().getStringExtra("toyName"));
        mIssuedToyId.setText(getIntent().getStringExtra("toyId"));
        mIssuedToyToName.setText(getIntent().getStringExtra("issuedToName"));
        mIssuedToyToId.setText(getIntent().getStringExtra("issuedToId"));
        mIssuedToyOn.setText(getIntent().getStringExtra("issuedOn"));
    }
}
