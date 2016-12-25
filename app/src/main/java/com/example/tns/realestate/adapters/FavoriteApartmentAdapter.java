package com.example.tns.realestate.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tns.realestate.R;
import com.example.tns.realestate.models.ApartmentDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TNS on 12/25/2016.
 */

public class FavoriteApartmentAdapter extends ArrayAdapter<ApartmentDetail> {

    private List<ApartmentDetail> listOfApartments;
    private Context context;

    public FavoriteApartmentAdapter(Context context, List<ApartmentDetail> listOfApartments) {
        super(context, R.layout.favorite_list_item, listOfApartments);
        this.listOfApartments = listOfApartments;
        this.context = context;
    }

    // view holder
    // An Adapter object acts as a bridge between an AdapterView and
    // the underlying data for that view. The Adapter provides access to
    // the data items. The Adapter is also responsible for making a View
    // for each item in the data set.
    private static class ViewHolder {
        public ImageView apartmentImageView;
        public TextView agentNameTextView;
        public TextView addressTextView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.favorite_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.addressTextView = (TextView) convertView.findViewById(R.id.textview_favorite_item_address);
            viewHolder.agentNameTextView = (TextView) convertView.findViewById(R.id.textview_favorite_item_agentname);
            viewHolder.apartmentImageView = (ImageView) convertView.findViewById(R.id.imageview_favorite_apartment);
            convertView.setTag(viewHolder);
        } else {
            // get view holder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // start to bind value for view holder
        ApartmentDetail detail = this.listOfApartments.get(position);
        viewHolder.agentNameTextView.setText(detail.getProvider().getName());
        viewHolder.addressTextView.setText(detail.getAddress());
        // load bitmap into image
        Picasso.with(this.context)
                .load(detail.getBitmapsResource()[2])
                .fit()
                .centerCrop()
                .into(viewHolder.apartmentImageView);
        return convertView;
    }

    @Nullable
    @Override
    public ApartmentDetail getItem(int position) {
        return super.getItem(position);
    }
}
