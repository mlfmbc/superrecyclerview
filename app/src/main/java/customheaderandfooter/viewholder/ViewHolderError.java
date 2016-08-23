package customheaderandfooter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import customheaderandfooter.interfaces.ConvertError;
/**
 * Created on 2016/8/10 13:21.
 * Author:chang
 * Description:
 */
public class ViewHolderError extends ViewHolder{
    private ConvertError ConvertError;
    public ViewHolderError(Context context, View itemView, ViewGroup parent,ConvertError ConvertError) {
        super(context, itemView, parent);
        this.ConvertError=ConvertError;
    }
    public void ConvertErrorView(ViewHolderError holder){
        if(ConvertError!=null){
            mConvertView.setVisibility(View.VISIBLE);
            ConvertError.onConvertError(holder);
        }
    }
    public static ViewHolder get(Context context, ViewGroup parent, int layoutId,ConvertError mConvertError)
    {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolder holder = new ViewHolderError(context, itemView, parent,mConvertError);
        return holder;
    }
}
