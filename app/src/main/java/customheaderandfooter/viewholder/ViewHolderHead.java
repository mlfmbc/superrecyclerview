package customheaderandfooter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customheaderandfooter.interfaces.ConvertHeader;

/**
 * Created on 2016/8/2 15:49.
 * Author:chang
 * Description:
 */
public class ViewHolderHead extends ViewHolder{
    private ConvertHeader ConvertHeader;
    private ViewHolderHead mViewHolderHead;
    public ViewHolderHead(Context context, View itemView, ViewGroup parent,ConvertHeader ConvertHeader) {
        super(context, itemView, parent);
        this.ConvertHeader=ConvertHeader;
    }
    public void ConvertHeadView(ViewHolderHead holder){
        if(ConvertHeader!=null){
            ConvertHeader.onConvertHeadView(holder);
        }
    }
    public static ViewHolder get(Context context, ViewGroup parent, int layoutId,ConvertHeader ConvertHeader)
    {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolder holder = new ViewHolderHead(context, itemView, parent,ConvertHeader);
        return holder;
    }
}
