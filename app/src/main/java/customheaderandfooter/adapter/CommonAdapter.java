package customheaderandfooter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import customheaderandfooter.interfaces.ConvertError;
import customheaderandfooter.interfaces.ConvertFooter;
import customheaderandfooter.interfaces.ConvertHeader;
import customheaderandfooter.interfaces.LoadMore;
import customheaderandfooter.viewholder.ViewHolder;
import customheaderandfooter.viewholder.ViewHolderError;
import customheaderandfooter.viewholder.ViewHolderFoot;
import customheaderandfooter.viewholder.ViewHolderHead;

/**
 * Created on 2016/8/16 16:22.
 * Author:chang
 * Description: 通用适配器
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    public interface onItemClick {
        void onItemClick(int position);
    }

    public interface onLongItemClick {
        void onLongItemClick(int position);
    }

    public CommonAdapter.onItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(CommonAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public CommonAdapter.onLongItemClick getOnLongItemClick() {
        return onLongItemClick;
    }

    public void setOnLongItemClick(CommonAdapter.onLongItemClick onLongItemClick) {
        this.onLongItemClick = onLongItemClick;
    }

    protected onLongItemClick onLongItemClick;

    private onItemClick onItemClick;
    protected Context mContext;
    protected int mLayoutId;

    public List<T> getmDatas() {
        return mDatas;
    }

    protected List<T> mDatas = new ArrayList<T>();
    private LayoutInflater mInflater;

    private ConvertHeader mConvertHeader;// 头部数据绑定回调

    private ConvertFooter mConvertFooter;// 底部数据绑定回调

    private ConvertError mConvertError;// Error数据绑定回调

    private ViewHolderHead mViewHolderHead;
    private ViewHolderFoot mViewHolderFoot;
    private ViewHolderError mViewHolderError;

    private int HeadLayoutId, FootLayoutId, ErrorLayoutId;// 头部和底部的id

    private String LoadingStr = "";// 加载时显示的内容

    private boolean IsShowLoading = false;// 是否需要显示最后一个加载提提示

    public boolean isShowHeader() {
        return ShowHeader;
    }

    public void setShowHeader(boolean showHeader) {
        ShowHeader = showHeader;
    }

    private boolean ShowHeader = true;

    public CommonAdapter<T> setIsShowLoading(boolean isShowLoading) {
        IsShowLoading = isShowLoading;
        return this;
    }

    public CommonAdapter<T> setLoadingStr(String loadingStr) {
        LoadingStr = loadingStr;
        return this;
    }

    public void notifyLoadingChanged() {
        notifyItemChanged(getItemCount() - 1);
    }

    public void setmConvertHeader(ConvertHeader mConvertHeader, int HeadLayoutId) {
        this.mConvertHeader = mConvertHeader;
        this.ShowHeader = true;
        this.HeadLayoutId = HeadLayoutId;
    }

    public void setmConvertFooter(ConvertFooter mConvertFooter, int FootLayoutId) {
        this.mConvertFooter = mConvertFooter;
        this.FootLayoutId = FootLayoutId;
    }

    public ConvertError getmConvertError() {
        return mConvertError;
    }

    public void setmConvertError(ConvertError mConvertError, int ErrorLayoutId) {
        this.mConvertError = mConvertError;
        this.ErrorLayoutId = ErrorLayoutId;
    }

    public ConvertFooter getmConvertFooter() {
        return mConvertFooter;
    }

    private LoadMore mLoadMore;

    public LoadMore getmLoadMore() {
        return mLoadMore;
    }

    public void setmLoadMore(LoadMore mLoadMore) {
        this.mLoadMore = mLoadMore;
    }

    public CommonAdapter(Context context, int layoutId) {
        this(context, layoutId, new ArrayList<T>());
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder VH;
        if (viewType == -1) {
            if (mViewHolderHead == null)
                mViewHolderHead = (ViewHolderHead) ViewHolderHead.get(mContext, parent, HeadLayoutId, mConvertHeader);
            VH = mViewHolderHead;
        } else if (viewType == -2) {
            if (mViewHolderFoot == null)
                mViewHolderFoot = (ViewHolderFoot) ViewHolderFoot.get(mContext, parent, FootLayoutId, mConvertFooter);
            VH = mViewHolderFoot;
        } else if (viewType == -3) {
            if (mViewHolderError == null)
                mViewHolderError = (ViewHolderError) ViewHolderError.get(mContext, parent, ErrorLayoutId, mConvertError);
            VH = mViewHolderError;
        } else {
            VH = ViewHolderget(parent, viewType);
        }
        return VH;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHead) {
            ((ViewHolderHead) holder).ConvertHeadView((ViewHolderHead) holder);
        } else if (holder instanceof ViewHolderFoot) {
            ((ViewHolderFoot) holder).ConvertFootView((ViewHolderFoot) holder, LoadingStr);
        } else if (holder instanceof ViewHolderError) {
            ((ViewHolderError) holder).ConvertErrorView((ViewHolderError) holder);
        } else {
            if (mConvertHeader != null && ShowHeader) {
                position = position - 1;
            }
            convert(holder, mDatas.get(position), position);
            final int finalPosition = position;
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(finalPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = mDatas.size();
        if (mConvertHeader != null && ShowHeader) size = mDatas.size() + 1;
        if (mConvertError != null && mDatas.size() == 0) size = mDatas.size() + 1;
        if (mConvertHeader != null && ShowHeader && mConvertError != null && mDatas.size() == 0)
            size = mDatas.size() + 2;
        if (mConvertFooter != null && IsShowLoading) size = mDatas.size() + 1;
        if (mConvertHeader != null && ShowHeader && mConvertFooter != null && IsShowLoading)
            size = mDatas.size() + 2;
        return size;
    }

    public void addDatas(List<T> datas) {
        if (datas == null || (datas != null && datas.isEmpty())) {
            mDatas.clear();
            notifyDataSetChanged();
            return;
        }
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void RefreshHead() {
        if (mConvertHeader != null && ShowHeader) {
            notifyItemChanged(0);
        }
    }

    public void Clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mConvertHeader != null && ShowHeader && position == 0) return -1;//heade
        if (mConvertFooter != null && position == getItemCount() - 1 && IsShowLoading)
            return -2;//foot
        if (mConvertError != null && mDatas.size() == 0 && ((mConvertHeader != null && ShowHeader && position == 1) || (!(mConvertHeader != null && ShowHeader) && position == 0)))
            return -3;
        return ItemViewType(position);
    }

    abstract public int ItemViewType(int position);

    abstract public void convert(ViewHolder holder, T t, int position);

    abstract public ViewHolder ViewHolderget(ViewGroup parent, int viewType);
}
