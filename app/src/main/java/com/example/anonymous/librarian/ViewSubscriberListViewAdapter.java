package com.example.anonymous.librarian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANONYMOUS on 14-Nov-17.
 */

public class ViewSubscriberListViewAdapter extends ArrayAdapter {

    public ViewSubscriberListViewAdapter(@NonNull Context context, ArrayList<Subscribers> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subscriber_custom_list_view, parent, false);
        }

        Subscribers currentSubscriber = (Subscribers) getItem(position);

        TextView subscriberId = listItemView.findViewById(R.id.issue_book_custom_list_view_subscriber_id);
        subscriberId.setText(currentSubscriber.getmSubscriberId());

        TextView subscriberName = listItemView.findViewById(R.id.issue_book_custom_list_view_subscriber_name);
        subscriberName.setText(currentSubscriber.getmSubscriberName());

        return listItemView;
    }
}
