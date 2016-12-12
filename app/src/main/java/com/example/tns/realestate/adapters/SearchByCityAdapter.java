package com.example.tns.realestate.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tns.realestate.ApartmentDetailActivity;
import com.example.tns.realestate.R;
import com.example.tns.realestate.SearchByCityActivity;
import com.example.tns.realestate.models.ApartmentDetail;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by TNS on 11/27/2016.
 */

public class SearchByCityAdapter extends RecyclerView.Adapter<SearchByCityAdapter.ViewHolder> {

    private static final String TAG = SearchByCityAdapter.class.getSimpleName(); // tag
    private List<ApartmentDetail> listOfApartments;
    private Context context;

    public SearchByCityAdapter(List<ApartmentDetail> listOfApartments, Context context) {
        this.listOfApartments = listOfApartments;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_search_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // start set up content for view holder
        final ApartmentDetail detail = this.listOfApartments.get(position);
        // make sure we have valid content
        if (detail != null) {
            holder.textViewPrice.setText(detail.getPrice());
            holder.textViewAddress.setText(detail.getProvider().getName());
            // source for image view
            Picasso.with(this.context)
                    .load(detail.getBitmapsResource()[1])
                    .fit()
                    .centerCrop()
                    .into(holder.imagePreview);
            // register image click listener
            holder.imagePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(context, ApartmentDetailActivity.class);
                    detailIntent.putExtra("detail_apartment", detail);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        Bundle transientBundle = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right).toBundle();
                        context.startActivity(detailIntent, transientBundle);
                    } else {
                        context.startActivity(detailIntent);
                    }

                }
            });
            // start animation
            Animation swingUpLeftAnimation = AnimationUtils.loadAnimation(this.context, R.anim.swing_up_left);
            holder.imagePreview.startAnimation(swingUpLeftAnimation);
        }
    }


    @Override
    public int getItemCount() {
        return this.listOfApartments.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // image view preview how room looks like
        ImageView imagePreview;
        ImageView imageViewMore;
        // each data item is just a string in this case
        TextView textViewPrice;
        TextView textViewAddress;

        public ViewHolder(View view) {
            super(view);
            this.textViewPrice = (TextView) view.findViewById(R.id.textview_search_biy_city_price);
            this.textViewAddress = (TextView) view.findViewById(R.id.textview_seach_by_city_address);
            this.imagePreview = (ImageView) view.findViewById(R.id.imageview_search_by_city_preview);
            this.imageViewMore = (ImageView) view.findViewById(R.id.imageview_hot);
        }
    }

    public List<ApartmentDetail> getListOfApartments() {
        return this.listOfApartments;
    }
}
