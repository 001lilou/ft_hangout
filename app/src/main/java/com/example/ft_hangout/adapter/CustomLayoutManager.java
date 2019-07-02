package com.example.ft_hangout.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

public class CustomLayoutManager extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 1100f;
    private LinearSmoothScroller smoothScroller;

    private Context _context;

    public CustomLayoutManager(Context context) {
        super(context);
        _context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
        smoothScroller = new LinearSmoothScroller(_context) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return CustomLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

    }

    public void setSpeed(float speed) {
        this.MILLISECONDS_PER_INCH = speed;
    }

}
