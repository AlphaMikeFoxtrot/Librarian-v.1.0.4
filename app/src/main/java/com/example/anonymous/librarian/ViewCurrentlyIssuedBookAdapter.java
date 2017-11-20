package com.example.anonymous.librarian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 19-Nov-17.
 */

public class ViewCurrentlyIssuedBookAdapter extends RecyclerView.Adapter<ViewCurrentlyIssuedBookAdapter.ViewCurrentlyIssuedBookAdapterViewHolder> {

    ArrayList<Books> issuedBooks;
    Context context;

    public ViewCurrentlyIssuedBookAdapter(ArrayList<Books> issuedBooks, ViewCurrentlyIssuedBooks context) {
        this.issuedBooks = issuedBooks;
        this.context = context;
    }

    @Override
    public ViewCurrentlyIssuedBookAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currently_issued_book_custom_recycler_view, parent, false);
        return new ViewCurrentlyIssuedBookAdapterViewHolder(listItemView, this.context, this.issuedBooks);
    }

    @Override
    public void onBindViewHolder(ViewCurrentlyIssuedBookAdapterViewHolder holder, int position) {

        Books book = this.issuedBooks.get(position);

        holder.bookName.setText(book.getmBookName());
        holder.bookId.setText(book.getmBookId());
        holder.issuedTo.setText(book.getmBookIssuedTo());
        holder.issuedOn.setText(book.getmBookIssuedOn());

    }

    @Override
    public int getItemCount() {
        return this.issuedBooks.size();
    }


    public class ViewCurrentlyIssuedBookAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView bookName, bookId, issuedTo, issuedOn;
        Context context;
        ArrayList<Books> books;

        public ViewCurrentlyIssuedBookAdapterViewHolder(View itemView, Context context, ArrayList<Books> books) {
            super(itemView);

            this.context = context;
            this.books = books;

            bookName = itemView.findViewById(R.id.currently_issued_book_name);
            bookId = itemView.findViewById(R.id.currently_issued_book_id);
            issuedTo = itemView.findViewById(R.id.currently_issued_book_to);
            issuedOn = itemView.findViewById(R.id.currently_issued_book_on);
        }
    }

}
