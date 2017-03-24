package com.xugaoxiang.ott.appstore.module.homepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xugaoxiang.ott.appstore.R;
import com.xugaoxiang.ott.appstore.contract.OnItemSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zero on 2016/9/20.
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TViewHolder> {

    private LayoutInflater inflater;
    private String[] mCatagroyList;

    private OnItemSelectedListener mOnItemSelectedListener;

    public void setOnItemSelectedLitener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public TextAdapter(Context context, String[] catagroyList) {
        inflater = LayoutInflater.from(context);
        mCatagroyList = catagroyList;
    }

    @Override
    public TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_catagroy, parent, false);
        return new TViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TViewHolder holder, final int position) {
        holder.itemView.setId(position + 2000);
        holder.itemView.setNextFocusRightId(1000);//按右默认跳到隔壁listview的第一项
        if (position == (getItemCount()-1))//如果是最后一个，那么向下的焦点是本身
            holder.itemView.setNextFocusDownId(position + 2000);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mOnItemSelectedListener.onItemSelected(v, position);
            }
        });
        holder.tvCatagroy.setText(mCatagroyList[position]);
    }

    @Override
    public int getItemCount() {
        return mCatagroyList.length;
    }

    public class TViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_category)
        TextView tvCatagroy;

        public TViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
