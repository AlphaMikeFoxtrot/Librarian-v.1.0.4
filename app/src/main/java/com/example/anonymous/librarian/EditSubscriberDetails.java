package com.example.anonymous.librarian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditSubscriberDetails extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mNewSubscriberEnrolledOn, mNewSubscriberEnrolledFor, mNewSubscriberEnrollmentType, mNewSubscriberDOB, mNewSubscriberPhone;
    Button mSubmit, mCancel, mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscriber_details);

        mToolbar = findViewById(R.id.edit_subscriber_detail_toolbar);
        setSupportActionBar(mToolbar);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        mNewSubscriberEnrolledOn = findViewById(R.id.edit_subscriber_detail_enrolled_on);
        mNewSubscriberEnrolledFor = findViewById(R.id.edit_subscriber_detail_enrolled_for);
        mNewSubscriberEnrollmentType = findViewById(R.id.edit_subscriber_detail_enrollment_type);
        mNewSubscriberDOB = findViewById(R.id.edit_subscriber_detail_dob);
        mNewSubscriberPhone = findViewById(R.id.edit_subscriber_detail_phone);

        mSubmit = findViewById(R.id.edit_subscriber_detail_submit);
        mCancel = findViewById(R.id.edit_subscriber_detail_cancel);
        mReset = findViewById(R.id.edit_subscriber_detail_reset);

        mNewSubscriberEnrolledOn.setText(getIntent().getStringExtra("enrolledOn"));
        mNewSubscriberEnrolledFor.setText(getIntent().getStringExtra("enrolledFor"));
        mNewSubscriberEnrollmentType.setText(getIntent().getStringExtra(("enrollmentType")));
        mNewSubscriberDOB.setText(getIntent().getStringExtra("dob"));
        mNewSubscriberPhone.setText(getIntent().getStringExtra("phone"));

        progressDialog.dismiss();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitClicked();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent back = new Intent(EditSubscriberDetails.this, ViewSubscribers.class);
                startActivity(back);

            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mNewSubscriberEnrolledOn.setText("");
                mNewSubscriberEnrolledFor.setText("");
                mNewSubscriberEnrollmentType.setText("");
                mNewSubscriberDOB.setText("");
                mNewSubscriberPhone.setText("");

                mNewSubscriberEnrolledOn.setText(getIntent().getStringExtra("enrolledOn"));
                mNewSubscriberEnrolledFor.setText(getIntent().getStringExtra("enrolledFor"));
                mNewSubscriberEnrollmentType.setText(getIntent().getStringExtra(("enrollmentType")));
                mNewSubscriberDOB.setText(getIntent().getStringExtra("dob"));
                mNewSubscriberPhone.setText(getIntent().getStringExtra("phone"));

            }
        });


    }

    private void submitClicked() {
        // TODO;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_subscriber_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_delete){
            deleteSubscriber();
            Toast.makeText(this, "Delete Protocol under development", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    private void deleteSubscriber() {
        // TODO:
    }
}
