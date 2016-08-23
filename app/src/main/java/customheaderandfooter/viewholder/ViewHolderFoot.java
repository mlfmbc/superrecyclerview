package customheaderandfooter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customheaderandfooter.interfaces.ConvertFooter;

/**
 * Created on 2016/8/2 16:18.
 * Author:chang
 * Description:
 */
public class ViewHolderFoot extends ViewHolder{
    private ConvertFooter ConvertFooter;
    public ViewHolderFoot(Context context, View itemView, ViewGroup parent,ConvertFooter ConvertFooter) {
        super(context, itemView, parent);
        this.ConvertFooter=ConvertFooter;
    }
    public void ConvertFootView(ViewHolderFoot holder, String loadingStr){
        if(ConvertFooter!=null){
            mConvertView.setVisibility(View.VISIBLE);
            ConvertFooter.onConvertFooter(holder,loadingStr);
        }
    }
    public static ViewHolder get(Context context, ViewGroup parent, int layoutId,ConvertFooter ConvertFooter)
    {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolder holder = new ViewHolderFoot(context, itemView, parent,ConvertFooter);
        return holder;
    }
}
