package com.xugaoxiang.ott.appstore.module.appinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zero on 2016/9/20.
 */
public class ScreenshotsAdapter extends RecyclerView.Adapter<ScreenshotsAdapter.SSViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<String> mImageList;

    public ScreenshotsAdapter(Context context, List<String> imageList) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mImageList = imageList;
    }

    @Override
    public SSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_screenshots, parent, false);
        return new SSViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SSViewHolder holder, int position) {
        Glide.with(mContext)
                .load(Constants.BASE_URL + mImageList.get(position))
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivScreenshot);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class SSViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_screenshot)
        ImageView ivScreenshot;

        public SSViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
