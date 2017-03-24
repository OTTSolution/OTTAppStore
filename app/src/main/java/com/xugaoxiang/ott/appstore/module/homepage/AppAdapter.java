package com.xugaoxiang.ott.appstore.module.homepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.R;
import com.xugaoxiang.ott.appstore.contract.OnItemClickListener;
import com.xugaoxiang.ott.appstore.contract.OnItemSelectedListener;
import com.xugaoxiang.ott.appstore.pojo.BeanAppInfo;
import com.xugaoxiang.ott.appstore.pojo.GsonAppList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 主页面应用列表的适配器
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AAViewHolder> {

    private Context mContext;
    private HomoPageFragment mFragment;
    private LayoutInflater inflater;
    private List<Object> mAppList;

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener onItemClickLitener) {
        mOnItemClickLitener = onItemClickLitener;
    }

    private OnItemSelectedListener mOnItemSelectedListener;

    public void setOnItemSelectedLitener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public AppAdapter(Context context, HomoPageFragment fragment, List<Object> appList) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mAppList = appList;
        mFragment = fragment;
    }

    @Override
    public AAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_app, parent, false);
        return new AAViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AAViewHolder holder, final int position) {
        holder.itemView.setId(position + 1000);//这里加上10000是为了避免和category的ID混淆
        if(position == 0 || (position%5 == 0 && position > 4))
            holder.itemView.setNextFocusLeftId(mFragment.getCategoryViewId());
        if (position < 5)
            holder.itemView.setNextFocusUpId(position + 1000);//往上按就把下个焦点设置为本身
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mOnItemSelectedListener.onItemSelected(v, position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(v, position);
            }
        });
        Object object = mAppList.get(position);
        GsonAppList.DataBean dataBean;
        BeanAppInfo appInfo;
        if (object instanceof BeanAppInfo)
        {
            appInfo = (BeanAppInfo) object;
            holder.ivAppIcon.setImageDrawable(appInfo.getAppIcon());
            holder.tvAppName.setText(appInfo.getAppName());
        }
        else if(object instanceof GsonAppList.DataBean)
        {
            dataBean = (GsonAppList.DataBean) object;
            Glide.with(mContext)
                    .load(Constants.BASE_URL + dataBean.getIcon_url())
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivAppIcon);
            holder.tvAppName.setText(dataBean.getAppName());
        }
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }


    public class AAViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_app_icon)
        ImageView ivAppIcon;
        @BindView(R.id.tv_app_name)
        TextView tvAppName;

        public AAViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
