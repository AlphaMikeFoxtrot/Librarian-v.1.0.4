package com.example.anonymous.librarian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 26-Dec-17.
 */

public class ReportAdapter extends ArrayAdapter {

    public ReportAdapter(@NonNull Context context, ArrayList<Subscribers> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_view_item, parent, false);
        }

        Subscribers currentSubscriber = (Subscribers) getItem(position);

        TextView subName = listItemView.findViewById(R.id.report_list_item_sub_name);
        subName.setText(currentSubscriber.getmSubscriberName());

        TextView book = listItemView.findViewById(R.id.report_list_item_sub_book);
        book.setText(currentSubscriber.getmSubscriberBookActivity());

        TextView toy = listItemView.findViewById(R.id.report_list_item_sub_toy);
        toy.setText(currentSubscriber.getmSubscriberToyActivity());

        return listItemView;
    }
}
