package com.example.anonymous.librarian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.Inet4Address;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class BookDetail extends AppCompatActivity {

    TextView mBookName, mBookId, mBookAuthor, mBookAddedOn;
    Button mBack, mDelete;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mToolbar = findViewById(R.id.book_detail_toolbar);
        setSupportActionBar(mToolbar);

        mBack = findViewById(R.id.book_detail_back_button);
        mDelete = findViewById(R.id.book_detail_delete_button);

        mBookName = findViewById(R.id.book_detail_book_name);
        mBookId = findViewById(R.id.book_detail_book_id);
        mBookAuthor = findViewById(R.id.book_detail_book_author);
        mBookAddedOn = findViewById(R.id.book_detail_added_on);

        mBookName.setText(getIntent().getStringExtra("bookName"));
        mBookId.setText(getIntent().getStringExtra("bookId"));
        mBookAuthor.setText(getIntent().getStringExtra("bookAuthor"));

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = dateFormat.parse(getIntent().getStringExtra("addedOn"));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            String dateTwo = calendar.getTime().toString();
            String[] dates = dateTwo.split(" ");
            mBookAddedOn.setText(dates[0] + " " + dates[1] + " " + dates[2] + " " + dates[dates.length - 1]);
        } catch (ParseException e) {
            e.printStackTrace();
            mBookAddedOn.setText(getIntent().getStringExtra("addedOn"));
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toViewBooks = new Intent(BookDetail.this, ViewBooks.class);
                toViewBooks.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toViewBooks);

            }
        });
    }
}
