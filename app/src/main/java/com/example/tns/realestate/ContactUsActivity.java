package com.example.tns.realestate;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ContactUsActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener {

    private static final String TAG = ContactUsActivity.class.getSimpleName();
    private Menu menu; // a reference to menu for changing menu item's icon programmatically
    private FloatingActionButton floatingActionButtonCallUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        toolbar.setTitle(""); // we don not want to show title
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.floatingActionButtonCallUs = (FloatingActionButton) findViewById(R.id.fab_contact_us);
        this.floatingActionButtonCallUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView imageViewDevelopingTeam = (ImageView) this.findViewById(R.id.imageview_develop_team);
        // start to load resource image into image view using Picasso framework
        Picasso.with(this)
                .load(R.drawable.tree_house)
                .fit()
                .centerCrop()
                .into(imageViewDevelopingTeam);

        NestedScrollView nestedScrollView = (NestedScrollView) this.findViewById(R.id.nested_scrollview_contactus);
        nestedScrollView.setOnScrollChangeListener(this); // register ScrollChangeListener
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_contactus_actitvity, menu);
        //  menu.findItem(R.id.call).setVisible(false);
        return true;
    }


    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (this.menu != null) {
            // detect if fab is hidden or not.
            MenuItem menuItemCallOrShare = this.menu.findItem(R.id.call);
            if (menuItemCallOrShare != null) {
                if (!this.floatingActionButtonCallUs.isShown()) {
                    menuItemCallOrShare.setVisible(true);
                } else {
                    menuItemCallOrShare.setVisible(false);
                }
            }
        }
    }
}
