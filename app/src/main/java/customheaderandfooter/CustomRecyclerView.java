package customheaderandfooter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import customheaderandfooter.adapter.CommonAdapter;

/**
 * Created on 2016/8/2 14:24.
 * Author:chang
 * Description:  扩展 自定义 不同数据模型的 HeaderView
 * 顺便兼容一下 footerView
 */
public class CustomRecyclerView extends RecyclerView {
    private Context mcontext;
    private CommonAdapter CommonAdapter;
    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mcontext=context;
        addOnScrollListener(new OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(getAdapter()==null)return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == getAdapter().getItemCount() && getAdapter().getItemCount() > 1) {
                    if(CommonAdapter!=null&&CommonAdapter.getmLoadMore()!=null)CommonAdapter.getmLoadMore().onLoadMore();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(getLayoutManager()==null)return;
                if(getLayoutManager()instanceof LinearLayoutManager) {
                    lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                }
                if (!canScrollVertically(-1)) {
//                    Log.e("onNestedPreFling", "我滑动到了顶部");
                }
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof CommonAdapter) CommonAdapter= (CommonAdapter) adapter;
    }

}
