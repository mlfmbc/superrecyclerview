package customheaderandfooter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superrecyclerview.mlfmbc.superrecyclerview.DensityUtil;
import com.superrecyclerview.mlfmbc.superrecyclerview.R;

import customheaderandfooter.adapter.CommonAdapter;

/**
 * Created by chang on 2016/11/3.
 */
public class CustomCoordinatorLayout extends CoordinatorLayout implements customheaderandfooter.RecyclerViewWithDamping.Damping {
    private RecyclerViewWithDamping RecyclerViewWithDamping;
    private View top_title;
    private NestedScrollView deep_scrollview;
    private float totalDy = 0,lastDy=0;
    private Context context;
    private TextView num;
    private CustomRecyclerView left_rv;
    private boolean move = false;
    private int mIndex = 0;

    public CustomCoordinatorLayout(Context context) {
        this(context, null);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void initView() {
        if (findViewById(R.id.cs_rv) != null) {
            RecyclerViewWithDamping = (RecyclerViewWithDamping) findViewById(R.id.cs_rv);
            RecyclerViewWithDamping.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
                        move = false;
                        int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                        if (0 <= n && n < RecyclerViewWithDamping.getChildCount()) {
                            int top = RecyclerViewWithDamping.getChildAt(n).getTop();
//                            if(n==RecyclerViewWithDamping.getChildCount()-1){
//                                top=top-DensityUtil.dip2px(context, 500);
//                            }
                            RecyclerViewWithDamping.smoothScrollBy(0, top);
                        }
                    }
                }
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
                    if (move ){
                        move = false;
                        int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                        if ( 0 <= n && n < RecyclerViewWithDamping.getChildCount()){
                            int top = RecyclerViewWithDamping.getChildAt(n).getTop();
                            RecyclerViewWithDamping.scrollBy(0, top);
                        }
                    }
//                    if(dy==0){
//                        totalDy= totalDy-(dy-lastDy);
//                    }else{
                        totalDy -= dy;
//                    }

                    lastDy=dy;

                    if(layoutManager.findFirstVisibleItemPosition()==0){
                        View firstVisiableChildView = layoutManager.findViewByPosition(0);
                        int itemHeight = firstVisiableChildView.getHeight();
                        totalDy=firstVisiableChildView.getTop();
                    }
                    Log.e("totalDy",""+totalDy+"-->"+dy);
                    if (!recyclerView.canScrollVertically(-1)) {
                        onScrolledToTop();
                    } else if (!recyclerView.canScrollVertically(1)) {
                        onScrolledToBottom();
                    } else if (dy < 0) {
                        onScrolledUp();
                    } else if (dy > 0) {
                        onScrolledDown();
                    }
                }
            });
            RecyclerViewWithDamping.setDamping(this);
        }
        top_title = findViewById(R.id.top_title);
        deep_scrollview = (NestedScrollView) findViewById(R.id.deep_scrollview);
        num = (TextView) findViewById(R.id.num);
        left_rv = (CustomRecyclerView) findViewById(R.id.left_rv);
    }
    public void scrollToPositionWithOffset(int i, int i1) {
        ((LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager()).scrollToPositionWithOffset(i, i1);
    }
    public void moveToPosition(int n) {
        mIndex = n;
        RecyclerViewWithDamping.stopScroll();
        LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            RecyclerViewWithDamping.scrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = RecyclerViewWithDamping.getChildAt(n - firstItem).getTop();
            RecyclerViewWithDamping.scrollBy(0, top);
        }else{
            RecyclerViewWithDamping.scrollToPosition(n);
            move = true;
        }

    }
    public void smoothMoveToPosition(int n) {
        mIndex = n;
        RecyclerViewWithDamping.stopScroll();
        LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            RecyclerViewWithDamping.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = RecyclerViewWithDamping.getChildAt(n - firstItem).getTop();
            RecyclerViewWithDamping.smoothScrollBy(0, top);
        }else{
            RecyclerViewWithDamping.smoothScrollToPosition(n);
            move = true;
        }

    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    public void onScrolledUp() {
        onScrolling();
    }

    public void onScrolledDown() {
        onScrolling();
    }

    public void onScrolling() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        if (itemHeight - Math.abs(firstVisiableChildView.getTop()) < DensityUtil.dip2px(context, 50)) {
            num.setText("我是第" + ((CommonAdapter) RecyclerViewWithDamping.getAdapter()).getmDatas().get(position).toString() + "条");
        } else {
            if (position != 0) {
                num.setText("我是第" + ((CommonAdapter) RecyclerViewWithDamping.getAdapter()).getmDatas().get(position - 1).toString() + "条");
            }
        }
        if (Math.abs(totalDy) < DensityUtil.dip2px(context, 200) - DensityUtil.dip2px(context, 50)) {
            ((CoordinatorLayout.LayoutParams) left_rv.getLayoutParams()).setMargins(0, DensityUtil.dip2px(context, 200) - (int) Math.abs(totalDy), 0, 0);
            requestLayout();
        }
        if (Math.abs(totalDy) >= DensityUtil.dip2px(context, 200) - DensityUtil.dip2px(context, 50)) {//&&Math.abs(totalDy)<=DensityUtil.dip2px(context, 200)
            ((CoordinatorLayout.LayoutParams) left_rv.getLayoutParams()).setMargins(0, DensityUtil.dip2px(context, 50), 0, 0);
            requestLayout();
        }

        if (Math.abs(totalDy) >= ((float) DensityUtil.dip2px(context, 150))) {
            top_title.setAlpha(1);
            num.setAlpha(1);
            left_rv.setTranslationY(0);
            ((CoordinatorLayout.LayoutParams) left_rv.getLayoutParams()).setMargins(0, DensityUtil.dip2px(context, 50), 0, 0);
            requestLayout();
        } else {
            top_title.setAlpha(0);
            num.setAlpha(0);
        }
        deep_scrollview.scrollTo(0, (int) (Math.abs(totalDy) / 3));
    }

    public void onScrolledToTop() {
        top_title.setAlpha(0);
        deep_scrollview.scrollTo(0, 0);
        ((CoordinatorLayout.LayoutParams) left_rv.getLayoutParams()).setMargins(0, DensityUtil.dip2px(context, 200), 0, 0);
        requestLayout();
    }

    public void onScrolledToBottom() {
    }

    @Override
    public void DampingMoving(int Distance) {
        if(left_rv!=null){
            left_rv.setTranslationY(Distance);
        }
    }

    @Override
    public void DampingMoving() {
        Moving();
    }
    private void Moving(){
        AnimatorSet AS = new AnimatorSet();
        ObjectAnimator OA1 = ObjectAnimator.ofFloat(left_rv, View.TRANSLATION_Y, RecyclerViewWithDamping.getTranslationY(), RecyclerViewWithDamping.getHeight());
        ObjectAnimator OA = ObjectAnimator.ofFloat(RecyclerViewWithDamping, View.TRANSLATION_Y, RecyclerViewWithDamping.getTranslationY(), RecyclerViewWithDamping.getHeight());
        OA.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                RecyclerViewWithDamping.setVisibility(GONE);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                }, 2000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                RecyclerViewWithDamping.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AS.play(OA).with(OA1);
        AS.setDuration(500);
        AS.start();
    }
    private void reset() {
        RecyclerViewWithDamping.setVisibility(VISIBLE);
        AnimatorSet AS = new AnimatorSet();
        ObjectAnimator OA1 = ObjectAnimator.ofFloat(left_rv, View.TRANSLATION_Y, RecyclerViewWithDamping.getHeight(), 0);
        ObjectAnimator OA = ObjectAnimator.ofFloat(RecyclerViewWithDamping, View.TRANSLATION_Y, RecyclerViewWithDamping.getHeight(), 0);
        AS.play(OA).with(OA1);
        OA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ii = (RecyclerViewWithDamping.getTranslationY()) / RecyclerViewWithDamping.getHeight();
                deep_scrollview.scrollTo(0, (int) (deep_scrollview.getScrollY() * ii));
            }
        });
        OA.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                deep_scrollview.scrollTo(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                deep_scrollview.scrollTo(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AS.setDuration(500);
        AS.start();
    }
}
