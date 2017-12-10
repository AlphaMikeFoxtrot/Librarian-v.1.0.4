package com.example.anonymous.librarian.CurrentlyIssuedBooksAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.IssueToyOnClickListeners.IssueToyPhaseOneOnItemClickListener;
import com.example.anonymous.librarian.IssuedBookDetail;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.ViewCurrentlyIssuedBooks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ANONYMOUS on 19-Nov-17.
 */

public class ViewCurrentlyIssuedBookAdapter extends RecyclerView.Adapter<ViewCurrentlyIssuedBookAdapter.ViewCurrentlyIssuedBookAdapterViewHolder> {

    ArrayList<Books> issuedBooks;
    Context context;
    public Intent toDetail;

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

        final Books book = this.issuedBooks.get(position);

        holder.bookName.setText(book.getmBookName());
        holder.bookId.setText(book.getmBookId());
        holder.issuedTo.setText(book.getmBookIssuedTo());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {

            date = df.parse(book.getmBookIssuedOn());
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            String currentDate = cal.getTime().toString();
            String[] dueDatesOne = currentDate.split(" ");
            holder.issuedOn.setText(dueDatesOne[0] + " " + dueDatesOne[1] + " " + dueDatesOne[2] + " " + dueDatesOne[dueDatesOne.length - 1]);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.setOnItemClickListener(new IssueToyPhaseOneOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // TODO :
                // Toast.makeText(context, "book name " + book.getmBookName(), Toast.LENGTH_SHORT).show();
                Intent toDetail = new Intent(context, IssuedBookDetail.class);
                toDetail.putExtra("bookName", book.getmBookName());
                toDetail.putExtra("bookId", book.getmBookId());
                toDetail.putExtra("issuedToName", book.getmBookIssuedTo());
                toDetail.putExtra("issuedOn", book.getmBookIssuedOn());
                toDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toDetail);

            }
        });

        // holder.issuedOn.setText(book.getmBookIssuedOn());

    }

    @Override
    public int getItemCount() {
        return this.issuedBooks.size();
    }


    public class ViewCurrentlyIssuedBookAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bookName, bookId, issuedTo, issuedOn;
        Context context;
        ArrayList<Books> books;
        IssueToyPhaseOneOnItemClickListener onItemClickListener;

        public ViewCurrentlyIssuedBookAdapterViewHolder(View itemView, Context context, ArrayList<Books> books) {
            super(itemView);

            this.context = context;
            this.books = books;

            bookName = itemView.findViewById(R.id.currently_issued_book_name);
            bookId = itemView.findViewById(R.id.currently_issued_book_id);
            issuedTo = itemView.findViewById(R.id.currently_issued_book_to);
            issuedOn = itemView.findViewById(R.id.currently_issued_book_on);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            this.onItemClickListener.onItemClick(view, getLayoutPosition());
        }

        public void setOnItemClickListener(IssueToyPhaseOneOnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

}
