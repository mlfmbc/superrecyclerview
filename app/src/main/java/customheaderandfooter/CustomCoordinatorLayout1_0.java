package customheaderandfooter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.superrecyclerview.mlfmbc.superrecyclerview.AppBarBehavior;
import com.superrecyclerview.mlfmbc.superrecyclerview.DensityUtil;
import com.superrecyclerview.mlfmbc.superrecyclerview.R;

import java.lang.reflect.Field;

import customheaderandfooter.adapter.CommonAdapter;

/**
 * Created by chang on 2016/11/8.
 */
public class CustomCoordinatorLayout1_0 extends CoordinatorLayout implements Damping, AppBarBehaviorDamping {
    private RecyclerView RecyclerViewWithDamping;
//    private View top_title;
    private ScrollView deep_scrollview;
    private float totalDy = 0,lastDy=0;
    private Context context;
//    private TextView num;
    private RecyclerView left_rv;
    private boolean move = false;
    private int mIndex = -1;
//    private View head;
    private AppBarLayout appbar;
    private DampingLyout dl;
    private boolean isShowed=true;
    private View touch;
    private int DeepInnerLayoutId,DeepTouchLayoutId,HeadLayoutId,LabelNumId,TopLayoutId,TopRightId;
    private LayoutInflater inflater;
    private ViewGroup DeepInnerLayout,DeepTouchLayout,HeadLayout,LabelNum,TopLayout,TopRight;
    public CustomCoordinatorLayout1_0(Context context) {
        this(context, null);
    }

    public CustomCoordinatorLayout1_0(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCoordinatorLayout1_0(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflater = LayoutInflater.from(context);
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs,
                R.styleable.CustomCoordinatorLayout1_0);
//        DeepInnerLayoutId = styledAttrs.getResourceId(R.styleable.CustomCoordinatorLayout1_0_DeepInnerLayoutId, 0);
//        DeepInnerLayout = (ViewGroup) inflater.inflate(DeepInnerLayoutId, null);
//        DeepTouchLayoutId = styledAttrs.getResourceId(R.styleable.CustomCoordinatorLayout1_0_DeepTouchLayoutId, 0);
//        DeepTouchLayout = (ViewGroup) inflater.inflate(DeepTouchLayoutId, null);
        HeadLayoutId = styledAttrs.getResourceId(R.styleable.CustomCoordinatorLayout1_0_HeadLayoutId, 0);
        HeadLayout = (ViewGroup) inflater.inflate(HeadLayoutId, null);
        LabelNumId = styledAttrs.getResourceId(R.styleable.CustomCoordinatorLayout1_0_LabelNumId, 0);
        LabelNum = (ViewGroup) inflater.inflate(LabelNumId, null);
        TopLayoutId = styledAttrs.getResourceId(R.styleable.CustomCoordinatorLayout1_0_TopLayoutId, 0);
        TopLayout = (ViewGroup) inflater.inflate(TopLayoutId, null);
        TopRightId = styledAttrs.getResourceId(R.styleable.CustomCoordinatorLayout1_0_TopRightId, 0);
        TopRight = (ViewGroup) inflater.inflate(TopRightId, null);
    }

    private void initView() {
        touch=new View(getContext());
//        top_title = findViewById(R.id.top_title);
        deep_scrollview = (ScrollView) findViewById(R.id.deep_scrollview);
//        num = (TextView) findViewById(R.id.num);
        left_rv = (RecyclerView) findViewById(R.id.left_rv);
//        head=findViewById(R.id.head);
        appbar= (AppBarLayout) findViewById(R.id.appbar);
//        LayoutParams a= getResolvedLayoutParams(appbar);
        LayoutParams app= (LayoutParams) appbar.getLayoutParams();
        AppBarBehavior AppBarBehavior= (AppBarBehavior) app.getBehavior();
        if(AppBarBehavior!=null){
            AppBarBehavior.setAppBarBehaviorDamping(this);
        }
        dl= (DampingLyout) findViewById(R.id.dl);
        dl.setmDamping(this);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e("verticalOffset","-"+verticalOffset);
//                xx((Activity) context);
                if(appbar.getVisibility()!=VISIBLE||!isShowed)return;
                OffsetChanged(verticalOffset, appbar);
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
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (mIndex > -1) {
                            smoothMoveToPosition(mIndex);
                            mIndex = -1;
                        }

                    }

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) RecyclerViewWithDamping.getLayoutManager();
                    if (move) {
                        move = false;
                        int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                        if (0 <= n && n < RecyclerViewWithDamping.getChildCount()) {
                            int top = RecyclerViewWithDamping.getChildAt(n).getTop();
                            RecyclerViewWithDamping.scrollBy(0, top);
                        }
                    }
                    onScrolling();
                }
            });
        }
    }
    private void xx(Activity activity){
        if (getStatusBarHeight(activity) >= 0) {
            View scrollerLayout = activity.findViewById(R.id.toolbar);
            scrollerLayout.getLayoutParams().height=getStatusBarHeight(activity)+DensityUtil.dip2px(activity,50);
            setPadding(0, 0, 0, 0);
        }
    }
    private void OffsetChanged(int verticalOffset,AppBarLayout appBarLayout){
        if (verticalOffset == 0) {
            deep_scrollview.scrollTo(0, 0);
            HeadLayout.setAlpha(0);
            TopLayout.setTranslationY(0);
            LabelNum.setTranslationY(0);
            TopRight.setTranslationY(0);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            HeadLayout.setAlpha(1);
            TopLayout.setTranslationY(-appBarLayout.getTotalScrollRange());
            LabelNum.setTranslationY(-appBarLayout.getTotalScrollRange());
            TopRight.setTranslationY(-appBarLayout.getTotalScrollRange()/3);
        } else {
            deep_scrollview.scrollTo(0, (int) (Math.abs(verticalOffset) / 3));
            HeadLayout.setAlpha(0);
            TopLayout.setTranslationY(-(int) (Math.abs(verticalOffset)));
            LabelNum.setTranslationY(-(int) (Math.abs(verticalOffset)));
            TopRight.setTranslationY(-(int) (Math.abs(verticalOffset) / 3));
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
        ((TextView)LabelNum.findViewById(R.id.num)).setText("我是第" + ((CommonAdapter) RecyclerViewWithDamping.getAdapter()).getmDatas().get(position).toString() + "条");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        CoordinatorLayout.LayoutParams LabelNumLp=new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LabelNumLp.topMargin=DensityUtil.dip2px(context, 200);
        LabelNumLp.leftMargin=DensityUtil.dip2px(context, 80);
        addView(LabelNum,LabelNumLp);
        CoordinatorLayout.LayoutParams TopLayoutLp=new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 200));
        addView(TopLayout,TopLayoutLp);
        CoordinatorLayout.LayoutParams TopRightLp=new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 200));
        LabelNumLp.topMargin=DensityUtil.dip2px(context, 200);
        addView(TopRight, TopRightLp);
        CoordinatorLayout.LayoutParams HeadLayoutLp=new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(HeadLayout, HeadLayoutLp);
        CoordinatorLayout.LayoutParams touchLp=new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(touch, touchLp);

    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if(touch.isClickable())return true;
//        return super.onTouchEvent(ev);
//    }

    @Override
    public void DampingEnd() {

    }

    @Override
    public void DampingMoving() {
        touch.setClickable(true);
        setTag(1);
        AnimatorSet AS = new AnimatorSet();

        ObjectAnimator OA = ObjectAnimator.ofFloat(dl, View.TRANSLATION_Y, dl.getTranslationY(), dl.getHeight());
        ObjectAnimator OA1 = ObjectAnimator.ofFloat(TopLayout, View.TRANSLATION_Y, dl.getTranslationY(), dl.getHeight());
        ObjectAnimator OA2 = ObjectAnimator.ofFloat(LabelNum, View.TRANSLATION_Y, dl.getTranslationY(), dl.getHeight());
        ObjectAnimator OA3 = ObjectAnimator.ofFloat(TopRight, View.TRANSLATION_X, 0, TopRight.getWidth());

        OA.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dl.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dl.setVisibility(GONE);
                TopLayout.setVisibility(GONE);
                LabelNum.setVisibility(GONE);
                appbar.setVisibility(GONE);
                isShowed = false;
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
                TopLayout.setVisibility(GONE);
                LabelNum.setVisibility(GONE);
                appbar.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AS.play(OA).with(OA1).with(OA2).with(OA3);
        AS.setDuration(500);
        AS.start();
    }

    @Override
    public void DampingReset() {
        dl.setEnabled(false);
        touch.setClickable(true);
        setTag(1);
        AnimatorSet AS = new AnimatorSet();
        ObjectAnimator OA = ObjectAnimator.ofFloat(dl, View.TRANSLATION_Y, dl.getTranslationY(), 0);
        ObjectAnimator OA1 = ObjectAnimator.ofFloat(TopLayout, View.TRANSLATION_Y, dl.getTranslationY(), 0);
        ObjectAnimator OA2 = ObjectAnimator.ofFloat(LabelNum, View.TRANSLATION_Y, dl.getTranslationY(), 0);
        ObjectAnimator OA3 = ObjectAnimator.ofFloat(TopRight, View.TRANSLATION_X, dl.getTranslationY()==dl.getHeight()?TopRight.getWidth():0,0 );
        AS.play(OA).with(OA1).with(OA2).with(OA3);
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
                TopLayout.setVisibility(VISIBLE);
                LabelNum.setVisibility(VISIBLE);
                appbar.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                deep_scrollview.scrollTo(0, 0);
                isShowed = true;
                dl.setEnabled(true);
                touch.setClickable(false);
                setTag(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                deep_scrollview.scrollTo(0, 0);
                isShowed = true;
                dl.setEnabled(true);
                touch.setClickable(false);
                setTag(0);
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
        TopLayout.setTranslationY(dy/3);
        LabelNum.setTranslationY(dy/3);
    }


    @Override
    public void AppBarBehaviorDamping(int dy) {
        TopLayout.setTranslationY(dy/3);
        LabelNum.setTranslationY(dy/3);
        dl.setTranslationY(dy/3);

    }

    @Override
    public void AppBarBehaviorMoving() {
        Log.e("AppBarBehaviorMoving", "AppBarBehaviorMoving");
        DampingMoving();
    }

    @Override
    public void AppBarBehaviorReset() {
        Log.e("AppBarBehaviorReset","AppBarBehaviorReset");
        DampingReset();
    }
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
