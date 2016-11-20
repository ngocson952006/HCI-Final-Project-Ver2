package com.example.tns.realestate.customviews;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.gordonwong.materialsheetfab.AnimatedFab;

/**
 * Created by TNS on 11/20/2016.
 */

public class TransientFloatingActionButton extends FloatingActionButton implements AnimatedFab {

    private Context context;

    public TransientFloatingActionButton(Context context) {
        super(context);
        this.context = context;
    }

    public TransientFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransientFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void show() {
        this.show(0, 0);
    }

    @Override
    public void show(float translationX, float translationY) {
        // NOTE: Using the parameters is only needed if you want
        // to support moving the FAB around the screen.
        // NOTE: This immediately hides the FAB. An animation can
        // be used instead - see the sample app.
        this.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the FAB.
     */
    @Override
    public void hide() {
        // NOTE: This immediately hides the FAB. An animation can
        // be used instead - see the sample app.
        this.setVisibility(View.INVISIBLE);
    }


}
