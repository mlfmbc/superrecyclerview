package com.superrecyclerview.mlfmbc.superrecyclerview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**http://blog.csdn.net/likuan0214/article/details/52199117
 * Created by chang on 2016/11/2.
 */
public class MdTopTitleBehavior extends CoordinatorLayout.Behavior<View> {
    private Context context;
    private RecyclerViewYDistanceWithLinearLayoutManager RecyclerViewYDistanceWithLinearLayoutManager;
    public MdTopTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        RecyclerViewYDistanceWithLinearLayoutManager=new RecyclerViewYDistanceWithLinearLayoutManager();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if(target instanceof RecyclerView){
            if(((LinearLayoutManager) ((RecyclerView)target).getLayoutManager()).findFirstVisibleItemPosition()==0){
                if((((RecyclerView)target).getLayoutManager()).findViewByPosition(0).getTop()==0){
                    child.setAlpha(0);
                }else{
                    float leftScrolled = getScollYDistance((RecyclerView) target);
                    float Alpha = leftScrolled / ((float)DensityUtil.dip2px(context, 150));
                    Log.e("onNestedPreScroll",leftScrolled+"+Alpha="+Alpha);
                    child.setAlpha(Alpha);
                }
            }else{
                child.setAlpha(1);
            }
        }


    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        if(target instanceof RecyclerView){
            if(((LinearLayoutManager) ((RecyclerView)target).getLayoutManager()).findFirstVisibleItemPosition()==0){
                if((((RecyclerView)target).getLayoutManager()).findViewByPosition(0).getTop()==0){
                    child.setAlpha(0);
                }else{
                    float leftScrolled = getScollYDistance((RecyclerView) target);
                    float Alpha = leftScrolled / ((float)DensityUtil.dip2px(context, 150));
                    Log.e("onNestedPreFling",leftScrolled+"+Alpha="+Alpha);
                    child.setAlpha(Alpha);
                }
            }else{
                child.setAlpha(1);
            }
        }


        return  super.onNestedPreFling( coordinatorLayout,  child,  target,  velocityX,  velocityY);
    }

    /**
     * 粗略计算滑动距离
     *
     * @param target
     * @return
     */
    public int getScollYDistance(RecyclerView target) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) target.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//
//        return dependency instanceof RecyclerView;
//    }
//
//    /**
//     * @param parent
//     * @param child
//     * @param dependency
//     * @return
//     */
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        Log.e("onDependentViewChanged","");
//        if (!dependency.canScrollVertically(-1)) {
//            Log.e("onDependentViewChanged","我滑动到了顶部");
//            child.setAlpha(0);
//        }
//        return super.onDependentViewChanged(parent, child, dependency);
//    }
}