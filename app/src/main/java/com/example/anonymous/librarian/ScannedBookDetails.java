package com.example.anonymous.librarian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScannedBookDetails extends AppCompatActivity {

    TextView isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_book_details);

        isbn = findViewById(R.id.book_isbn_url);

        isbn.setText(getIntent().getStringExtra("bookUrl"));
    }
}
