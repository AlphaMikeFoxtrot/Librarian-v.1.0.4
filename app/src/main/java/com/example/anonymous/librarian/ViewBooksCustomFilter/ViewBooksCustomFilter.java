package com.example.anonymous.librarian.ViewBooksCustomFilter;

import android.widget.Filter;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.R;
import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.ViewBooksAdapter.ViewBooksAdapter;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 10-Dec-17.
 */

public class ViewBooksCustomFilter extends Filter {

    ViewBooksAdapter adapter;
    ArrayList<Books> oldList;

    public ViewBooksCustomFilter(ViewBooksAdapter adapter, ArrayList<Books> oldList) {
        this.adapter = adapter;
        this.oldList = oldList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if (charSequence != null && charSequence.length() > 0) {

            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Books> newList = new ArrayList<>();
            for(int i = 0; i < oldList.size(); i++){

                if(oldList.get(i).getmBookName().toUpperCase().contains(charSequence)|| oldList.get(i).getmBookId().toUpperCase().contains(charSequence)){

                    newList.add(oldList.get(i));

                }

            }

            results.values = newList;
            results.count = newList.size();

        } else {

            results.values = oldList;
            results.count = oldList.size();

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.oldList = (ArrayList<Books>) filterResults.values;
        adapter.notifyDataSetChanged();

    }

}
