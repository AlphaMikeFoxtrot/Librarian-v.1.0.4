package com.example.anonymous.librarian.ViewToysCustomFilter;

import android.widget.Filter;

import com.example.anonymous.librarian.Toys;
import com.example.anonymous.librarian.ViewToysAdapter.ViewToysAdapter;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 10-Dec-17.
 */

public class ViewToysCustomFilter extends Filter {

    ViewToysAdapter adapter;
    ArrayList<Toys> oldList;

    public ViewToysCustomFilter(ViewToysAdapter adapter, ArrayList<Toys> oldList) {
        this.adapter = adapter;
        this.oldList = oldList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if (charSequence != null && charSequence.length() > 0) {

            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Toys> newList = new ArrayList<>();
            for(int i = 0; i < oldList.size(); i++){

                if(oldList.get(i).getmToyName().toUpperCase().contains(charSequence)|| oldList.get(i).getmToyId().toUpperCase().contains(charSequence)){

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

        adapter.oldList = (ArrayList<Toys>) filterResults.values;
        adapter.notifyDataSetChanged();

    }

}
