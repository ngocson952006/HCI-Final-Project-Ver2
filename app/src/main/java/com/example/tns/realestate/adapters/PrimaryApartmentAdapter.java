package com.example.tns.realestate.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tns.realestate.ApartmentDetailActivity;
import com.example.tns.realestate.R;
import com.example.tns.realestate.models.ApartmentDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TNS on 11/19/2016.
 */

public class PrimaryApartmentAdapter extends RecyclerView.Adapter<PrimaryApartmentAdapter.ViewHolder>
        implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = PrimaryApartmentAdapter.class.getSimpleName(); // tag

    private Context context;
    private List<ApartmentDetail> listOfApartments;
    private static ApartmentDetail selectedItem;


    public PrimaryApartmentAdapter(Context context, List<ApartmentDetail> listOfApartments) {
        this.context = context;
        this.listOfApartments = listOfApartments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get view holder from  parent
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_info, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ApartmentDetail detail = this.listOfApartments.get(position);
        if (detail != null) {
            holder.textViewPrice.setText(detail.getPrice());
            holder.textViewAddress.setText(detail.getAddress());
            holder.textviewAgentName.setText(detail.getProvider().getName());
            // we should get view in View.post method. Why ?
            // When the onCreate() method is called, the content view is set
            // inflating the layout XML with a LayoutInflater. The process of inflation involves creating the views but not setting their sizes
            holder.imagePreview.post(new Runnable() {
                @Override
                public void run() {
                    // In order to increase app performance, we need to load Bitmap into Image efficiently.
                    // To do this, we need to calculate sample size of
                    // the Bitmap via the Image's size. After having best Bitmap, then we need to cache it for later loading.
                    // In this app, we use open source Picasso framework for this purpose.
                    Picasso.with(context)
                            .load(detail.getBitmapsResource()[0])
                            .fit()
                            .into(holder.imagePreview);
                }
            });

            holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.imageViewMore);
                }
            });

            holder.ratingBar.setRating(detail.getRating());
            holder.imagePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent detailIntent = new Intent(context, ApartmentDetailActivity.class);
                    detailIntent.putExtra("detail_apartment", detail);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        Bundle transientBundle = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right).toBundle();
                        context.startActivity(detailIntent, transientBundle);
                    } else {
                        context.startActivity(detailIntent);
                    }

                }
            });

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.cardview_action_add_favorite) {

        } else if (itemId == R.id.cardview_action_view_detail) {


        }
        return false;
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
        TextView textviewAgentName;
        RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            this.textViewPrice = (TextView) view.findViewById(R.id.textview_price);
            this.textViewAddress = (TextView) view.findViewById(R.id.textview_address);
            this.imagePreview = (ImageView) view.findViewById(R.id.preview_image);
            this.imageViewMore = (ImageView) view.findViewById(R.id.imageview_more);
            this.textviewAgentName = (TextView) view.findViewById(R.id.textview_cardview_agent_name);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this.context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_view, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }
}
