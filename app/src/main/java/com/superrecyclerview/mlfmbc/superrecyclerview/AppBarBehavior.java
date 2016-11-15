package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import customheaderandfooter.AppBarBehaviorDamping;
import customheaderandfooter.Damping;

/**
 * Created by chang on 2016/11/11.
 */
public class AppBarBehavior extends AppBarLayout.Behavior {
    private int mLastMotionY;
    private int mTouchSlop = -1;
    private int mActivePointerId = -1;
    private int dy = 0;
    public customheaderandfooter.AppBarBehaviorDamping getAppBarBehaviorDamping() {
        return AppBarBehaviorDamping;
    }

    public void setAppBarBehaviorDamping(customheaderandfooter.AppBarBehaviorDamping appBarBehaviorDamping) {
        AppBarBehaviorDamping = appBarBehaviorDamping;
    }

    private AppBarBehaviorDamping AppBarBehaviorDamping;

    public AppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && dyUnconsumed == 0) {
            Log.e("上滑中。。。", "上滑中。。。");
        }
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            Log.e("到边界了还在上滑。。。", "到边界了还在上滑。。。");
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            Log.e("下滑中。。。", "下滑中。。。");
        }
        if (dyConsumed == 0 && dyUnconsumed < 0) {
            Log.e("到边界了，还在下滑。。。", "到边界了，还在下滑。。。");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        boolean bb = super.onInterceptTouchEvent(parent, child, ev);

        if (bb && getTopAndBottomOffset() == 0) {
            mLastMotionY = (int) ev.getY();
            mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
            Log.e("onInterceptTouchEvent", "------" + mLastMotionY);
        }
        if(parent.getTag()!=null&&(Integer) parent.getTag()==1){
            return false;
        }
        return bb;

    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        boolean bb = false;

        if (getTopAndBottomOffset() == 0) {
            switch (MotionEventCompat.getActionMasked(ev)) {
                case MotionEvent.ACTION_DOWN: {
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                            mActivePointerId);
                    if (activePointerIndex == -1) {
                        return false;
                    }
                    final int y = (int) MotionEventCompat.getY(ev, activePointerIndex);
                    dy = mLastMotionY - y;

                    if (dy > 0) {
                        dy -= mTouchSlop;
                    } else {
                        dy += mTouchSlop;
                    }
                    if (dy <= 0) {
                        if (AppBarBehaviorDamping != null) {
                            AppBarBehaviorDamping.AppBarBehaviorDamping(Math.abs(dy));
                        }
                    } else {
                        if (AppBarBehaviorDamping != null) {
                            dy = 0;
                            AppBarBehaviorDamping.AppBarBehaviorDamping(Math.abs(dy));
                        }
                        bb = super.onTouchEvent(parent, child, ev);
                    }

//                    Log.e("onTouchEvent", "ACTION_MOVE------" + bb + "____>" + dy);
                    break;
                }

                case MotionEvent.ACTION_UP:
//                    Log.e("onTouchEvent", "ACTION_UP------" + bb + "____>" + dy);
                    if (dy != 0) {
                        if (Math.abs(dy) != 0 && Math.abs(dy) > 100) {
                            if (AppBarBehaviorDamping != null) {
                                AppBarBehaviorDamping.AppBarBehaviorMoving();
                            }
                        } else if (Math.abs(dy) > 0 && Math.abs(dy) < 100) {
                            if (AppBarBehaviorDamping != null) {
                                AppBarBehaviorDamping.AppBarBehaviorReset();
                            }
                        }
//                        if (AppBarBehaviorDamping != null) {
//                            AppBarBehaviorDamping.AppBarBehaviorDamping(Math.abs(dy));
//                        }
                    }
                    // $FALLTHROUGH
                case MotionEvent.ACTION_CANCEL: {

                    break;
                }
            }
        } else {
            bb = super.onTouchEvent(parent, child, ev);
        }

        return bb;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        Log.e("CoordinatorLayout","__"+parent.getPaddingTop());
        return super.onLayoutChild(parent, abl, layoutDirection);
    }

}
