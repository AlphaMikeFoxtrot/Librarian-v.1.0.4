package com.example.anonymous.librarian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ANONYMOUS on 11-Nov-17.
 */

public class MainActivityAdapter extends ArrayAdapter<MainActivityListViewItems> {

    public MainActivityAdapter(@NonNull Context context, ArrayList<MainActivityListViewItems> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.main_activity_custom_list_view, parent ,false);
        }

        MainActivityListViewItems currentItem = getItem(position);

        ImageView buttonImage = listItemView.findViewById(R.id.button_image);
        buttonImage.setImageResource(currentItem.getmButtonImageSource());

        TextView buttonName = listItemView.findViewById(R.id.button_name);
        buttonName.setText(currentItem.getmButtonName());

        return listItemView;

    }
}
