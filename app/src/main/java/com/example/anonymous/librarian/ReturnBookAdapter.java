package com.example.anonymous.librarian;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 13-Nov-17.
 */

public class ReturnBookAdapter extends RecyclerView.Adapter<ReturnBookAdapter.ReturnBookViewHolder>{

    Context context;
    ArrayList<Books> mBooks;

    public ReturnBookAdapter(Context context, ArrayList<Books> mBooks) {
        this.context = context;
        this.mBooks = mBooks;
    }

    @Override
    public ReturnBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.issued_book_one_custom_list_view, parent, false);
        return new ReturnBookViewHolder(listItemView, this.context, this.mBooks);
    }

    @Override
    public void onBindViewHolder(ReturnBookViewHolder holder, int position) {

        Books currentBook = mBooks.get(position);

        holder.bookName.setText(currentBook.getmBookName());
        holder.bookId.setText(currentBook.getmBookId());

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class ReturnBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bookName, bookId;
        Context context;
        ArrayList<Books> books;

        public ReturnBookViewHolder(View itemView, Context context, ArrayList<Books> books) {

            super(itemView);

            itemView.setOnClickListener(this);

            this.context = context;
            this.books = books;

            bookName = itemView.findViewById(R.id.book_name);
            bookId = itemView.findViewById(R.id.book_id);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Books bookPressed = mBooks.get(position);

            Intent toReturnBookFinalPhase = new Intent(context, ReturnBookFinalPhase.class);
            toReturnBookFinalPhase.putExtra("book_id", bookPressed.getmBookId());
            toReturnBookFinalPhase.putExtra("book_name", bookPressed.getmBookName());
            toReturnBookFinalPhase.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(toReturnBookFinalPhase);
        }
    }

    public void setFilter(ArrayList<Books> newList){
        mBooks = new ArrayList<>();
        mBooks.addAll(newList);
        notifyDataSetChanged();
    }

}
