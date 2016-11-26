package com.example.tns.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class SearchByPriceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_price);

        // get Toolbars
        ActionBar actionBar = this.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        List<SliceValue> values = new ArrayList<>();

        SliceValue sliceValue = new SliceValue();
        sliceValue.setColor(ChartUtils.COLOR_RED);
        sliceValue.setLabel("1 tỷ-1,5 tỷ");
        sliceValue.setValue(0.6f);
        SliceValue sliceValue1 = new SliceValue();
        sliceValue1.setColor(ChartUtils.COLOR_GREEN);
        sliceValue1.setLabel("800 triệu-1 tỷ");
        sliceValue1.setValue(0.3f);
        SliceValue sliceValue2 = new SliceValue();
        sliceValue2.setColor(ChartUtils.COLOR_BLUE);
        sliceValue2.setLabel("500-800 triệu");
        sliceValue2.setValue(0.1f);


        values.add(sliceValue);
        values.add(sliceValue1);
        values.add(sliceValue2);

//        for (int i = 0; i < numValues; ++i) {
//            values.add(new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor()));
//        }

        // Prepare sample data for Piechart
        PieChartData pieChartData = new PieChartData(values);
        pieChartData.setHasLabels(true);
        //   pieChartData.setValueLabelBackgroundColor(R.color.colorAccent);

        final PieChartView pieChartView = (PieChartView) this.findViewById(R.id.piechart_view_sample);
        pieChartView.setChartRotationEnabled(true);
        pieChartView.setPieChartData(pieChartData);
        // animation for PieChartView
        Animation swingUpLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.swing_up_left);
        pieChartView.startAnimation(swingUpLeftAnimation);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                    this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
