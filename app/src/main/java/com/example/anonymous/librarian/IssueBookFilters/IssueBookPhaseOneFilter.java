package com.example.anonymous.librarian.IssueBookFilters;

import android.widget.Filter;

import com.example.anonymous.librarian.Books;
import com.example.anonymous.librarian.IssueBookAdapter.IssueBookPhaseOneAdapter;
import com.example.anonymous.librarian.IssueBookPhaseOne;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookPhaseOneFilter extends Filter {

    IssueBookPhaseOneAdapter adapter;
    ArrayList<Books> oldList;

    public IssueBookPhaseOneFilter(IssueBookPhaseOneAdapter adapter, ArrayList<Books> oldList) {
        this.adapter = adapter;
        this.oldList = oldList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        FilterResults results = new FilterResults();

        if(charSequence != null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Books> newList = new ArrayList<>();
            for(int i = 0; i < oldList.size(); i++){
                if(oldList.get(i).getmBookName().toUpperCase().contains(charSequence)){
                    newList.add(oldList.get(i));
                }
            }

            results.count = newList.size();
            results.values = newList;

        } else {
            results.count = oldList.size();
            results.values = oldList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.oldList = (ArrayList<Books>) filterResults.values;

        adapter.notifyDataSetChanged();

    }

}
