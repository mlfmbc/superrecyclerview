package customheaderandfooter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.superrecyclerview.mlfmbc.superrecyclerview.DensityUtil;
import com.superrecyclerview.mlfmbc.superrecyclerview.R;

import customheaderandfooter.adapter.CommonAdapter;

/**
 * Created by chang on 2016/11/8.
 */
public class CustomCoordinatorLayout1_0 extends CoordinatorLayout implements customheaderandfooter.Damping {
    private RecyclerView RecyclerViewWithDamping;
    private View top_title;
    private ScrollView deep_scrollview;
    private float totalDy = 0,lastDy=0;
    private Context context;
    private TextView num;
    private RecyclerView left_rv;
    private boolean move = false;
    private int mIndex = -1;
    private View head;
    private AppBarLayout appbar;
    private DampingLyout dl;
    private boolean isShowed=true;
    private View touch;
    public CustomCoordinatorLayout1_0(Context context) {
        this(context, null);
    }

    public CustomCoordinatorLayout1_0(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCoordinatorLayout1_0(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void initView() {
        touch=findViewById(R.id.touch);
        top_title = findViewById(R.id.top_title);
        deep_scrollview = (ScrollView) findViewById(R.id.deep_scrollview);
        num = (TextView) findViewById(R.id.num);
        left_rv = (RecyclerView) findViewById(R.id.left_rv);
        head=findViewById(R.id.head);
        appbar= (AppBarLayout) findViewById(R.id.appbar);
        dl= (DampingLyout) findViewById(R.id.dl);
        dl.setmDamping(this);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(appbar.getVisibility()!=VISIBLE||!isShowed)return;
                OffsetChanged(verticalOffset,appbar);
                dl.setTranslationY(0);
            }
        });

        if (findViewById(R.id.cs_rv) != null) {
            RecyclerViewWithDamping = (RecyclerView) findViewById(R.id.cs_rv);
            RecyclerViewWithDamping.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
                    if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        move = false;
                        int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                        if (0 <= n && n < RecyclerViewWithDamping.getChildCount()) {
                            int top = RecyclerViewWithDamping.getChildAt(n).getTop();
                            RecyclerViewWithDamping.smoothScrollBy(0, top);
                        }
                    }
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        if(mIndex>-1){
                            smoothMoveToPosition(mIndex);
                            mIndex=-1;
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
                    onScrolling();
                }
            });
        }
    }
    private void OffsetChanged(int verticalOffset,AppBarLayout appBarLayout){
        if (verticalOffset == 0) {
            deep_scrollview.scrollTo(0, 0);
            top_title.setAlpha(0);
            head.setTranslationY(0);
            num.setTranslationY(0);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            top_title.setAlpha(1);
            head.setTranslationY(-appBarLayout.getTotalScrollRange());
            num.setTranslationY(-appBarLayout.getTotalScrollRange());
        } else {
            deep_scrollview.scrollTo(0, (int) (Math.abs(verticalOffset) / 3));
            top_title.setAlpha(0);
            head.setTranslationY(-(int) (Math.abs(verticalOffset)));
            num.setTranslationY(-(int) (Math.abs(verticalOffset)));
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
    public void onScrolling() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        num.setText("我是第" + ((CommonAdapter) RecyclerViewWithDamping.getAdapter()).getmDatas().get(position).toString() + "条");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }



    @Override
    public void DampingEnd() {

    }

    @Override
    public void DampingMoving() {
        touch.setClickable(true);
        AnimatorSet AS = new AnimatorSet();

        ObjectAnimator OA = ObjectAnimator.ofFloat(dl, View.TRANSLATION_Y, dl.getTranslationY(), dl.getHeight());
        ObjectAnimator OA1 = ObjectAnimator.ofFloat(head, View.TRANSLATION_Y, dl.getTranslationY(), dl.getHeight());
        ObjectAnimator OA2 = ObjectAnimator.ofFloat(num, View.TRANSLATION_Y, dl.getTranslationY(), dl.getHeight());
        OA.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dl.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dl.setVisibility(GONE);
                head.setVisibility(GONE);
                num.setVisibility(GONE);
                appbar.setVisibility(GONE);
                isShowed=false;
                touch.setClickable(false);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DampingReset();
                    }
                }, 2000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                dl.setVisibility(GONE);
                head.setVisibility(GONE);
                num.setVisibility(GONE);
                appbar.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AS.play(OA).with(OA1).with(OA2);
        AS.setDuration(500);
        AS.start();
    }

    @Override
    public void DampingReset() {
        dl.setEnabled(false);
        touch.setClickable(true);
        AnimatorSet AS = new AnimatorSet();
        ObjectAnimator OA = ObjectAnimator.ofFloat(dl, View.TRANSLATION_Y, dl.getTranslationY(), 0);
        ObjectAnimator OA1 = ObjectAnimator.ofFloat(head, View.TRANSLATION_Y, dl.getTranslationY(), 0);
        ObjectAnimator OA2 = ObjectAnimator.ofFloat(num, View.TRANSLATION_Y, dl.getTranslationY(), 0);
        AS.play(OA).with(OA1).with(OA2);
        OA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ii = (dl.getTranslationY()) / (dl.getHeight());
                deep_scrollview.scrollTo(0, (int) (deep_scrollview.getScrollY() * ii));
            }
        });
        OA.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dl.setVisibility(VISIBLE);
                head.setVisibility(VISIBLE);
                num.setVisibility(VISIBLE);
                appbar.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                deep_scrollview.scrollTo(0, 0);
                isShowed=true;
                dl.setEnabled(true);
                touch.setClickable(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                deep_scrollview.scrollTo(0, 0);
                isShowed=true;
                dl.setEnabled(true);
                touch.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AS.setDuration(500);
        AS.start();

    }

    @Override
    public void Damping(int dy) {
        head.setTranslationY(dy/3);
        num.setTranslationY(dy/3);
    }
}
