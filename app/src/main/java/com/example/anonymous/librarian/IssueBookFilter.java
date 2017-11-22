package com.example.anonymous.librarian;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 22-Nov-17.
 */

public class IssueBookFilter extends Filter {
    IssuedBookPhaseOneAdapter adapter;
    ArrayList<Books> oldList;

    public IssueBookFilter(IssuedBookPhaseOneAdapter adapter, ArrayList<Books> oldList) {
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
        adapter.mBooks = (ArrayList<Books>) filterResults.values;

        adapter.notifyDataSetChanged();
    }
}
