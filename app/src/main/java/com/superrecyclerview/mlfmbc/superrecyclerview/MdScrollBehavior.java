package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by chang on 2016/11/2.
 */
public class MdScrollBehavior extends CoordinatorLayout.Behavior<View>{
    public MdScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        Log.e("onNestedPreScroll", "=" + dy);
        if(target instanceof RecyclerView){
            if(((LinearLayoutManager) ((RecyclerView)target).getLayoutManager()).findFirstVisibleItemPosition()==0){
                int leftScrolled = getScollYDistance((RecyclerView) target);
//        Log.e("onNestedPreScroll",leftScrolled+"");
                child.setScrollY(leftScrolled / 3);
            }
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
//        if(target instanceof RecyclerView){
//            if(((LinearLayoutManager) ((RecyclerView)target).getLayoutManager()).findFirstVisibleItemPosition()==0){
//                int leftScrolled = getScollYDistance((RecyclerView) target);
////        Log.e("onNestedPreFling",leftScrolled+"");
//                child.setScrollY(leftScrolled/3);
////        ((NestedScrollView) child).fling((int) velocityY);
//            }
//        }
        return super.onNestedPreFling( coordinatorLayout,  child,  target,  velocityX,  velocityY);
    }

    /**
     * 粗略计算滑动距离
     * @param target
     * @return
     */
    public int getScollYDistance(RecyclerView target ) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) target.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
}
