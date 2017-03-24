package com.xugaoxiang.ott.appstore.module.homepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xugaoxiang.ott.appstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zero on 2016/9/20.
 */
public class AppCategoryAdapter extends RecyclerView.Adapter<AppCategoryAdapter.TViewHolder> {

    private LayoutInflater inflater;
    private String[] mCatagroyList;

    public AppCategoryAdapter(Context context, String[] catagroyList) {
        inflater = LayoutInflater.from(context);
        mCatagroyList = catagroyList;
    }

    @Override
    public TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_app_catagroy, parent, false);
        return new TViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TViewHolder holder, int position) {
        holder.itemView.setId(position + 30000);
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
