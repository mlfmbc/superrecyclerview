package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by chang on 2016/11/4.
 */
public class RvScrollBehavior extends CoordinatorLayout.Behavior<View>{
    private Context context;
    private float totalDy = 0;
    public RvScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if(target instanceof RecyclerView){
            totalDy -= dy;
            Log.e("onNestedPreScroll","--"+totalDy);
            ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).setMargins(0, (int) (DensityUtil.dip2px(context, 200) - Math.abs(totalDy)), 0, 0);//>DensityUtil.dip2px(context, 50)?
            child.requestLayout();
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        if(target instanceof RecyclerView){

//        Log.e("onNestedPreFling",leftScrolled+"");
//                child.setScrollY(leftScrolled/3);
//        ((RecyclerView) child).fling(0,(int) velocityY);

        }
        return super.onNestedPreFling( coordinatorLayout,  child,  target,  velocityX,  velocityY);
    }
}
